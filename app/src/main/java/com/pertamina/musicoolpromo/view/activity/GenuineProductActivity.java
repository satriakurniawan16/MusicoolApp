package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_CUSTOMER;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_AMOUNT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_LASTPOSITION;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_NAME;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_SCANNED_BY;

public class GenuineProductActivity extends BaseActivity {

    private TextView detailProduct;
    private TextView pointProduct;
    private TextView titleProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genuine_product);
        setUpView();
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

        detailProduct = findViewById(R.id.textView7);
        titleProduct = findViewById(R.id.textView8);
        pointProduct = findViewById(R.id.point_text);

    }

    @Override
    public void generateView() {

        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(GenuineProductActivity.this);
        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
            pointProduct.setVisibility(View.GONE);
            titleProduct.setText("Teknisi ini adalah teknisi resmi \n dari pertamina");
            detailProduct.setText(getIntent().getStringExtra(INTENT_EXTRA_NAME) + "\n ("+getIntent().getStringExtra(INTENT_EXTRA_ID)+" )");
        } else {
            detailProduct.setText(getIntent().getStringExtra(INTENT_EXTRA_NAME) + "\n ("+getIntent().getStringExtra(INTENT_EXTRA_ID)+" )");
            if (getIntent().getStringExtra(INTENT_EXTRA_AMOUNT) != null) {
                pointProduct.setText(getIntent().getStringExtra(INTENT_EXTRA_AMOUNT));
                titleProduct.setText("Selamat anda telah mendapatkan poin");
            } else {
                pointProduct.setVisibility(View.GONE);
                String scannedBy = getIntent().getStringExtra(INTENT_EXTRA_SCANNED_BY);
                String last_position = getIntent().getStringExtra(INTENT_EXTRA_LASTPOSITION);
                String account = "";
                if (scannedBy.equals("-")) {
                    titleProduct.setText("Kode tabung belum di aktivasi");
                } else {
                    if (last_position.equals("DPT")) {
                        account = "Depot";
                    } else if (last_position.equals("AGN")) {
                        account = "Agen";
                    } else if (last_position.equals("OTL")) {
                        account = "Outlet";
                    } else {
                        account = "Teknisi";
                    }
                    if (scannedBy.equals(sharePreferenceManager.getAccountID())) {
                        titleProduct.setText("Poin sudah anda claim");
                    } else {
                        titleProduct.setText("Tabung ini tidak bisa di scan. Posisi scan terakir oleh  " + account + " dengan id \n " + scannedBy);
                    }
                }
            }
        }

    }

    @Override
    public void setupListener() {

    }
}
