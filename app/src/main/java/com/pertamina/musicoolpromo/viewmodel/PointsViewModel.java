package com.pertamina.musicoolpromo.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pertamina.musicoolpromo.model.reward.RewardData;
import com.pertamina.musicoolpromo.model.reward.RewardResult;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointsViewModel extends ViewModel{

    private MutableLiveData<List<RewardResult>> rewardListData = new MutableLiveData<>();

    private ApiInterface service = ApiClient.getData().create(ApiInterface.class);

    public LiveData<List<RewardResult>> getReward(final Context mContext,String token) {
        Call<RewardData> call = service.getRewardList("Bearer "+token);
        call.enqueue(new Callback<RewardData>() {
            @Override
            public void onResponse(@NonNull Call<RewardData> call, @NonNull Response<RewardData> response) {
                Log.d("ganteng", "onResponse: " + response.code() +response.message());
                if(response.code() == 200){
                    if (response.body() != null) {
                        Log.d("ganteng", "onResponse: " + response.body().getRewardResults().toString());
                        rewardListData.setValue(response.body().getRewardResults());
                    } else if(response.code()==401){
                        Toast.makeText(mContext, "Sesi anda te", Toast.LENGTH_SHORT).show();
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RewardData> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "njengtolo", Toast.LENGTH_LONG).show();
            }
        });
        return rewardListData;
    }
}