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
import com.pertamina.musicoolpromo.view.adapter.history.HistoryAdapter;
import com.pertamina.musicoolpromo.view.adapter.history.HistoryItem;
import com.pertamina.musicoolpromo.view.adapter.history.ListItem;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.DateUtils;
import com.pertamina.musicoolpromo.view.utilities.Reversed;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveHistoryFragment extends BaseFragment {


    @NonNull
    private List<ListItem> items = new ArrayList<>();
    private List<ListItem> itemsClone = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private ProgressBar progressBar;
    private LinearLayout emptyView;
    private Reversed reversed;

    public ReceiveHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receive_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
        generateView(view);
    }

    @Override
    public void setUpView(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        emptyView= view.findViewById(R.id.empty_view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_historypoints);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(items);
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
        Log.d("dateanting", "loadEvents: disni ");
        final List<HistoryPoints> eventItem = new ArrayList<>();
        Log.d("kalender panteek", "loadEvents: " + buildRandomDateInCurrentMonth().toString());
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.getRewardPoint("Bearer "+sharePreferenceManager.getToken(),sharePreferenceManager.getAccountID(),"1","100","transaction_type:ASC");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("dateanying", "onResponse:  "+ sharePreferenceManager.getAccountID()+ response.body());
                Log.d("dateanying", "onResponse:  "+ sharePreferenceManager.getAccountID()+ response.code());

                if(response.code() == 200 ){
                    JsonObject jsonObject = response.body();
                    JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                    if(jsonArray.size() == 0){
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    for (int i = jsonArray.size() ; i >= 0 ; i--) {
                        try {
                            JsonObject data = jsonArray.get(i).getAsJsonObject();
                            String nameString = data.get("description").getAsString();
                            String dateString = data.get("createdAt").getAsString();
                            String statusString = data.get("transaction_type").getAsString();
                            Log.d("dateanying", "onResponse: " + dateString.substring(0,10));
                            int amount = data.get("amount").getAsInt();
                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String s = "2020-12-12";
                            final Date date = inputFormat.parse(dateString.substring(0,10));
                            Log.d("dateanying", "onResponse: " + amount + dateString);
                            if(!statusString.equals("credit")){
                                eventItem.add(new HistoryPoints(nameString,amount,date,statusString));
                            }
//                            Collections.reverse();
                            Log.d("dateanying", "onResponse: Sampesini" );
                        }catch (Exception e){
                            Log.d("dateanying", "onResponse: " + e);

                        }
                    }

                    Map<Date, List<HistoryPoints>> events = toMap(eventItem);

                    showLoading(false);
                    int count = 0;
                    int i = 0;
                    for (Date date : events.keySet()) {
                        Log.d("tolol", "onResponse: " + events.keySet());
                        HeaderItem header = new HeaderItem(date);
                        items.add(header);
                        for (HistoryPoints event : events.get(date)) {
                            HistoryItem item = new HistoryItem(event);
                            items.add(item);
                            count+= item.getHistoryPoints().getTotal();
                        }
                        i++;
                        Log.d("kintilol"+i, "onResponse: " + count);
                        BottomItem bottom = new BottomItem(String.valueOf(count));
                        count = 0 ;
                        items.add(bottom);
                        historyAdapter.notifyDataSetChanged();
                    }
                }else {
                    try {
                        assert response.errorBody() != null;
                        Log.d("anyingsiah", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("dateanying", "onResponse: " + t);

            }
        });
    }

    private Date buildRandomDateInCurrentMonth() {
        Random random = new Random();
        return DateUtils.buildDate(random.nextInt(31) + 1);
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
