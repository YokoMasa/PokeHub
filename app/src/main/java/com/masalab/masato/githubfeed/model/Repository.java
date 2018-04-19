package com.masalab.masato.githubfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Masato on 2018/01/27.
 */

public class Repository extends BaseModel {

    public String fullName;
    public String name;
    public String owner;
    public String baseUrl;
    public String htmlUrl;
    public String lang;
    public String description;
    public Profile ownerProfile;
    public Repository parent;
    public int stars;
    public int watches;
    public int forks;
    public static Parcelable.Creator<Repository> CREATOR = new Parcelable.Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel parcel) {
            Repository repo = new Repository();
            repo.fullName = parcel.readString();
            repo.name = parcel.readString();
            repo.owner = parcel.readString();
            repo.baseUrl = parcel.readString();
            repo.htmlUrl = parcel.readString();
            repo.lang = parcel.readString();
            repo.description = parcel.readString();
            repo.ownerProfile = parcel.readParcelable(getClass().getClassLoader());
            repo.parent = parcel.readParcelable(getClass().getClassLoader());
            repo.stars = parcel.readInt();
            repo.watches = parcel.readInt();
            repo.forks = parcel.readInt();
            return repo;
        }

        @Override
        public Repository[] newArray(int i) {
            return new Repository[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(name);
        parcel.writeString(owner);
        parcel.writeString(baseUrl);
        parcel.writeString(htmlUrl);
        parcel.writeString(lang);
        parcel.writeString(description);
        parcel.writeParcelable(ownerProfile, 0);
        parcel.writeParcelable(parent, 0);
        parcel.writeInt(stars);
        parcel.writeInt(watches);
        parcel.writeInt(forks);
    }
}
