package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.GlobalString;

public class PrivacyPolicy extends BaseActivity {

    TextView privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setUpView();
        generateView();

    }

    @Override
    public void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kebijakan dan Privasi");
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        privacy = findViewById(R.id.privacytext);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            privacy.setText(Html.fromHtml(GlobalString.ReusableString.TNC, Html.FROM_HTML_MODE_COMPACT));
        } else {
            privacy.setText(Html.fromHtml(GlobalString.ReusableString.TNC));
        }
    }

    @Override
    public void setupListener() {

    }
}
