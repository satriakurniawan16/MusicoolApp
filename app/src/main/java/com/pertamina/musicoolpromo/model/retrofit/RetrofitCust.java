package com.pertamina.musicoolpromo.model.retrofit;

import com.google.gson.annotations.SerializedName;

public class RetrofitCust {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("city_id")
    private String city_id;
    @SerializedName("province_id")
    private String province_id;
    @SerializedName("district_id")
    private String district_id;
    @SerializedName("zipcode")
    private String zipcode;
    @SerializedName("address")
    private String address;
    @SerializedName("idcard_image")
    private String idcard_image;

    public String getName() {
        return name;
    }

    public String getIdcard_image() {
        return idcard_image;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setIdcard_image(String idcard_image) {
        this.idcard_image = idcard_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
