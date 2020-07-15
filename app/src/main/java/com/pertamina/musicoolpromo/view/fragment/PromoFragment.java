package com.pertamina.musicoolpromo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.promo.PromoResult;
import com.pertamina.musicoolpromo.view.activity.DetailFeedActivity;
import com.pertamina.musicoolpromo.view.adapter.PromoAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.viewmodel.FeedViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_FEED;

public class PromoFragment extends BaseFragment {


    private PromoAdapter promoAdapter;
    private ProgressBar progressBar;
    private List<PromoResult> promoResultList;
    private LinearLayout emptyView;

    public PromoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo, container, false);
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


        progressBar = view.findViewById(R.id.progressBar);
        emptyView = view.findViewById(R.id.empty_view);
        promoResultList = new ArrayList<>();
        promoAdapter = new PromoAdapter(promoResultList,emptyView);
        promoAdapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_feed);

        GridLayoutManager layoutManager
                = new GridLayoutManager(getContext(), 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(promoAdapter);

    }

    @Override
    public void generateView(View view) {


    }

    @Override
    public void setupListener(View view) {
        promoAdapter.setOnItemClickCallback(new PromoAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(PromoResult data) {
                parsingDataToDetail(data);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getPromoList();
    }

    private void parsingDataToDetail(PromoResult news){
        news.setImages(news.getImages());
        news.setTitle(news.getTitle());
        news.setDescription(news.getDescription());
        news.setCreated_at(news.getCreated_at());

        Intent intent = new Intent(getContext(), DetailFeedActivity.class);
        intent.putExtra(DetailFeedActivity.EXTRA_DATA_PROMO, news);
        intent.putExtra(INTENT_EXTRA_FEED, "Promo");
        startActivity(intent);
    }

    private void getPromoList() {
        FeedViewModel dataViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        dataViewModel.getPromo(Objects.requireNonNull(getContext())).observe(this, new Observer<List<PromoResult>>() {
            @Override
            public void onChanged(List<PromoResult> promoResults) {
                promoResultList.clear();
                Log.d("njengtololnamgrt", promoResults.toString());
                promoResultList.addAll(promoResults);
                Collections.reverse(promoResultList);
                promoAdapter.notifyDataSetChanged();
                promoAdapter.updateEmptyView();
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

}