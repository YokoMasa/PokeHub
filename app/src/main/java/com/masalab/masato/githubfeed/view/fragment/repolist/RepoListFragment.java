package com.masalab.masato.githubfeed.view.fragment.repolist;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListFragment;

/**
 * Created by Masato on 2018/03/04.
 */

public class RepoListFragment extends PaginatingListFragment implements RepoListContract.View {

    @Override
    final protected PaginatingListContract.Presenter onCreatePresenter() {
        RepoListContract.Presenter presenter = onCreateRepoListPresenter();
        presenter.setView(this);
        return presenter;
    }

    protected RepoListContract.Presenter onCreateRepoListPresenter() {
        RepoListContract.Presenter presenter = null;
        String url = getArguments().getString("url");
        if (url != null) {
            presenter = new RepoListPresenter(url);
        } else {
            throw new RuntimeException("no valid argument");
        }
        return presenter;
    }

    @Override
    public void showRepo(Repository repository) {
        Navigator.navigateToRepoView(getContext(), repository.baseUrl);
    }

    @Override
    protected PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.repo_list_element, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType) {
        RepoViewHolder repoViewHolder = (RepoViewHolder) holder;
        Repository repository = (Repository) element;
        repoViewHolder.bindRepo(repository);
    }

    private class RepoViewHolder extends PaginatingListViewHolder {

        AppCompatTextView title;
        AppCompatTextView description;
        AppCompatTextView stars;
        AppCompatTextView forks;
        AppCompatTextView lang;

        void bindRepo(Repository repository) {
            title.setText(repository.fullName);
            stars.setText(Integer.toString(repository.stars));
            forks.setText(Integer.toString(repository.forks));
            if (repository.description != null) { description.setText(repository.description); }
            if (repository.lang != null && !repository.lang.equals("null")) { lang.setText(repository.lang); }
        }

        public RepoViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.repo_list_element_title);
            description = itemView.findViewById(R.id.repo_list_element_description);
            stars = itemView.findViewById(R.id.repo_list_element_stars);
            forks = itemView.findViewById(R.id.repo_list_element_forks);
            lang = itemView.findViewById(R.id.repo_list_element_lang);
        }
    }
}
