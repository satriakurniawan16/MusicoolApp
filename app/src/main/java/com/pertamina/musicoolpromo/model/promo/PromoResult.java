package com.pertamina.musicoolpromo.model.promo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoResult implements Parcelable {

    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String resume) {
        this.description = resume;
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
        dest.writeString(this.description);
        dest.writeString(this.created_at);
    }

    private PromoResult(Parcel in) {
        this.images = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.created_at = in.readString();
    }

    public static final Creator<PromoResult> CREATOR = new Creator<PromoResult>() {
        @Override
        public PromoResult createFromParcel(Parcel source) {
            return new PromoResult(source);
        }

        @Override
        public PromoResult[] newArray(int size) {
            return new PromoResult[size];
        }
    };
}