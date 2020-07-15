package com.pertamina.musicoolpromo.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.adapter.SectionsPagerAdapter;
import com.pertamina.musicoolpromo.view.base.BaseFragment;

import java.util.Objects;

public class FeedFragment extends BaseFragment {

    private int[] tabIcons = {
            R.drawable.ic_news,
            R.drawable.ic_promo
    };

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
//        generateView();

    }

    @Override
    public void setUpView(View view) {

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), Objects.requireNonNull(getChildFragmentManager()));
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(tabIcons[i]);
        }

    }

    @Override
    public void generateView(View view) {

    }

    @Override
    public void setupListener(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        showDialogue();
    }

    public void showDialogue(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        View mView = getLayoutInflater().inflate(R.layout.pop_up_feed,
                null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
