package com.pertamina.musicoolpromo.view.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.activity.MasterActivity;
import com.pertamina.musicoolpromo.view.base.BaseFragment;

public class FakeFragment extends BaseFragment {

    private MasterActivity myContext;

    public FakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fake, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
        generateView(view);
    }

    @Override
    public void setUpView(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragManager = myContext.getSupportFragmentManager();
                fragManager.beginTransaction().replace(R.id.main_container, new QRFragment()).commit();
            }
        });
    }

    @Override
    public void generateView(View view) {

    }

    @Override
    public void setupListener(View view) {

    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (MasterActivity) activity;
        super.onAttach(activity);
    }

}
