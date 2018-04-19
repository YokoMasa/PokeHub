package com.masalab.masato.githubfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Masato on 2018/02/27.
 */

public class Action extends BaseModel {

    public Type type;
    public String triggerUrl;
    public static Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel parcel) {
            Action action = new Action();
            action.type = (Type) parcel.readSerializable();
            action.triggerUrl = parcel.readString();
            return action;
        }

        @Override
        public Action[] newArray(int i) {
            return new Action[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(type);
        parcel.writeString(triggerUrl);
    }

    public enum Type {
        REPO, ISSUE, PR
    }
}
