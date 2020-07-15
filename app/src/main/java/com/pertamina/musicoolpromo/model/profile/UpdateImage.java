package com.pertamina.musicoolpromo.model.profile;

import com.google.gson.annotations.SerializedName;

public class UpdateImage {

    @SerializedName("id")
    private String id;
    @SerializedName("avatar_image")
    private String avatar_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }
}
