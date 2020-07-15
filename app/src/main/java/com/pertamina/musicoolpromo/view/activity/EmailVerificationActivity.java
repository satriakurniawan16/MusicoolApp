package com.pertamina.musicoolpromo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;

public class EmailVerificationActivity extends BaseActivity {

    private TextView descriptionText;
    private String descriptionString;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        setUpView();
        generateView();
        setupListener();

    }

    @Override
    public void setUpView() {
        descriptionText = findViewById(R.id.description_text);
        button = findViewById(R.id.button);
    }

    @Override
    public void generateView() {
        if(getIntent().getStringExtra("emailverification")!= null) {
            descriptionString = getIntent().getStringExtra("emailverification");
            descriptionText.setText(descriptionString);
        }

    }

    @Override
    public void setupListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailVerificationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
