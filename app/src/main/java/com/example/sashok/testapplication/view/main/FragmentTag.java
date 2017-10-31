package com.example.sashok.testapplication.view.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sashok on 26.9.17.
 */

public enum FragmentTag implements Parcelable {

    TAG_IMAGE_LIST("TAG_LIST_FOLDER"),
    TAG_MAP("TAG_MAP"),
    TAG_IMAGE_INFO("TAG_IMAGE_INFO");

    public static final Creator<FragmentTag> CREATOR = new Creator<FragmentTag>() {
        @Override
        public FragmentTag createFromParcel(Parcel in) {
            return FragmentTag.values()[in.readInt()];
        }

        @Override
        public FragmentTag[] newArray(int size) {
            return new FragmentTag[size];
        }
    };
    private String fragmentName;

    private FragmentTag(String name) {
        fragmentName = name;
    }

    public FragmentTag fromString(String tag) {
        return FragmentTag.valueOf(tag);
    }

    public String getFragmentName() {
        return this.fragmentName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
