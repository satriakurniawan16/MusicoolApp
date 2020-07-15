package com.pertamina.musicoolpromo.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.HistoryPoints;
import com.pertamina.musicoolpromo.view.adapter.HistoryPointsAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.viewmodel.HistoryPointsViewModel;

import java.util.ArrayList;

public class RetrofitFragment extends BaseFragment {

    private HistoryPointsAdapter historyPointsAdapter;
    private HistoryPointsViewModel historyPointsViewModel;

    public RetrofitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retrofit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
        generateView(view);
    }

    @Override
    public void setUpView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.rv_historypoints);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        historyPointsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HistoryPointsViewModel.class);
        historyPointsViewModel = new HistoryPointsViewModel();

        historyPointsAdapter = new HistoryPointsAdapter();
        historyPointsAdapter.notifyDataSetChanged();

        historyPointsAdapter.setOnItemClickCallback(new HistoryPointsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(HistoryPoints data) {
                Toast.makeText(getContext(), "onClicked", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(historyPointsAdapter);

    }

    @Override
    public void generateView(View view) {

        historyPointsViewModel.getHistory().observe(getViewLifecycleOwner(), new Observer<ArrayList<HistoryPoints>>() {
            @Override
            public void onChanged(ArrayList<HistoryPoints> history) {
                if(history != null){
                    historyPointsAdapter.setData(history);
                }
            }
        });

        historyPointsViewModel.setItems("");

    }

    @Override
    public void setupListener(View view) {

    }
}
