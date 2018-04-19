package com.masalab.masato.githubfeed.view.fragment.paginatinglist;

import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.BaseModel;

import static com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract.View.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Masato on 2018/01/29.
 *
 * このクラスはツイッターのように、あるスクロール地点まで行くと次のページのものを取ってくるという機能を実装したクラスです。
 * PaginatingListFragmentのonCreatePresenter()コールバックでこのクラスのインスタンスを渡すことができます。
 * 実際になんのオブジェクトのリストを持つか、RecyclerViewのリストの要素がタップされたときになんの処理を行うか、という
 * ことについては、このクラスのサブクラスが実装します。
 *
 */

public abstract class PaginatingListPresenter implements PaginatingListContract.Presenter {

    private static final int DEFAULT_FETCH_THRESHOLD = 15;

    private PaginatingListContract.View view;
    private ArrayList<BaseModel> elementList = new ArrayList<>();
    private int currentPage = 1;
    private int maxPage = -1;
    private int fetchThreshold = DEFAULT_FETCH_THRESHOLD;
    private boolean feedMaxedOut = false;
    private boolean refreshing = false;
    private boolean fetching = false;

    protected void setFetchThreshold(int fetchThreshold) {
        this.fetchThreshold = fetchThreshold;
    }

    protected void setMaxPage(int maxPage) { this.maxPage = maxPage; }

    @Override
    public void setView(PaginatingListContract.View view) {
        this.view = view;
    }

    protected void reset() {
        elementList.clear();
        feedMaxedOut = false;
        currentPage = 0;
        view.updateAdapter();
    }

    /**
     * ポジションの位置にあるアイテムを返します。
     * @param position リスト中のアイテムのポジション。
     * @return 該当するポジションにあるアイテム。
     */
    public BaseModel getItem(int position) {
        return elementList.get(position);
    }

    /**
     * ポジションに応じたviewTypeを返します。リストに何もなければNOTHING_TO_SHOW_VIEW、
     * リストの最後のポジションであればLOADING_VIEW、リストの最後のポジションだがもうこれ以上次のページから
     * アイテムが取得できない場合はELEMENT_VIEWを返します。
     * @param position viewTypeを知りたいポジション。
     * @return そのポジションに応じたviewType。
     */
    @Override
    public int getItemViewType(int position) {
        if (feedMaxedOut) {
            if (elementList.size() == 0) {
                return NOTHING_TO_SHOW_VIEW;
            }
            return onGetPaginatingItemViewType(getItem(position));
        }
        if (position == getItemCount() - 1) {
            return LOADING_VIEW;
        }
        return onGetPaginatingItemViewType(getItem(position));
    }

    protected abstract int onGetPaginatingItemViewType(BaseModel element);

    /**
     * ユーザーがある程度スクロールしたとき、またはユーザーが画面を下に引っ張って更新したときに呼ばれます。
     * そのページにあるアイテムを取得する処理を実装します。
     * @param page アイテムをとってきてほしいページ。
     */
    protected abstract void onFetchElement(int page);

    protected void onFetchSucceeded(List<? extends BaseModel> elements) {
        addElements(elements);
    }

    protected void onFetchFailed(Failure failure, String errorMessage) {
        view.stopRefreshing();
        view.showErrorView(failure, errorMessage);
        refreshing = false;
        fetching = false;
    }

    /**
     * 取得したアイテムのリストを持っているリストに追加します。アイテムの数やフラグによって処理が変わるので
     * onFetchElement()によって取得したアイテムはこのメソッドで追加される必要があります。
     * @param elements 取得したアイテムのリスト。
     */
    private void addElements(List<? extends BaseModel> elements) {
        if (refreshing) {
            this.elementList.clear();
            this.elementList.addAll(elements);
            view.stopRefreshing();
            refreshing = false;
        } else {
            if (elements.size() == 0) {
                feedMaxedOut = true;
            } else {
                this.elementList.addAll(elements);
                feedMaxedOut = currentPage == maxPage;
            }
        }
        view.updateAdapter();
        fetching = false;
    }

    public void tryAgain() {
        view.hideErrorView();
        refresh();
    }

    /**
     * PaginatingListFragmentのRecyclerViewが初期化されるとき、またはユーザーが画面を下に引っ張ったときに
     * リストを初期化するために呼びます。
     */
    public void refresh() {
        if (refreshing) {
            return;
        }
        feedMaxedOut = false;
        currentPage = 1;
        refreshing = true;
        fetching = true;
        onFetchElement(1);
    }

    /**
     * 与えられたポジションがリストの終端に近づいていて、fetchThresholdで定められた基準を下回っているかどうかを
     * 判定します。例えばアイテムが20個ありthresholdが5の時、最後から5番目のアイテムが表示されるときに次のページ
     * からのアイテムの取得が必要だと判断され、onFetchElement()が呼ばれます。
     * @param position 現在表示されようとしているアイテムのポジション。
     */
    public void fetchElementIfNeeded(int position) {
        if (fetching) {
            return;
        }
        int remaining = getItemCount() - position;
        if (remaining < fetchThreshold && !feedMaxedOut) {
            fetching = true;
            onFetchElement(currentPage + 1);
            currentPage++;
        }
    }

    /**
     * 表示するアイテムの数を返します。リストが0個だからと言って0が帰るわけではありません。
     * リストが0個の時は、取得中であることを表すLOADING_VIEW1個か、取得が終わり本当にアイテムが0個であること
     * を表すNOTHING_TO_SHOW_VIEW1個を返します。
     * @return 表示するアイテムの数。
     */
    @Override
    public int getItemCount() {
        if (feedMaxedOut) {
            if (elementList.size() == 0) {
                return 1;
            }
            return elementList.size();
        }
        return elementList.size() + 1;
    }

}
