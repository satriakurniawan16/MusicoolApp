package com.pertamina.musicoolpromo.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pertamina.musicoolpromo.model.Feed.NewsFeedData;
import com.pertamina.musicoolpromo.model.Feed.NewsFeedResult;
import com.pertamina.musicoolpromo.model.promo.PromoData;
import com.pertamina.musicoolpromo.model.promo.PromoResult;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedViewModel extends ViewModel {

    private MutableLiveData<List<NewsFeedResult>> newsResultData = new MutableLiveData<>();
    private MutableLiveData<List<PromoResult>> promoResultData = new MutableLiveData<>();

    private ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);

    public LiveData<List<NewsFeedResult>> getNewsFeed(final Context mContext){
        Call<NewsFeedData> call = service.getNewsFeed();
        call.enqueue(new Callback<NewsFeedData>() {
            @Override
            public void onResponse(@NonNull Call<NewsFeedData> call, @NonNull Response<NewsFeedData> response) {
                if(response.body() != null){
                    Log.d("njeng", "onResponse: " + response.body().getNewsResults());
                    newsResultData.setValue(response.body().getNewsResults());
                }
            }
            @Override
            public void onFailure(@NonNull Call<NewsFeedData> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
            }
        });
        return newsResultData;
    }

    public LiveData<List<PromoResult>> getPromo(final Context mContext){
        Call<PromoData> call = service.getPromo();
        call.enqueue(new Callback<PromoData>() {
            @Override
            public void onResponse(@NonNull Call<PromoData> call, @NonNull Response<PromoData> response) {
                if(response.body() != null){
                    Log.d("njengtolol", "onResponse: " + response.body().getPromoResults());
                    promoResultData.setValue(response.body().getPromoResults());
                }else {
                    Log.d("njengtolol", "onResponse: " );
                }
            }
            @Override
            public void onFailure(@NonNull Call<PromoData> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
                Log.d("njengtolol", "onResponse: " + t);
            }
        });
        return promoResultData;
    }

}
