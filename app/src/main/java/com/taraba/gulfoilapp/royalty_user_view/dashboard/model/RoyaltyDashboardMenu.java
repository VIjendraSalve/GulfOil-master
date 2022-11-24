package com.taraba.gulfoilapp.royalty_user_view.dashboard.model;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */

public class RoyaltyDashboardMenu {
    private String lebal;
    private int image;

    public RoyaltyDashboardMenu(int image, String lebal) {
        this.image = image;
        this.lebal = lebal;
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
                ", image=" + image +
                '}';
    }

}
