package com.pertamina.musicoolpromo.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.image.ImageList;
import com.pertamina.musicoolpromo.view.adapter.ImageListAdapter;
import com.pertamina.musicoolpromo.view.adapter.ViewPagerAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.viewmodel.ListImageViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private ImageView[] dots;
    private ImageListAdapter imageListAdapter;
    private List<ImageList> imageResultList;
    private ViewPagerAdapter viewPagerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);

        viewPagerAdapter = new ViewPagerAdapter(getContext());

        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 1000, 4000);

        imageResultList = new ArrayList<>();

        imageListAdapter = new ImageListAdapter(imageResultList);
        imageListAdapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        imageListAdapter = new ImageListAdapter(imageResultList);
        imageListAdapter.setOnItemClickCallback(new ImageListAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ImageList data) {
//                parsingDataToDetail(data);
            }
        });

        recyclerView.setAdapter(imageListAdapter);

        getBanner();
    }
    @Override
    public void generateView(View view) {

    }

    @Override
    public void setupListener(View view) {
        imageListAdapter.setOnItemClickCallback(new ImageListAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ImageList data) {
//                parsingDataToDetail(data);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getImageList();
    }

    private void getImageList(){
        Log.d("disini", "getImage: ");
        ListImageViewModel dataViewModel = ViewModelProviders.of(this).get(ListImageViewModel.class);
        dataViewModel.getProductList(Objects.requireNonNull(getContext())).observe(this, new Observer<List<ImageList>>() {
            @Override
            public void onChanged(List<ImageList> movieResults) {
                imageResultList.clear();
                Log.d("lol", movieResults.toString());
                imageResultList.addAll(movieResults);
                imageListAdapter.notifyDataSetChanged();
//                showLoading(false);
            }
        });
    }

    private void getBanner(){

        ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);
        Call<JsonObject> call = service.getBanner();
        ArrayList<String> mDataBanner = new ArrayList<>();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    JsonObject jsonObject = response.body();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                    int countlol = 0;
                    for(int i = 0; i < jsonArray.size(); i++){
                        JsonObject newjsonObject = jsonArray.get(i).getAsJsonObject();
                        String result = newjsonObject.get("banner").getAsString();
                        mDataBanner.add(result);
                        countlol++;
                    }
                    Collections.reverse(mDataBanner);
                    viewPagerAdapter.setData(mDataBanner);
                    getDots(countlol);
                }
              }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }

        });
    }


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (isVisible()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() == 0) {
                            viewPager.setCurrentItem(1);
                        } else if (viewPager.getCurrentItem() == 1) {
                            viewPager.setCurrentItem(2);
                        } else if (viewPager.getCurrentItem() == 2) {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    public void getDots(int count){
        dots = new ImageView[count];
        try {
            for (int i = 0; i < dots.length ; i++) {

                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(4, 0, 4, 0);

                sliderDotspanel.addView(dots[i], params);
            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for(int i = 0; i< count; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }catch (NullPointerException e){

        }
    }

}
