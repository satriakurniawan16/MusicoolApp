package com.pertamina.musicoolpromo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_AMOUNT;

public class RewardActivity extends BaseActivity {

    private Button button;
    private TextView textPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        setUpView();
        setupListener();
        generateView();
    }

    @Override
    public void setUpView() {
        textPoint = findViewById(R.id.point_text);
        button = findViewById(R.id.to_home);
    }

    @Override
    public void generateView() {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);

        textPoint.setText(getIntent().getStringExtra(INTENT_EXTRA_AMOUNT));
    }

    @Override
    public void setupListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RewardActivity.this,MasterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
