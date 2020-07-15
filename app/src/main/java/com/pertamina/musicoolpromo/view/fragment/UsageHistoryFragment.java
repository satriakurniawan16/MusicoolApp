package com.pertamina.musicoolpromo.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.HistoryPoints;
import com.pertamina.musicoolpromo.view.adapter.history.BottomItem;
import com.pertamina.musicoolpromo.view.adapter.history.HeaderItem;
import com.pertamina.musicoolpromo.view.adapter.history.HistoryItem;
import com.pertamina.musicoolpromo.view.adapter.history.HistoryUsageAdapter;
import com.pertamina.musicoolpromo.view.adapter.history.ListItem;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsageHistoryFragment extends BaseFragment {

    @NonNull
    private List<ListItem> items = new ArrayList<>();
    private HistoryUsageAdapter historyAdapter;
    private ProgressBar progressBar;
    private LinearLayout emptyView;

    public UsageHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usage_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
        generateView(view);
    }

    @Override
    public void setUpView(View view) {

        emptyView= view.findViewById(R.id.empty_view);
        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_historypoints);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryUsageAdapter(items);
        historyAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(historyAdapter);

    }

    @Override
    public void generateView(View view) {
        loadEvents();
    }

    @Override
    public void setupListener(View view) {

    }

    private void loadEvents() {
        final List<HistoryPoints> eventItem = new ArrayList<>();
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.getRedeemPoint("Bearer "+sharePreferenceManager.getToken(),sharePreferenceManager.getAccountID(),"1","100");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                if(jsonArray.size() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                }
                Log.d("datareceive", "onResponse:  "+ sharePreferenceManager.getAccountID()+ response.body());
                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        JsonObject data = jsonArray.get(i).getAsJsonObject();
                        JsonObject name = data.get("reward").getAsJsonObject();
                        String dateString = data.get("createdAt").getAsString();
                        String nameString = name.get("name").getAsString();
                        int poin = name.get("point_cost").getAsInt();
                        String statusString = data.get("status").getAsString();
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        final Date date = inputFormat.parse(dateString.substring(0,10));
                        eventItem.add(new HistoryPoints(nameString,poin,date,statusString));
                    }catch (Exception e){
                        Log.d("dataanyingerrrrrrrrrr", "onResponse: " + e);

                    }
                }
                Map<Date, List<HistoryPoints>> events = toMap(eventItem);

                showLoading(false);

                int count = 0;
                int i = 0;
                for (Date date : events.keySet()) {
                    HeaderItem header = new HeaderItem(date);
                    items.add(header);
                    for (HistoryPoints event : events.get(date)) {
                        HistoryItem item = new HistoryItem(event);
                        items.add(item);
                        count+= item.getHistoryPoints().getTotal();
                    }
                    i++;
                    Log.d("8"+i, "onResponse: " + count);
                    BottomItem bottom = new BottomItem(String.valueOf(count));
                    count = 0 ;
                    items.add(bottom);

                    historyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);

            }
        });
    }

    @NonNull
    private Map<Date, List<HistoryPoints>> toMap(@NonNull List<HistoryPoints> events) {
        Map<Date, List<HistoryPoints>> map = new TreeMap<>(Collections.reverseOrder());
        for (HistoryPoints event : events) {
            List<HistoryPoints> value = map.get(event.getDate());
            if (value == null) {
                value = new ArrayList<>();
                map.put(event.getDate(), value);
            }
            value.add(event);
        }
        return map;
    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
