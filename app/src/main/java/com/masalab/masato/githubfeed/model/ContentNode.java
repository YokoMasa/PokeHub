package com.masalab.masato.githubfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Masato on 2018/02/25.
 */

public class ContentNode extends BaseModel {

    public String type;
    public String url;
    public String name;
    public ContentNode parent;
    private List<ContentNode> childNodes;
    public static Parcelable.Creator<ContentNode> CREATOR = new Parcelable.Creator<ContentNode>() {
        @Override
        public ContentNode createFromParcel(Parcel parcel) {
            ContentNode contentNode = new ContentNode();
            contentNode.type = parcel.readString();
            contentNode.url = parcel.readString();
            contentNode.name = parcel.readString();
            contentNode.parent = parcel.readParcelable(getClass().getClassLoader());
            return contentNode;
        }

        @Override
        public ContentNode[] newArray(int i) {
            return new ContentNode[i];
        }
    };

    @Override
    public int compareTo(@NonNull BaseModel baseModel) {
        if (baseModel instanceof ContentNode) {
            ContentNode other = (ContentNode) baseModel;
            if (this.type.equals("dir")) {
                if (other.type.equals("dir")) {
                    return this.name.compareTo(other.name);
                } else {
                    return -1;
                }
            } else {
                return this.name.compareTo(other.name);
            }
        } else {
            return 0;
        }
    }

    public List<ContentNode> getChildNodes() {
        return this.childNodes;
    }

    public void setChildNodes(List<ContentNode> childNodes) {
        for (ContentNode contentNode : childNodes) {
            contentNode.parent = this;
        }
        this.childNodes = childNodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(url);
        parcel.writeString(name);
        parcel.writeParcelable(parent, 0);
    }

}
