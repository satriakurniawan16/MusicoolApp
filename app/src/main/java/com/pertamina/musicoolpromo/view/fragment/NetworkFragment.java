package com.pertamina.musicoolpromo.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.network.NetworkResult;
import com.pertamina.musicoolpromo.view.adapter.NetworkAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.viewmodel.NetworkViewModel;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkFragment extends BaseFragment {

    private NetworkAdapter networkAdapter;
    private List<NetworkResult> networkList;
    private BetterSpinner provinceBetterSpinner;
//    private BetterSpinner districtBetterSpinner;
    private BetterSpinner cityBetterSpinner;

    private LinearLayout emptyView;
    private Button filterSearch;

    private ProgressBar progressBar;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;

    private ArrayList<String> province;
    private ArrayList<String> provinceID;
    private ArrayList<String> city;
    private ArrayList<String> cityID;

    private String provinceIDString = "";
    private String cityIDString = "";
    private String districtIDString;

    public NetworkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
        generateView(view);
        setupListener(view);
    }

    @Override
    public void setUpView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.rv_network);

        progressBar = view.findViewById(R.id.progressBar);
        emptyView = view.findViewById(R.id.empty_view);
        filterSearch = view.findViewById(R.id.filter_search);

        provinceBetterSpinner = view.findViewById(R.id.province_spinner);
//        districtBetterSpinner = view.findViewById(R.id.district_spinner);
        cityBetterSpinner = view.findViewById(R.id.city_spinner);

        provinceBetterSpinner.setEnabled(false);
        cityBetterSpinner.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        networkList = new ArrayList<>();

        networkAdapter = new NetworkAdapter(networkList,emptyView);
        networkAdapter.notifyDataSetChanged();

        networkAdapter.setOnItemClickCallback(new NetworkAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(NetworkResult data) {
            }
        });

        recyclerView.setAdapter(networkAdapter);

        getProvinces();
    }

    @Override
    public void generateView(View view) {
    }

    @Override
    public void setupListener(View view) {

        cityBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.size() == 0) {
                    Toast.makeText(getContext(), "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        filterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetworkList();
            }
        });

        provinceBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                districtBetterSpinner.setText("");
                cityBetterSpinner.setText("");
                provinceIDString = provinceID.get(i);
                cityIDString = "";
                getCities(provinceID.get(i));
            }
        });

        cityBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityIDString = cityID.get(i);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getNetworkList();
    }

    private void getNetworkList(){
        Log.d("disini", "getImage: ");
        NetworkViewModel dataViewModel = ViewModelProviders.of(this).get(NetworkViewModel.class);
        dataViewModel.getNetwork(Objects.requireNonNull(getContext()),cityIDString,provinceIDString).observe(this, new Observer<List<NetworkResult>>() {
            @Override
            public void onChanged(List<NetworkResult> movieResults) {
                networkList.clear();
                Log.d("lol", movieResults.toString());
                networkList.addAll(movieResults);
                networkAdapter.notifyDataSetChanged();
                networkAdapter.updateEmptyView();
                showLoading(false);
                provinceBetterSpinner.setEnabled(true);
            }
        });
    }

    private void getProvinces() {

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getProvinces();

        province = new ArrayList<>();
        provinceID = new ArrayList<>();
        city = new ArrayList<>();
        cityID = new ArrayList<>();

        provinceBetterSpinner.setHint("Mengambil data");
        provinceBetterSpinner.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("name").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result + "id = " + resultID);
                    province.add(result);
                    provinceID.add(resultID);
                }
                provinceBetterSpinner.setHint("Pilih Provinsi");
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    private void getCities(String id) {
        Log.d("lol", "getCities: " + id);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getCities(id, "1", "0");

        city = new ArrayList<>();
        cityID = new ArrayList<>();

        cityBetterSpinner.setHint("Mengambil data");
        cityBetterSpinner.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.d("datacity", "onResponse: " + response.body());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("city").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                    city.add(result);
                    cityID.add(resultID);
                }
                cityBetterSpinner.setHint("Pilih Kota/Kabupaten");
                cityBetterSpinner.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    private void getDistricts(String id) {
        Log.d("lol", "getDistricts: " + id);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getDistricts(id);

//        districtBetterSpinner.setHint("Mengambil data");
//        districtBetterSpinner.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.d("datacity", "onResponse: " + response.body());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("sub_district").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                }
//                districtBetterSpinner.setHint("Pilih Kecamatan");
//                districtBetterSpinner.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    private void setSpinnerAdapter() {
        provinceAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, province);

        provinceBetterSpinner.setAdapter(provinceAdapter);

        cityAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, city);

        cityBetterSpinner.setAdapter(cityAdapter);

    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
