package com.taraba.gulfoilapp.view.splash_pop_up.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SplashPopUpDetails implements Parcelable {
    @SerializedName("campaign_id")
    private String campaignId;
    @SerializedName("title")
    private String title;
    @SerializedName("sub_title")
    private String subTitle;
    @SerializedName("description")
    private String description;
    @SerializedName("priority_level")
    private String priorityLevel;
    @SerializedName("popup_image")
    private String popupImage;
    public static final Creator<SplashPopUpDetails> CREATOR = new Creator<SplashPopUpDetails>() {
        @Override
        public SplashPopUpDetails createFromParcel(Parcel in) {
            return new SplashPopUpDetails(in);
        }

        @Override
        public SplashPopUpDetails[] newArray(int size) {
            return new SplashPopUpDetails[size];
        }
    };
    @SerializedName("tag")
    private String tag;
    @SerializedName("tag_label")
    private String tag_label;

    public SplashPopUpDetails(String title, String subTitle, String description, String popupImage, String tag,
                              String tag_label) {
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.popupImage = popupImage;
        this.tag = tag;
        this.tag_label = tag_label;
    }

    protected SplashPopUpDetails(Parcel in) {
        campaignId = in.readString();
        title = in.readString();
        subTitle = in.readString();
        description = in.readString();
        priorityLevel = in.readString();
        popupImage = in.readString();
        tag = in.readString();
        tag_label = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(campaignId);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(description);
        dest.writeString(priorityLevel);
        dest.writeString(popupImage);
        dest.writeString(tag);
        dest.writeString(tag_label);
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPopupImage() {
        return popupImage;
    }

    public void setPopupImage(String popupImage) {
        this.popupImage = popupImage;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag_label() {
        return tag_label;
    }

    public void setTag_label(String tag_label) {
        this.tag_label = tag_label;
    }

    @Override
    public String toString() {
        return "SplashPopUpDetails{" +
                "campaignId='" + campaignId + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", description='" + description + '\'' +
                ", priorityLevel='" + priorityLevel + '\'' +
                ", popupImage='" + popupImage + '\'' +
                ", tag='" + tag + '\'' +
                ", tag_label='" + tag_label + '\'' +
                '}';
    }
}
