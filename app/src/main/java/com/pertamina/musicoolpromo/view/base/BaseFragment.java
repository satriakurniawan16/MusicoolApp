package com.pertamina.musicoolpromo.view.base;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public abstract void setUpView(View view);
    public abstract void generateView(View view);
    public abstract void setupListener(View view);

}
