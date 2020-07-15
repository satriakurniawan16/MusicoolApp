package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.reward.RewardRedeem;
import com.pertamina.musicoolpromo.model.reward.RewardResult;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemDetailActivity extends BaseActivity {

    public static final String EXTRA_DATA_REDEEM = "extra_data_redeem";
    private RewardResult rewardResult;
    private TextView title;
    private TextView description;
    private TextView validDate;
    private TextView pointCost;
    private TextView termCondition;
    private ImageView image;
    private Button claimButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_detail);
        setUpView();
        setupListener();
        generateView();
    }

    @Override
    public void setUpView() {
        rewardResult =  getIntent().getParcelableExtra(EXTRA_DATA_REDEEM);
        title = findViewById(R.id.title_point);
        description = findViewById(R.id.desc_point);
        validDate = findViewById(R.id.available_point);
        image = findViewById(R.id.image_point);
        claimButton = findViewById(R.id.button2);
        pointCost = findViewById(R.id.price_point);
        termCondition = findViewById(R.id.term_point);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
    }

    @Override
    public void generateView() {
        title.setText(rewardResult.getName());
        description.setText(rewardResult.getDescription());
        termCondition.setText(rewardResult.getTerm_and_conditions());

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String reformattedStr = myFormat.format(fromUser.parse(rewardResult.getStart_date()));
            String reformattedStr2 = myFormat.format(fromUser.parse(rewardResult.getEnd_date()));
            validDate.setText(reformattedStr+" - "+ reformattedStr2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pointCost.setText(rewardResult.getPoint_cost() + " poin");
        getImage(rewardResult.getId());
    }

    @Override
    public void setupListener() {
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(rewardResult.getId());
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getImage(String id)  {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.getRewardID("Bearer "+sharePreferenceManager.getToken(),id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call< JsonObject > call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                Log.d("tampan", "onResponse: " + response.code() + response.message());
                JsonObject imageObject = jsonObject.get("image_storage").getAsJsonObject();
                Glide.with(RedeemDetailActivity.this)
                        .load(imageObject.get("url").getAsString())
                        .into(image);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }


    private void showDialog(String myid) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RedeemDetailActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Apakah anda yakin ?");
        // set pesan dari dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        setReedem(myid);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void setReedem(String theid) {

        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(Objects.requireNonNull(RedeemDetailActivity.this));

        RewardRedeem rewardRedeem = new RewardRedeem();
        rewardRedeem.setReward_id(theid);
        Log.d("lolredeem", "setReedem: " + sharePreferenceManager.getAccountID());
        rewardRedeem.setRequested_by(sharePreferenceManager.getAccountID());

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {
            Call<JsonObject> call = service
                    .setReedem("application/json", "Bearer " + sharePreferenceManager.getToken(), rewardRedeem);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolredeem", "onResponse: ");
                    Log.d("lolredeem", "SuccessoTOLOL" + response.code() + response.message() + call.toString() + response.body());
                    if (response.code() == 200) {
                        Toast.makeText(RedeemDetailActivity.this, "Permintaan Anda dikirim", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RedeemDetailActivity.this,MasterActivity.class);
                        startActivity(intent);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            String errorString = response.errorBody().string();
                            JsonObject jsonObject = new JsonParser().parse(errorString).getAsJsonObject();
                            String parseJsonObject = jsonObject.get("message").getAsString();
                            if (parseJsonObject.equals("Not enough point")) {
                                Toast.makeText(RedeemDetailActivity.this, "Point anda tidak cukup", Toast.LENGTH_SHORT).show();
                            } else if (parseJsonObject.equals("Reward inactive")) {
                                Toast.makeText(RedeemDetailActivity.this, "Reward ini belum active", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RedeemDetailActivity.this, "Reward tidak tersedia", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolredeem", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
            Log.d("lolredeem", "uploadError: " + e.toString());
        }
    }

}
