package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.reward.RewardResult;
import com.pertamina.musicoolpromo.view.adapter.PointsAdapter;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
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

public class RedeemPointActivity extends BaseActivity {

    private PointsAdapter pointAdapter;
    private List<RewardResult> rewardResultList;

    private LinearLayout klik;
    private LinearLayout emptyView;

    private TextView point_text;

    private Toolbar toolbar;

    private ProgressBar progressBar;

    private SharePreferenceManager sharePreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_point);
        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void setUpView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Hadiah");
        toolbar.setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        klik = findViewById(R.id.to_history);

        point_text = findViewById(R.id.my_point);

        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.empty_view);

        sharePreferenceManager = new SharePreferenceManager(this);

        rewardResultList = new ArrayList<>();
        pointAdapter = new PointsAdapter(rewardResultList, emptyView);
        pointAdapter.notifyDataSetChanged();

        RecyclerView recyclerView = findViewById(R.id.rv_points);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        pointAdapter.setOnItemClickCallback(new PointsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(RewardResult data) {
                parsingDataToDetail(data);
            }
        });

        recyclerView.setAdapter(pointAdapter);
    }

    @Override
    public void generateView() {
        getPoint(sharePreferenceManager.getToken());
    }

    @Override
    public void setupListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemPointActivity.this, PointsHistoryActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getRewardList();
    }

    private void getRewardList() {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        Log.d("disini", "getImage: ");
        PointsViewModel dataViewModel = ViewModelProviders.of(this).get(PointsViewModel.class);
        dataViewModel.getReward(Objects.requireNonNull(this), sharePreferenceManager.getToken()).observe(this, new Observer<List<RewardResult>>() {
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
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    private void parsingDataToDetail(RewardResult reward){
        Intent intent = new Intent(RedeemPointActivity.this, RedeemDetailActivity.class);
        intent.putExtra(RedeemDetailActivity.EXTRA_DATA_REDEEM, reward);
        startActivity(intent);
    }
}
