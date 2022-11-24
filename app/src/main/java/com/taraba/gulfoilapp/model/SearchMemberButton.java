package com.taraba.gulfoilapp.model;

/**
 * Created by SHINSAN-CONT on 6/9/2017.
 */

public class SearchMemberButton {
    private String name;
    private int backColor;

    public SearchMemberButton() {
    }

    public SearchMemberButton(String name, int backColor) {
        this.name = name;
        this.backColor = backColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    @Override
    public String toString() {
        return "SearchMemberButton{" +
                "name='" + name + '\'' +
                ", backColor=" + backColor +
                '}';
    }
}
