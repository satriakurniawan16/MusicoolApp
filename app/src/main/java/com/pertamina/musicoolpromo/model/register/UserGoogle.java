package com.pertamina.musicoolpromo.model.register;

public class UserGoogle {


    private String phoneNumber;
    private String iduser;
    private String name;
    private String gender;
    private String address;
    private String province;
    private String city;
    private String district;
    private String image;
    private String kodepos;
    private String imageProfile;
    private String email;
    private String completeProfile;
    private String provinceID;
    private String districtID;
    private String cityID;

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    private String ktp;

    public UserGoogle() {
    }

    public UserGoogle(String phoneNumber, String name, String gender, String address, String province, String city, String district, String image, String kodepos, String imageProfile, String email, String completeProfile) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.province = province;
        this.city = city;
        this.district = district;
        this.image = image;
        this.kodepos = kodepos;
        this.imageProfile = imageProfile;
        this.email = email;
        this.completeProfile = completeProfile;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getCompleteProfile() {
        return completeProfile;
    }

    public void setCompleteProfile(String completeProfile) {
        this.completeProfile = completeProfile;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKodepos() {
        return kodepos;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
