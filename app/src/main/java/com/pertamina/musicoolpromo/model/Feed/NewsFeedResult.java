package com.pertamina.musicoolpromo.model.Feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsFeedResult implements Parcelable {

    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("resume")
    @Expose
    private String resume;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.images);
        dest.writeString(this.title);
        dest.writeString(this.resume);
        dest.writeString(this.created_at);
    }

    private NewsFeedResult(Parcel in) {
        this.images = in.readString();
        this.title = in.readString();
        this.resume = in.readString();
        this.created_at = in.readString();
    }

    public static final Creator<NewsFeedResult> CREATOR = new Creator<NewsFeedResult>() {
        @Override
        public NewsFeedResult createFromParcel(Parcel source) {
            return new NewsFeedResult(source);
        }

        @Override
        public NewsFeedResult[] newArray(int size) {
            return new NewsFeedResult[size];
        }
    };
}