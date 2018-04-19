package com.masalab.masato.githubfeed.view.fragment.repocontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;
import com.masalab.masato.githubfeed.view.fragment.repocontent.breadcrumb.BreadCrumbsFragment;
import com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist.ContentListContract;
import com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist.ContentListFragment;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.repocontent.breadcrumb.BreadCrumb;
import com.masalab.masato.githubfeed.view.fragment.repocontent.breadcrumb.BreadCrumbsView;

import java.util.List;

/**
 * Created by Masato on 2018/02/26.
 */

public class RepoContentFragment extends BaseFragment implements
        BreadCrumbsFragment.OnBreadCrumbClickListener, ContentListFragment.OnContentsListChangeListener {

    private BreadCrumbsView breadCrumbsView;
    private ContentListContract.View contentListView;
    private boolean isFragmentCreated;
    private ContentNode current;

    @Override
    public boolean isZombie() {
        boolean result = false;
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                if (baseFragment.isZombie()) {
                    result = true;
                }
            }
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        deleteZombieFragments();
    }

    private void deleteZombieFragments() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        doSafeViewTask(() -> {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            for (Fragment f : fragments) {
                ft.remove(f);
            }
            ft.commit();
        });
    }

    @Override
    public void onTryAgain() {

    }

    @Override
    public void onBreadCrumbClicked(BreadCrumb breadCrumb) {
        ContentNode contentNode = (ContentNode) breadCrumb.data;
        this.current = contentNode;
        contentListView.performClick(contentNode);
    }

    @Override
    public void nextContent(ContentNode contentNode) {
        this.current = contentNode;
        BreadCrumb breadCrumb = new BreadCrumb();
        breadCrumb.data = contentNode;
        breadCrumb.name = contentNode.name;
        breadCrumbsView.addBreadCrumb(breadCrumb);
    }

    @Override
    public void previousContent() {
        if (current.parent != null) {
            current = current.parent;
        }
        breadCrumbsView.goBack();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isFragmentCreated) {
            initFragments();
            isFragmentCreated = true;
        }
    }

    private void initFragments() {
        Repository repository = getArguments().getParcelable("repo");
        doSafeViewTask(() -> {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();

            BreadCrumbsFragment breadCrumbsFragment = new BreadCrumbsFragment();
            ft.add(R.id.repo_content_bread_crumbs, breadCrumbsFragment);
            breadCrumbsView = breadCrumbsFragment;

            ContentListFragment contentListFragment = FragmentFactory.createContentListFragment(repository, "");
            ft.add(R.id.repo_content_list, contentListFragment);
            contentListView = contentListFragment;

            ft.commit();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("current_node", current);
    }
}
