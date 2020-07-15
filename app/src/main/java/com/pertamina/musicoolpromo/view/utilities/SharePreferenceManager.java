package com.pertamina.musicoolpromo.view.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ADDRESS;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.CITY;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.CITY_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.COMPLETE_PROFILE;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.DISTRICT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.DISTRICT_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.EMAIL;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.IMAGE;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.KTP;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.NAME;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.PASSWORD;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.PHONE_NUMBER;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.PROFILE;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.PROVINCE;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.PROVINCE_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.REG_TOKEN;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ZIPCODE;

public class SharePreferenceManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public static final String PREF_TOKEN = "com.satriakurniawandicoding.musicoolapp";

    public SharePreferenceManager(Context context) {
        gson = new Gson();
        pref = context.getSharedPreferences(PREF_TOKEN, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setToken(String token) {
        editor.putString(REG_TOKEN, token);
        editor.commit();
    }

    public void removeToken(){
        editor.remove(REG_TOKEN);
        editor.commit();
    }

    public void setAccount(String token) {
        editor.putString(ACCOUNT_TYPE, token);
        editor.commit();
    }

    public void removeAccount(){
        editor.remove(ACCOUNT_TYPE);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(REG_TOKEN, null);
    }

    public String getAccountType() {
        return pref.getString(ACCOUNT_TYPE, null);
    }


    public void setProfile(String data) {
        editor.putString(PROFILE, data);
        editor.commit();
    }

    public void removeProfile(){
        editor.remove(PROFILE);
        editor.commit();
    }

    public String getProfile() {
        return pref.getString(PROFILE, null);
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public void removeEmail(){
        editor.remove(EMAIL);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(EMAIL, null);
    }


    public void setName(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public void removeName(){
        editor.remove(NAME);
        editor.commit();
    }

    public String getName() {
        return pref.getString(NAME, null);
    }

    public void setPhone(String phone) {
        editor.putString(PHONE_NUMBER, phone);
        editor.commit();
    }

    public void removePhone(){
        editor.remove(PHONE_NUMBER);
        editor.commit();
    }

    public String getPhone() {
        return pref.getString(PHONE_NUMBER, null);
    }

    public void setProvince(String province) {
        editor.putString(PROVINCE, province);
        editor.commit();
    }

    public void removeProvince(){
        editor.remove(PROVINCE);
        editor.commit();
    }

    public String getProvince() {
        return pref.getString(PROVINCE, null);
    }

    public void setProvinceID(String provinceID) {
        editor.putString(PROVINCE_ID, provinceID);
        editor.commit();
    }

    public void removeProvinceID(){
        editor.remove(PROVINCE_ID);
        editor.commit();
    }

    public String getProvinceID() {
        return pref.getString(PROVINCE_ID, null);
    }

    public void setCityID(String cityID) {
        editor.putString(CITY_ID, cityID);
        editor.commit();
    }

    public void removeCityID(){
        editor.remove(CITY_ID);
        editor.commit();
    }

    public String getCityID() {
        return pref.getString(CITY_ID, null);
    }

    public void setDistrictID(String districtID) {
        editor.putString(DISTRICT_ID, districtID);
        editor.commit();
    }

    public void removeDistrictID(){
        editor.remove(DISTRICT_ID);
        editor.commit();
    }

    public String getDistrictID() {
        return pref.getString(DISTRICT_ID, null);
    }

    public void setCity(String city) {
        editor.putString(CITY, city);
        editor.commit();
    }

    public void removeCity(){
        editor.remove(CITY);
        editor.commit();
    }

    public String getCity() {
        return pref.getString(CITY, null);
    }

    public void setDistrict(String district) {
        editor.putString(DISTRICT, district);
        editor.commit();
    }

    public void removeDistrict(){
        editor.remove(DISTRICT);
        editor.commit();
    }

    public String getDistrict() {
        return pref.getString(DISTRICT, null);
    }

    public void setZipcode(String zipcode) {
        editor.putString(ZIPCODE, zipcode);
        editor.commit();
    }

    public void removeZipcode(){
        editor.remove(ZIPCODE);
        editor.commit();
    }

    public String getZipcode() {
        return pref.getString(ZIPCODE, null);
    }

    public void setAddress(String address) {
        editor.putString(ADDRESS, address);
        editor.commit();
    }

    public void removeAddress(){
        editor.remove(ADDRESS);
        editor.commit();
    }

    public String getAddress() {
        return pref.getString(ADDRESS, null);
    }

    public void setImage(String image) {
        editor.putString(IMAGE, image);
        editor.commit();
    }

    public void removeImage(){
        editor.remove(IMAGE);
        editor.commit();
    }

    public String getImage() {
        return pref.getString(IMAGE, null);
    }



    public void setAccountID(String id) {
        editor.putString(ACCOUNT_ID, id);
        editor.commit();
    }

    public void removeAccountID(){
        editor.remove(ACCOUNT_ID);
        editor.commit();
    }


    public String getAccountID() {
        return pref.getString(ACCOUNT_ID, null);
    }

    public void setPassword(String email) {
        editor.putString(PASSWORD, email);
        editor.commit();
    }

    public void removePassword(){
        editor.remove(PASSWORD);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(PASSWORD, null);
    }

    public void setCompleteProfile(String complete) {
        editor.putString(COMPLETE_PROFILE, complete);
        editor.commit();
    }

    public void removeComplete(){
        editor.remove(COMPLETE_PROFILE);
        editor.commit();
    }

    public String getComplete() {
        return pref.getString(COMPLETE_PROFILE, null);
    }

    public void setNumber(String number) {
        editor.putString(PHONE_NUMBER, number);
        editor.commit();
    }

    public void removeNumber(){
        editor.remove(PHONE_NUMBER);
        editor.commit();
    }

    public String getNumber() {
        return pref.getString(PHONE_NUMBER, null);
    }


    public void setKtp(String ktp) {
        editor.putString(KTP, ktp);
        editor.commit();
    }

    public void removeKtp(){
        editor.remove(KTP);
        editor.commit();
    }

    public String getKtp() {
        return pref.getString(KTP, null);
    }


}
