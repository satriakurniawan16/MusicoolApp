package com.pertamina.musicoolpromo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.weiwangcn.betterspinner.library.BetterSpinner;

public class OrderMerchantActivity extends BaseActivity {

    private BetterSpinner provinceBetterSpinner;
    private BetterSpinner districtBetterSpinner;
    private BetterSpinner cityBetterSpinner;
    private BetterSpinner serviceSpinner;
    private BetterSpinner serviceSpinner2;
    private BetterSpinner serviceSpinner3;
    private Button button;

    private String[] sample = {
            "Sample1",
            "Sample2"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_merchant);

        setUpView();
        generateView();

    }

    @Override
    public void setUpView() {
        button = findViewById(R.id.button);
        provinceBetterSpinner = findViewById(R.id.province_spinner);
        districtBetterSpinner = findViewById(R.id.district_spinner);
        cityBetterSpinner = findViewById(R.id.city_spinner);
        serviceSpinner = findViewById(R.id.choose_service);
        serviceSpinner2 = findViewById(R.id.choose_service2);
        serviceSpinner3 = findViewById(R.id.choose_service3);
        button = findViewById(R.id.button);
    }

    @Override
    public void generateView() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sample);
        provinceBetterSpinner.setAdapter(adapter);
        cityBetterSpinner.setAdapter(adapter);
        districtBetterSpinner.setAdapter(adapter);
        serviceSpinner.setAdapter(adapter);
        serviceSpinner2.setAdapter(adapter);
        serviceSpinner3.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderMerchantActivity.this,SendEmailActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void setupListener() {

    }

}
