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
import com.pertamina.musicoolpromo.model.Feed.NewsFeedResult;
import com.pertamina.musicoolpromo.view.activity.DetailFeedActivity;
import com.pertamina.musicoolpromo.view.adapter.FeedAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.viewmodel.FeedViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_FEED;


public class NewsFragment extends BaseFragment {


    private FeedAdapter feedAdapter;
    private List<NewsFeedResult> movieResultList;
    private ProgressBar progressBar;
    private LinearLayout emptyView;

    public NewsFragment() {
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
        return inflater.inflate(R.layout.fragment_news, container, false);
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
        emptyView = view.findViewById(R.id.empty_view);
        movieResultList = new ArrayList<>();

        feedAdapter = new FeedAdapter(movieResultList,emptyView);
        feedAdapter.notifyDataSetChanged();

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
        feedAdapter.setOnItemClickCallback(new FeedAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(NewsFeedResult data) {
                parsingDataToDetail(data);
            }
        });
        recyclerView.setAdapter(feedAdapter);

    }

    @Override
    public void generateView(View view) {


    }

    @Override
    public void setupListener(View view) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getNewsList();
    }

    private void parsingDataToDetail(NewsFeedResult news){
        news.setImages(news.getImages());
        news.setTitle(news.getTitle());
        news.setResume(news.getResume());
        news.setCreated_at(news.getCreated_at());

        Intent intent = new Intent(getContext(), DetailFeedActivity.class);
        intent.putExtra(DetailFeedActivity.EXTRA_DATA_FEED, news);
        intent.putExtra(INTENT_EXTRA_FEED, "News");
        startActivity(intent);
    }

    private void getNewsList() {
        FeedViewModel dataViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        dataViewModel.getNewsFeed(Objects.requireNonNull(getContext())).observe(this, new Observer<List<NewsFeedResult>>() {
            @Override
            public void onChanged(List<NewsFeedResult> movieResults) {
                movieResultList.clear();
                Log.d("lol", movieResults.toString());
                movieResultList.addAll(movieResults);
                Collections.reverse(movieResultList);
                feedAdapter.notifyDataSetChanged();
                feedAdapter.updateEmptyView();
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
