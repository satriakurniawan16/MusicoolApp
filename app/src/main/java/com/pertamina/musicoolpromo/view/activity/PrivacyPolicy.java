package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;

public class PrivacyPolicy extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setUpView();

    }

    @Override
    public void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kebijakan dan Privasi");
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivacyPolicy.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void generateView() {

    }

    @Override
    public void setupListener() {

    }
}
