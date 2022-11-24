package com.taraba.gulfoilapp.drawerinterface;

import com.taraba.gulfoilapp.Child;

import java.util.ArrayList;

/**
 * Created by Mayuri on 2/15/16.
 */
public class NavigationItem {
    private String mText;
    private int mDrawable;

    private ArrayList<Child> Items;

    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> items) {
        Items = items;
    }

    public NavigationItem() {
    }

    public NavigationItem(String text, int drawable) {
        this.mText = text;
        this.mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getImage() {
        return mDrawable;
    }

    public void setImage(int drawable) {
        this.mDrawable = drawable;
    }
}
