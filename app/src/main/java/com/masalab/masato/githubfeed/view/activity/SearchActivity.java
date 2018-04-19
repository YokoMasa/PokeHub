package com.masalab.masato.githubfeed.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;
import com.masalab.masato.githubfeed.view.fragment.repolist.search.RepoSearchContract;
import com.masalab.masato.githubfeed.view.fragment.repolist.search.RepoSearchFragment;

import java.util.List;

/**
 * Created by Masato on 2018/03/06.
 */

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private RepoSearchContract.View repoSearchView;
    private boolean isFragmentInited;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        setUpActionBar();
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.general_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search);
    }

    private void initFragment(String q) {
        doSafeFTTransaction(() -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment f : fragments) {
                ft.remove(f);
            }

            RepoSearchFragment repoSearchFragment = FragmentFactory.createRepoSearchFragment(q);
            ft.add(R.id.general_mother, repoSearchFragment);
            ft.commit();
            repoSearchView = repoSearchFragment;
            isFragmentInited = true;
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_view_menu, menu);
        setUpSearchView(menu);
        return true;
    }

    private void setUpSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getSupportActionBar().setTitle(query);
        if (isFragmentInited) {
            repoSearchView.showSearchResult(query);
        } else {
            initFragment(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onTryAgain() {

    }
}
