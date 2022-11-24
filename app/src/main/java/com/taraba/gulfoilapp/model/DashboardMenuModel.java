package com.taraba.gulfoilapp.model;

public class DashboardMenuModel {
    private int iconRes;
    private String menuName;
    private String menuTag;

    public DashboardMenuModel(int iconRes, String menuName, String menuTag) {
        this.iconRes = iconRes;
        this.menuName = menuName;
        this.menuTag = menuTag;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuTag() {
        return menuTag;
    }

    public void setMenuTag(String menuTag) {
        this.menuTag = menuTag;
    }
}
