package com.masalab.masato.githubfeed.view.fragment.profilelist;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masato on 2018/03/10.
 */

public class ProfileListFragment extends PaginatingListFragment implements ProfileListContract.View {

    @Override
    public void showProfile(Profile profile) {
        Navigator.navigateToProfileView(getContext(), profile.url);
    }

    @Override
    protected PaginatingListContract.Presenter onCreatePresenter() {
        ProfileListContract.Presenter presenter = null;
        String url = getArguments().getString("url");
        if (url != null) {
            presenter = new ProfileListPresenter(url);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
        return presenter;
    }

    @Override
    protected PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.profile_list_element, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType) {
        ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;
        Profile profile = (Profile) element;
        profileViewHolder.bindProfile(profile);
    }

    private class ProfileViewHolder extends PaginatingListViewHolder {

        AppCompatTextView name;
        CircleImageView image;

        void bindProfile(Profile profile) {
            name.setText(profile.name);
            Picasso.with(getContext()).load(profile.iconUrl).into(image);
        }

        public ProfileViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.profile_list_name);
            image = itemView.findViewById(R.id.profile_list_image);
        }
    }

}
