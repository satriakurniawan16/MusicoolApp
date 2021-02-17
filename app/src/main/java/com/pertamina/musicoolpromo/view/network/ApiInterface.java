package com.pertamina.musicoolpromo.view.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.model.Feed.NewsFeedData;
import com.pertamina.musicoolpromo.model.image.ImageData;
import com.pertamina.musicoolpromo.model.forgotpass.ForgotPass;
import com.pertamina.musicoolpromo.model.login.Login;
import com.pertamina.musicoolpromo.model.network.NetworkData;
import com.pertamina.musicoolpromo.model.profile.UpdateImage;
import com.pertamina.musicoolpromo.model.register.User;
import com.pertamina.musicoolpromo.model.promo.PromoData;
import com.pertamina.musicoolpromo.model.retrofit.RetrofitCust;
import com.pertamina.musicoolpromo.model.retrofit.RetrofitUnit;
import com.pertamina.musicoolpromo.model.reward.RewardData;
import com.pertamina.musicoolpromo.model.reward.RewardRedeem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("master/provinces")
    Call<JsonArray> getProvinces();

    @GET("master/provinces/{provinceId}/cities")
    Call<JsonArray> getCities
            (@Path("provinceId") String id,
             @Query("page") String page,
             @Query("page_size") String pageSize);

    @GET("master/cities/{cityId}/sub-districts")
    Call<JsonArray> getDistricts
            (@Path("cityId") String id);

    @GET("api/banners")
    Call<JsonObject> getBanner();

    @GET("api/newsfeed")
    Call<NewsFeedData> getNewsFeed();

    @GET("api/eventpromo")
    Call<PromoData> getPromo();

    @GET("api/product")
    Call<ImageData> getProductList();

    @GET("api/network")
    Call<NetworkData> getNetwork();

    @GET("api/network")
    Call<JsonObject> getTechnician();

    @Multipart
    @POST("master/upload-image")
    Call<JsonObject> uploadImage
            (@Part MultipartBody.Part photo);

    @POST("account/register")
    Call<JsonObject> registerAccount(
            @Header("Content-Type") String language,
            @Body User body);

    @POST("account/login")
    Call<String> loginAccount(
            @Header("Content-Type") String header,
            @Body Login body);

    @POST("account/login")
    Call<String> newToken(
            @Header("Content-Type") String header,
            @Body Login body);

    @POST("account/recover")
    Call<JsonObject> forgotPassword(
            @Header("Content-Type") String header,
            @Body ForgotPass body);

    @GET("account/me")
    Call<JsonObject> checkAccount(
            @Header("authorization") String token);

    @GET("product/check/{code}")
    Call<JsonObject> checkCode(
            @Header("authorization") String token,
            @Path("code") String code);

    @GET("retrofit/unit/{code}")
    Call<JsonObject> checkRetrofit(
            @Header("authorization") String token,
            @Path("code") String code);

    @GET("breezon/retrofit/{code}")
    Call<JsonObject> checkRetrofitBreezon(
            @Header("authorization") String token,
            @Path("code") String code);

    @POST("retrofit/customer/")
    Call<JsonObject> custRetrofit(
            @Header("Content-Type") String header,
            @Header("authorization") String token,
            @Body RetrofitCust retrofit
    );

    @POST("breezon/retrofit-customer/")
    Call<JsonObject> custRetrofitBreezon(
            @Header("Content-Type") String header,
            @Header("authorization") String token,
            @Body RetrofitCust retrofit
    );

    @POST("retrofit/unit/submit")
    Call<JsonObject> setUnitRetrofit(
            @Header("Content-Type") String header,
            @Header("Authorization") String token,
            @Query("page") String page,
            @Query("page_size") String pageSize,
            @Body RetrofitUnit retrofit
    );

    @POST("breezon/retrofit/submit")
    Call<JsonObject> setUnitRetrofitBreezon(
            @Header("Content-Type") String header,
            @Header("Authorization") String token,
            @Query("page") String page,
            @Query("page_size") String pageSize,
            @Body RetrofitUnit retrofit
    );

    @GET("product/item/info/{code}")
    Call<JsonArray> getProduct(
            @Header("authorization") String token,
            @Header("content-type") String content,
            @Path("code") String code
    );

    @GET("breezon/item/info/{code}")
    Call<JsonArray> getProductBreezon(
            @Header("authorization") String token,
            @Header("content-type") String content,
            @Path("code") String code
    );

    @GET("reward/all/restrict")
    Call<RewardData> getRewardList(
            @Header("Authorization") String token
    );

    @GET("reward/{id}")
    Call<JsonObject> getRewardID(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @GET("reward/{id}")
    Call<JsonObject> getHistory(
            @Header("Authorization") String token
    );

    @GET("account/{id}/history/point")
    Call<JsonObject> getRewardPoint(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Query("page") String page,
            @Query("page_size") String pageSize,
            @Query("order") String order
    );

    @GET("reward/redeem/all/by-requester/{account}")
    Call<JsonObject> getRedeemPoint(
            @Header("Authorization") String token,
            @Path("account") String account,
            @Query("page") String page,
            @Query("page_size") String pageSize
    );

    @PUT("product/item/{code}/scan")
    Call<JsonObject> setProduct (
            @Header("Authorization") String token,
            @Header("Content-Type") String content,
            @Path("code") String code
    );


    @PUT("breezon/item/{code}/scan")
    Call<JsonObject> setProductBreezon (
            @Header("Authorization") String token,
            @Header("Content-Type") String content,
            @Path("code") String code
    );

    @POST("reward/redeem/")
    Call<JsonObject> setReedem(
            @Header("Content-Type") String header,
            @Header("Authorization") String token,
            @Body RewardRedeem rewardRedeem
    );

    @PATCH("account/image")
    Call<JsonObject> updateImage(
            @Header("Content-Type") String header,
            @Header("Authorization") String token,
            @Body UpdateImage updateImage
    );

    @Multipart
    @POST("api/pesanan")
    Call<ResponseBody> setOrder(
       @Part("nama") RequestBody name,
       @Part("email") RequestBody email,
       @Part("no_hp") RequestBody no_hp,
       @Part("alamat") RequestBody alamat,
       @Part("provinsi") RequestBody provinsi,
       @Part("provinsi_name") RequestBody provinsi_name,
       @Part("kota") RequestBody kota,
       @Part("kota_name") RequestBody kota_name,
       @Part("kecamatan") RequestBody kecamatan,
       @Part("jumlah_ac") RequestBody jumlah_ac,
       @Part("merk_ac") RequestBody merk_ac,
       @Part("ket_merk_ac") RequestBody ket_merk_ac,
       @Part("pk_ac") RequestBody pk_ac,
       @Part("ket_pk_ac") RequestBody ket_pk_ac,
       @Part("service_ac") RequestBody service_ac,
       @Part("keterangan") RequestBody keterangan,
       @Part("ktp") RequestBody ktp,
       @Part("kode_voucher") RequestBody voucher,
       @Part("source") RequestBody source
    );



}