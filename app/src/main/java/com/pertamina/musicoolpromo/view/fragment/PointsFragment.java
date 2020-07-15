package com.pertamina.musicoolpromo.view.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.reward.RewardResult;
import com.pertamina.musicoolpromo.view.activity.PointsHistoryActivity;
import com.pertamina.musicoolpromo.view.activity.RedeemDetailActivity;
import com.pertamina.musicoolpromo.view.adapter.PointsAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;
import com.pertamina.musicoolpromo.viewmodel.PointsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PointsFragment extends BaseFragment {

    private PointsAdapter pointAdapter;
    private List<RewardResult> rewardResultList;

    private LinearLayout klik;
    private LinearLayout emptyView;

    private TextView point_text;

    private ProgressBar progressBar;

    private SharePreferenceManager sharePreferenceManager;

    public PointsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpView(view);
        setupListener(view);
        generateView(view);

    }

    @Override
    public void setUpView(View view) {

        rewardResultList = new ArrayList<>();
        emptyView = view.findViewById(R.id.empty_view);
        pointAdapter = new PointsAdapter(rewardResultList,emptyView);
        pointAdapter.notifyDataSetChanged();

        sharePreferenceManager = new SharePreferenceManager(Objects.requireNonNull(getContext()));

        klik = view.findViewById(R.id.to_history);

        progressBar = view.findViewById(R.id.progressBar);

        point_text = view.findViewById(R.id.my_point);

        RecyclerView recyclerView = view.findViewById(R.id.rv_points);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pointAdapter);
    }

    @Override
    public void generateView(View view) {
        getPoint(sharePreferenceManager.getToken());
    }

    @Override
    public void setupListener(View view) {
        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PointsHistoryActivity.class);
                startActivity(intent);
            }
        });

        pointAdapter.setOnItemClickCallback(new PointsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(RewardResult data) {
                parsingDataToDetail(data);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getRewardList();
    }

    private void getRewardList(){
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        Log.d("disini", "getImage: ");
        PointsViewModel dataViewModel = ViewModelProviders.of(PointsFragment.this).get(PointsViewModel.class);
        dataViewModel.getReward(Objects.requireNonNull(getContext()),sharePreferenceManager.getToken()).observe(this, new Observer<List<RewardResult>>() {
            @Override
            public void onChanged(List<RewardResult> rewardResults) {
                Log.d("gantengtolol", rewardResults.toString());
                rewardResultList.clear();
                rewardResultList.addAll(rewardResults);
                pointAdapter.notifyDataSetChanged();
                pointAdapter.updateEmptyView();
                showLoading(false);
            }
        });
    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void getPoint(String token) {
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        Call<JsonObject> call = service.checkAccount("Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    String point = jsonObject.get("point").getAsString();
                    point_text.setText(point);
                } catch (Exception e) {
                    Log.d("PANTEK", "onResponse: " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    private void parsingDataToDetail(RewardResult reward){
        Intent intent = new Intent(getContext(), RedeemDetailActivity.class);
        intent.putExtra(RedeemDetailActivity.EXTRA_DATA_REDEEM, reward);
        startActivity(intent);
    }

}
