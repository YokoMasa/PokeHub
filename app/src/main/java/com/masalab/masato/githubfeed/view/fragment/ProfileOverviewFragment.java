package com.masalab.masato.githubfeed.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.navigator.Navigator;

/**
 * Created by Masato on 2018/03/07.
 */

public class ProfileOverviewFragment extends BaseFragment {

    @Override
    public boolean isZombie() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        Profile profile = getArguments().getParcelable("profile");
        if (profile == null) {
            return;
        }

        if (profile.realName != null && !profile.realName.equals("null")) {
            AppCompatTextView name = view.findViewById(R.id.profile_name);
            name.setText(profile.realName);
        }

        if (profile.company != null && !profile.company.equals("null")) {
            AppCompatTextView org = view.findViewById(R.id.profile_org);
            org.setText(profile.company);
        }

        if (profile.email != null && !profile.email.equals("null")) {
            AppCompatTextView email = view.findViewById(R.id.profile_email);
            email.setText(toLinkSpanned(profile.email));
            email.setOnClickListener(view1 -> {
                Navigator.navigateToEmail(getContext(), profile.email);
            });
        }

        if (profile.blog != null && !profile.blog.equals("null")) {
            AppCompatTextView blog = view.findViewById(R.id.profile_url);
            blog.setText(toLinkSpanned(profile.blog));
            blog.setOnClickListener(view1 -> {
                Navigator.navigateToExternalBrowser(getContext(), profile.blog);
            });
        }

        if (profile.bio != null && !profile.bio.equals("null")) {
            AppCompatTextView bio = view.findViewById(R.id.profile_bio);
            bio.setText(profile.bio);
        }
    }

    @Override
    public void onTryAgain() {

    }

    private Spanned toLinkSpanned(String text) {
        Spannable spannable = new SpannableString(text);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GREEN);
        spannable.setSpan(underlineSpan, 0, text.length(), Spanned.SPAN_COMPOSING);
        spannable.setSpan(foregroundColorSpan, 0, text.length(), Spanned.SPAN_COMPOSING);
        return spannable;
    }

}
