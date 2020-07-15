package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;

public class FakeProductActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_product);

        setUpView();
        generateView();
        setupListener();

    }

    @Override
    public void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FakeProductActivity.this,MasterActivity.class);
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
