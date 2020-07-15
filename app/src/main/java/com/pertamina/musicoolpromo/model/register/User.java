package com.pertamina.musicoolpromo.model.register;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("repeat_password")
    private String repeat_password;
    @SerializedName("account_type")
    private String account_type;
    @SerializedName("validation_image")
    private String validation_image;
    @SerializedName("avatar_image")
    private String avatar_image;
    @SerializedName("details")
    private UserDetail details;

    public UserDetail getDetails() {
        return details;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public void setDetails(UserDetail details) {
        this.details = details;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeat_password() {
        return repeat_password;
    }

    public void setRepeat_password(String repeat_password) {
        this.repeat_password = repeat_password;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getValidation_image() {
        return validation_image;
    }

    public void setValidation_image(String validation_image) {
        this.validation_image = validation_image;
    }


}
