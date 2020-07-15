package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_SCANNED_BY;

public class RetrofitVerifiedActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button buttonFill;
    private TextView titleResult;
    private TextView idResult;
    private TextView detailResult;
    private TextView statusResult;
    private SharePreferenceManager sharePreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_verified);
        setUpView();
        setupListener();
        generateView();
    }

    @Override
    public void setUpView() {
        toolbar = findViewById(R.id.toolbar);

        titleResult = findViewById(R.id.textView6);
        detailResult = findViewById(R.id.textView8);
        statusResult = findViewById(R.id.status_retrofit);
        idResult = findViewById(R.id.textView7);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);

        buttonFill = findViewById(R.id.button_fill);

        sharePreferenceManager = new SharePreferenceManager(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void generateView() {

        if(getIntent().getStringExtra(INTENT_EXTRA_SCANNED_BY) != null){
            Log.d("mantap anying", "generateView: ");
            buttonFill.setVisibility(View.GONE);
            statusResult.setVisibility(View.VISIBLE);
            if(getIntent().getStringExtra(INTENT_EXTRA_SCANNED_BY).equals(sharePreferenceManager.getAccountID())){
                statusResult.setText("Poin sudah anda claim");
            }else {
                statusResult.setText("Didaftarkan oleh "+getIntent().getStringExtra(INTENT_EXTRA_SCANNED_BY));
            }
            titleResult.setText("TERDAFTAR");
            detailResult.setText("Data retrofit customer telah terdaftar");
            idResult.setText("Retrofit "+getIntent().getStringExtra(INTENT_EXTRA_ID));
        }

    }

    @Override
    public void setupListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetrofitVerifiedActivity.this, MasterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetrofitVerifiedActivity.this,CustomerRetrofitActivity.class);
                intent.putExtra(INTENT_EXTRA_ID,getIntent().getStringExtra(INTENT_EXTRA_ID));
                startActivity(intent);
            }
        });
    }



}
