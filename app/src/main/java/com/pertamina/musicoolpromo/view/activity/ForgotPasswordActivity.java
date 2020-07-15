package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.forgotpass.ForgotPass;
import com.pertamina.musicoolpromo.model.register.User;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.GlobalMethod;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ForgotPasswordActivity extends BaseActivity {

    private Button sendForgotPassword;
    private EditText emailText;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    GlobalMethod globalMethod ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setUpView();
        generateView();
        setupListener();

    }

    @Override
    public void setUpView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lupa Kata Sandi");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        sendForgotPassword = findViewById(R.id.send_forgot_password);
        constraintLayout = findViewById(R.id.rootLayout);
        emailText = findViewById(R.id.editText);

        globalMethod = new GlobalMethod();
    }

    @Override
    public void generateView() {

    }

    @Override
    public void setupListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sendForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSendForgotPassword(emailText.getText().toString().trim());
            }
        });
    }

    private void setSendForgotPassword(String email){
        ForgotPass forgotPass = new ForgotPass();
        forgotPass.setEmail(email);

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {
            Call<JsonObject> call = service
                    .forgotPassword("application/json", forgotPass);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("password", "Successo" + call.toString() + response.body());
                    User user = null;
                    if (response.code() == 400) {
                        try {
                            Log.d("lolfinal", "onResponse - Status : " + response.code() + response.errorBody().string() + response.message());
                            globalMethod.snackBar(constraintLayout,"Email tidak ditemukan");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else {
                        intentsendEmailForgotPassword();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
            Log.d("lolfinal", "uploadError: " + e.toString());
        }
    }

    private void intentsendEmailForgotPassword(){
        Intent intent = new Intent(ForgotPasswordActivity.this,EmailVerificationActivity.class);
        intent.putExtra("emailverification","Link untuk pengaturan ulang sandi telah dikirim ke email anda");
        startActivity(intent);
    }
}
