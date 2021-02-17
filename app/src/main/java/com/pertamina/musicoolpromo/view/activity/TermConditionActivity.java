package com.pertamina.musicoolpromo.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.GlobalString;

public class TermConditionActivity extends BaseActivity {

    TextView term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        setUpView();
        generateView();
    }

    @Override
    public void setUpView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Syarat dan Ketentuan");
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        term = findViewById(R.id.term_condition_text);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermConditionActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void generateView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            term.setText(Html.fromHtml(GlobalString.ReusableString.TNC, Html.FROM_HTML_MODE_COMPACT));
        } else {
            term.setText(Html.fromHtml(GlobalString.ReusableString.TNC));
        }
    }

    @Override
    public void setupListener() {

    }
}
