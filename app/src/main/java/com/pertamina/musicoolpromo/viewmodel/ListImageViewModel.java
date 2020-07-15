package com.pertamina.musicoolpromo.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pertamina.musicoolpromo.model.image.ImageData;
import com.pertamina.musicoolpromo.model.image.ImageList;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListImageViewModel extends ViewModel {

    private MutableLiveData<List<ImageList>> imageListData = new MutableLiveData<>();

    private ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);

    public LiveData<List<ImageList>> getProductList (final Context mContext){
        Call<ImageData> call = service.getProductList();
        call.enqueue(new Callback<ImageData>() {
            @Override
            public void onResponse(@NonNull Call<ImageData> call, @NonNull Response<ImageData> response) {
                if(response.body() != null){
                    Log.d("njengtolo", "onResponse: " + response.body().getImageLists().toString());
                    imageListData.setValue(response.body().getImageLists());
                }
            }
            @Override
            public void onFailure(@NonNull Call<ImageData> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
            }
        });
        return imageListData;
    }

}
