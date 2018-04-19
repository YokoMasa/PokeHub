package com.masalab.masato.githubfeed.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.view.fragment.eventlist.EventListFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;

/**
 * Created by Masato on 2018/01/27.
 */

public class GlobalFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        Toolbar toolbar = (Toolbar) findViewById(R.id.general_tool_bar);
        toolbar.setTitle(R.string.feed_timeline);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            initFragment();
        }
    }

    private void initFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EventListFragment eventListFragment =
                FragmentFactory.createEventListFragment("https://api.github.com/events", "");
        ft.add(R.id.general_mother, eventListFragment, "f1");
        ft.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
