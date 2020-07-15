package com.pertamina.musicoolpromo.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pertamina.musicoolpromo.model.network.NetworkData;
import com.pertamina.musicoolpromo.model.network.NetworkResult;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkViewModel extends ViewModel {

    private MutableLiveData<List<NetworkResult>> networkListData = new MutableLiveData<>();
    private MutableLiveData<List<NetworkResult>> listFilter = new MutableLiveData<>();

    private ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);

    public LiveData<List<NetworkResult>> getNetwork(final Context mContext, String city, String province) {
        final ArrayList<NetworkResult> listItemNetwork = new ArrayList<>();
        listFilter = new MutableLiveData<>();
        if (!city.equals("")) {
            for (int i = 0; i < Objects.requireNonNull(networkListData.getValue()).size(); i++) {
                if (networkListData.getValue().get(i).getKota_id().equals(city)) {
                    NetworkResult networkResult = new NetworkResult();
                    networkResult.setId(networkListData.getValue().get(i).getId());
                    networkResult.setAlamat(networkListData.getValue().get(i).getAlamat());
                    networkResult.setKota_id(networkListData.getValue().get(i).getKota_id());
                    networkResult.setNama(networkListData.getValue().get(i).getNama());
                    networkResult.setNo_telp(networkListData.getValue().get(i).getNo_telp());
                    Log.d("lolgoblin", "onResponse: " + networkResult.toString());
                    listItemNetwork.add(networkResult);
                }
            }
            listFilter.setValue(listItemNetwork);
        } else if (!province.equals("")) {
            listFilter = new MutableLiveData<>();
            for (int i = 0; i < Objects.requireNonNull(networkListData.getValue()).size(); i++) {
                if (networkListData.getValue().get(i).getProvinsi_id().equals(province)) {
                    Log.d("provinsi", "getNetwork: " + province +" "+networkListData.getValue().get(i).getProvinsi_id());
                    NetworkResult networkResult = new NetworkResult();
                    networkResult.setId(networkListData.getValue().get(i).getId());
                    networkResult.setAlamat(networkListData.getValue().get(i).getAlamat());
                    networkResult.setKota_id(networkListData.getValue().get(i).getKota_id());
                    networkResult.setKota_id(networkListData.getValue().get(i).getProvinsi_id());
                    networkResult.setNama(networkListData.getValue().get(i).getNama());
                    networkResult.setNo_telp(networkListData.getValue().get(i).getNo_telp());
                    Log.d("lolgoblin", "onResponse: " + networkResult.toString());
                    listItemNetwork.add(networkResult);
                }
            }
            listFilter.setValue(listItemNetwork);
        } else {
            Call<NetworkData> call = service.getNetwork();
            call.enqueue(new Callback<NetworkData>() {
                @Override
                public void onResponse(@NonNull Call<NetworkData> call, @NonNull Response<NetworkData> response) {
                    if (response.body() != null) {
                        Log.d("njengtolo", "onResponse: " + response.body().getNetworkResults().toString());
                        if (city.equals("")) {
                            networkListData.setValue(response.body().getNetworkResults());
                        } else {
                            for (int i = 0; i < response.body().getNetworkResults().size(); i++) {
                                NetworkResult networkResult = new NetworkResult();
                                networkResult.setId(response.body().getNetworkResults().get(i).getId());
                                networkResult.setAlamat(response.body().getNetworkResults().get(i).getAlamat());
                                networkResult.setKota_id(response.body().getNetworkResults().get(i).getKota_id());
                                networkResult.setNama(response.body().getNetworkResults().get(i).getNama());
                                networkResult.setNo_telp(response.body().getNetworkResults().get(i).getNo_telp());
                                Log.d("lolgoblin", "onResponse: " + networkResult.toString());
                                listItemNetwork.add(networkResult);
                            }
                            networkListData.setValue(listItemNetwork);
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<NetworkData> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
                }
            });
        }
        if (!city.equals("") || !province.equals("")) {
            return listFilter;
        } else {
            return networkListData;
        }
    }
}

