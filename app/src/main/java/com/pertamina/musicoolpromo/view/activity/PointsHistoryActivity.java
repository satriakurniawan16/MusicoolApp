package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.adapter.PointsPagerAdapter;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.LockableViewPager;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_AGENT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_OUTLET;

public class PointsHistoryActivity extends BaseActivity{

    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_history);

        setUpView();
        generateView();

    }

    @Override
    public void setUpView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Riwayat Point");
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
                onBackPressed();
            }
        });

        PointsPagerAdapter pointsPagerAdapter = new PointsPagerAdapter(this, getSupportFragmentManager());
        LockableViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pointsPagerAdapter);
        viewPager.setSwipeable(false);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void generateView() {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        if(sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_AGENT) || sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_OUTLET)){
            tabs.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupListener() {

    }
}