package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;

public class GenuineDepotActivity extends BaseActivity {

    private Button buttonOK;
    private TextView textTitle;
    private TextView textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genuine_depot);

        setUpView();
        setupListener();
        generateView();
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
                onBackPressed();
            }
        });
        textTitle = findViewById(R.id.product_detail);
        textDescription = findViewById(R.id.textView8);
        buttonOK = findViewById(R.id.button_ok);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void generateView() {
        textTitle.setText(getIntent().getStringExtra("name")+" ( "+getIntent().getStringExtra("id")+" ) ");
        String scannedBy = getIntent().getStringExtra("scanned_by");
        String last_position = getIntent().getStringExtra("last_position");
        String account = "";
        if (last_position.equals("DPT")){
            account = "Depot";
        }else if (last_position.equals("AGN")){
           account = "Agen" ;
        }else if(last_position.equals("OTL")){
            account = "Outlet";
        }else {
            account = "Teknisi";
        }
        if(last_position.equals("DPT")){
           textDescription.setText("Produk siap di edarkan");
        }else {
           textDescription.setText("Tabung ini tidak bisa di reset. Posisi scan terakir oleh " + account + " dengan id " + scannedBy +" Mohon untuk mengubungi Call Center 135 ");
        }
    }

    @Override
    public void setupListener() {

    }
}
