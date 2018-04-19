package com.masalab.masato.githubfeed.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Masato on 2018/01/19.
 */

public class Profile extends BaseModel {

    public String name;
    public String iconUrl;
    public String url;
    public String htmlUrl;
    public String type;
    public String realName;
    public String company;
    public String blog;
    public String location;
    public String email;
    public String bio;
    public Bitmap icon;
    public static Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel parcel) {
            Profile profile = new Profile();
            profile.name = parcel.readString();
            profile.iconUrl = parcel.readString();
            profile.url = parcel.readString();
            profile.htmlUrl = parcel.readString();
            profile.type = parcel.readString();
            profile.realName = parcel.readString();
            profile.company = parcel.readString();
            profile.blog = parcel.readString();
            profile.location = parcel.readString();
            profile.email = parcel.readString();
            profile.bio = parcel.readString();
            profile.icon = parcel.readParcelable(getClass().getClassLoader());
            return profile;
        }

        @Override
        public Profile[] newArray(int i) {
            return new Profile[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(iconUrl);
        parcel.writeString(url);
        parcel.writeString(htmlUrl);
        parcel.writeString(type);
        parcel.writeString(realName);
        parcel.writeString(company);
        parcel.writeString(blog);
        parcel.writeString(location);
        parcel.writeString(email);
        parcel.writeString(bio);
        parcel.writeParcelable(icon, 0);
    }
}
