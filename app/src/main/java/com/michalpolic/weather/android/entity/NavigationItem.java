package com.michalpolic.weather.android.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Michal on 8.6.2015.
 */
public class NavigationItem {

    private String mText;
    private Drawable mIcon;


    public NavigationItem(String mText, Drawable mIcon) {
        this.mText = mText;
        this.mIcon = mIcon;
    }


    public Drawable getIcon() {
        return mIcon;
    }


    public String getText() {
        return mText;
    }

}
