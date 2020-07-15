package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;

public class ProductVerifiedActivity extends BaseActivity {

    TextView productDetail;
    Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_verified);
        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void setUpView() {
        productDetail = findViewById(R.id.product_detail);
        buttonOK = findViewById(R.id.button_ok);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void generateView() {
        String currentString = getIntent().getStringExtra("name");
        String newString = currentString.replace("Scan","");
        productDetail.setText(newString);
    }

    @Override
    public void setupListener() {
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
