package com.taraba.gulfoilapp.model;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */

public class DashboardMenu {
    private String lebal;
    private int color;
    private int image;

    public DashboardMenu(int image, String lebal, int color) {
        this.image = image;
        this.lebal = lebal;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLebal() {
        return lebal;
    }

    public void setLebal(String lebal) {
        this.lebal = lebal;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "DashboardMenu{" +
                "lebal='" + lebal + '\'' +
                ", color=" + color +
                ", image=" + image +
                '}';
    }

}
