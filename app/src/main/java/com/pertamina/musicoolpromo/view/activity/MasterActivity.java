package com.pertamina.musicoolpromo.view.activity;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.fragment.FeedFragment;
import com.pertamina.musicoolpromo.view.fragment.HomeFragment;
import com.pertamina.musicoolpromo.view.fragment.NetworkFragment;
import com.pertamina.musicoolpromo.view.fragment.PointsFragment;
import com.pertamina.musicoolpromo.view.fragment.ProfileFragment;
import com.pertamina.musicoolpromo.view.fragment.QRFragment;
import com.pertamina.musicoolpromo.view.network.RetrofitGet;
import com.pertamina.musicoolpromo.view.network.RetrofitListener;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;


import q.rorbin.badgeview.QBadgeView;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_AGENT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_CUSTOMER;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_OUTLET;

public class MasterActivity extends BaseActivity {

    private Button button;
    private Button exitbutton;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private QBadgeView qBadgeView;
    private Button whatsapp;
    private int i = 0;
    private BottomNavigationView navView;
    private SharePreferenceManager sharePreferenceManager;

    private RetrofitGet retrofitGet;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        setUpView();
        generateView();
        setupListener();

    }

    @Override
    public void setUpView() {

        sharePreferenceManager = new SharePreferenceManager(MasterActivity.this);
        retrofitGet = new RetrofitGet();

        qBadgeView = new QBadgeView(MasterActivity.this);
        navView = findViewById(R.id.nav_view);
        imageView = findViewById(R.id.ic_qr);
        button = findViewById(R.id.action_button);
        exitbutton = findViewById(R.id.button_exit);
        linearLayout = findViewById(R.id.fab);
        whatsapp = findViewById(R.id.action_wa);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
    }

    @Override
    public void generateView() {
        Menu menu = navView.getMenu();
        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
            button.setVisibility(View.VISIBLE);
            exitbutton.setVisibility(View.VISIBLE);
            menu.getItem(3).setIcon(R.drawable.ic_network);
            menu.getItem(3).setTitle(R.string.title_network);
            menu.getItem(2).setVisible(false);
            imageView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            getBadgeView();
        } else {
            Log.d("mamangcuy", "generateView: " + sharePreferenceManager.getAccountType() + ACCOUNT_TYPE_AGENT);
            whatsapp.setVisibility(View.VISIBLE);
            if(sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_OUTLET) || sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_AGENT)){
//                menu.getItem(3).setVisible(false);
//                imageView.setVisibility(View.GONE);
                menu.getItem(2).setTitle("Scan");
                linearLayout.setVisibility(View.GONE);
            }else {
                menu.getItem(3).setIcon(R.drawable.ic_point);
                menu.getItem(3).setTitle("Poin");
            }
        }
    }

    public void getBadgeView() {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navView.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(4);
        if (sharePreferenceManager.getComplete().equals("false")) {
            qBadgeView.bindTarget(view).setBadgeNumber(1);
        } else {
            qBadgeView.hide(false);
            qBadgeView.bindTarget(view).setBadgeNumber(0);
        }
    }


    public void onClickWhatsApp(View view) {
        String url = "https://api.whatsapp.com/send?phone=62881023168099";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
            retrofitGet.getToken(this, new RetrofitListener() {
                @Override
                public void onRequestSuccess(String jsonString) {
                    if(!jsonString.equals("fail")){
                        sharePreferenceManager.setToken(jsonString);
                        Log.d("taianying", jsonString);
                    }else {
                        Log.d("taianying", jsonString);
                        Toast.makeText(MasterActivity.this, "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
                        sharePreferenceManager.removeToken();
                        startActivity(new Intent(MasterActivity.this,MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onRequestError(String error) {
                    Toast.makeText(MasterActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void setupListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharePreferenceManager.getComplete().equals("true")) {
                    Intent intent = new Intent(MasterActivity.this, OrderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MasterActivity.this, "Lengkapi profile anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWhatsApp(v);
            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitbutton.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            }
        });
        final int[] count = {0};

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            FragmentManager fm = getSupportFragmentManager();

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
                        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                            button.setVisibility(View.VISIBLE);
                            exitbutton.setVisibility(View.VISIBLE);
                        }else {
                            whatsapp.setVisibility(View.VISIBLE);
                        }
                        imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorDefault, null));
                        return true;

                    case R.id.navigation_feed:
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.main_container, new FeedFragment()).commit();
                        imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorDefault, null));
                        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                            button.setVisibility(View.VISIBLE);
                            exitbutton.setVisibility(View.VISIBLE);
                        }else {
                            whatsapp.setVisibility(View.VISIBLE);
                        }
                        return true;

                    case R.id.navigation_qr:
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.main_container, new QRFragment()).commit();
                        imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                            button.setVisibility(View.GONE);
                            exitbutton.setVisibility(View.GONE);
                        }else {
                            whatsapp.setVisibility(View.GONE);
                        }
                        return true;

                    case R.id.navigation_network:
                        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                            fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_container, new NetworkFragment()).commit();
                            imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorDefault, null));
                            button.setVisibility(View.VISIBLE);
                            exitbutton.setVisibility(View.VISIBLE);
                        } else {
                            fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_container, new PointsFragment()).commit();
                            imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorDefault, null));
                            button.setVisibility(View.GONE);
                            exitbutton.setVisibility(View.GONE);
                            whatsapp.setVisibility(View.VISIBLE);

                        }
                        return true;

                    case R.id.navigation_profile:
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.main_container, new ProfileFragment()).commit();
                        imageView.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorDefault, null));
                        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                            button.setVisibility(View.VISIBLE);
                            exitbutton.setVisibility(View.VISIBLE);
                        }else {
                            whatsapp.setVisibility(View.VISIBLE);
                        }
                        return true;
                }
                return false;
            }
        };
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}