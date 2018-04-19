package com.masalab.masato.githubfeed.model.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.masalab.masato.githubfeed.model.Action;
import com.masalab.masato.githubfeed.model.BaseModel;

/**
 * Created by Masato on 2018/02/09.
 */

public class Event extends BaseModel {

    public String content;
    public Action action;
    public String iconUrl;
    public static Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel parcel) {
            Event event = new Event();
            event.content = parcel.readString();
            event.action = parcel.readParcelable(getClass().getClassLoader());
            event.iconUrl = parcel.readString();
            return event;
        }

        @Override
        public Event[] newArray(int i) {
            return new Event[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeParcelable(action, 0);
        parcel.writeString(iconUrl);
    }

}
