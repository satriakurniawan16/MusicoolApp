package com.pertamina.musicoolpromo.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.login.Login;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.GlobalMethod;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ProgressDialog pDialog;
    private FirebaseAuth mAuth;
    private TextInputEditText emailUser;
    private TextInputEditText passwordUser;
    private Button loginButton;
    private TextView forgotPassword;
    private TextView register;

    private ConstraintLayout constraintLayout;

    private GlobalMethod globalMethod;

    private SharePreferenceManager sharePreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpView();
        generateView();
        setupListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkEmailVerification();
    }

    @Override
    public void setUpView() {


        globalMethod = new GlobalMethod();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        constraintLayout = findViewById(R.id.root);
        mAuth = FirebaseAuth.getInstance();

        pDialog = new ProgressDialog(LoginActivity.this);

        emailUser = findViewById(R.id.user_email);
        passwordUser = findViewById(R.id.user_password);
        loginButton = findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.forgot_password);
        register = findViewById(R.id.register_button);
    }

    @Override
    public void generateView() {


    }

    @Override
    public void setupListener() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailString = emailUser.getText().toString().trim();
                final String passwordString = passwordUser.getText().toString().trim();
                if(TextUtils.isEmpty(emailString)){
                    TextInputLayout emailTIL = findViewById(R.id.textInputLayout2);
                    emailTIL.setError("Email tidak boleh kosong");
                } else if(!isEmailValid(emailString)){
                    TextInputLayout emailTIL = findViewById(R.id.textInputLayout2);
                    emailTIL.setError("Email tidak valid");
                }
                else if(TextUtils.isEmpty(passwordString)){
                    TextInputLayout passwordTIL = findViewById(R.id.textInputLayout);
                    passwordTIL.setError("Password tidak boleh kosong");
                }
                else {
//                    emailAuthentication(emailString,passwordString);
                    emailLogin(emailString,passwordString);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkEmailVerification(){
        try {
            final Intent intent = getIntent();
            if(intent.getStringExtra("name") != null){
                final String mNameUser = intent.getStringExtra("name");
                Intent mailClient = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gmail.com"));
                PendingIntent pendingIntent = PendingIntent.getActivity(LoginActivity.this, 0, mailClient, 0);

                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        try {
                            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(LoginActivity.this)
                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                                    .setTitle("Hi " + mNameUser)
                                    .setMessage("Please check your email, we send a verification email so you can log in to your account");
                            builder.show();
                        }catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error : "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                }.start();

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification mBuilder  = new Notification.Builder(LoginActivity.this)
                        .setContentTitle("Template")
                        .setContentText("Please check your email, we send a verification email so you can log in to your account")
                        .setSound(soundUri)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true).build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, mBuilder);

            }

        }catch (NullPointerException ignored){

        }
    }

    private void emailLogin(String email,String password){
        displayLoader();
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        login.setApp_version("0.9");

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<String> call = service
                    .loginAccount("application/json", login);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    pDialog.dismiss();
                    Log.d("lolfinal", "Successo" + call.toString() + response.body());

                    if(response.code() == 200 ){
                        sharePreferenceManager = new SharePreferenceManager(LoginActivity.this);
                        getAccountType(response.body());
                    }else if(response.code() == 403){
                        try {
                            assert response.errorBody() != null;
                            String responseErr = response.errorBody().string();
                            Log.d("LOOOOOLL", "onResponse: " + response.errorBody().string());
                            Log.d("LOOOOOLL", "onResponse: ll " + responseErr);
                            if(responseErr.equals("\"account inactive\"")) {
//                                Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                globalMethod.snackBar(constraintLayout,"Akun anda belum di aktivasi" );
                            }else {
                                globalMethod.snackBar(constraintLayout,"Email/Password anda salah");
//                                Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (response.code() == 400){
                        globalMethod.snackBar(constraintLayout,"Akun anda belum terdaftar");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    pDialog.dismiss();
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
            pDialog.dismiss();
            Log.d("lolfinal", "uploadError: " + e.toString());
            Toast.makeText(this, "Registrasi gagal coba lagi !", Toast.LENGTH_SHORT).show();
        }

    }




    private void sendToHome(){
        if(sharePreferenceManager.getAccountType().equals("DPT")){
            Intent intent = new Intent(LoginActivity.this, ScannerActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(LoginActivity.this, MasterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getAccountType(String token) {
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        Call<JsonObject> call = service.checkAccount("Bearer "+token);
        call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JsonObject jsonObject = response.body();
                        String accountType = jsonObject.get("account_type").getAsString();
                        String accountId = jsonObject.get("id").getAsString();
                        JsonObject jsonObjectImage = jsonObject.get("avatar_img").getAsJsonObject();
                        String url = jsonObjectImage.get("url").getAsString();
                        sharePreferenceManager = new SharePreferenceManager(LoginActivity.this);
                        sharePreferenceManager.setAccount(accountType);
                        sharePreferenceManager.setToken(token);
                        sharePreferenceManager.setProfile(jsonObject.toString());
                        sharePreferenceManager.setImage(url);
                        sharePreferenceManager.setEmail(emailUser.getText().toString().trim());
                        sharePreferenceManager.setPassword(passwordUser.getText().toString().trim());
                        sharePreferenceManager.setAccountID(accountId);
                        sendToHome();
                    }catch (Exception e){
                        Log.d("PANTEK", "onResponse: " + e.toString());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lol gagal", "onResponse: " + t);
                }
            });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void displayLoader() {
        pDialog.setMessage("Memverifikasi Akun...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void emailAuthentication (String email,String password){
        displayLoader();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Your email account is not registered.", Toast.LENGTH_SHORT).show();
                        } else {
                            try{
                                if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                                    Intent intent = new Intent(LoginActivity.this,MasterActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, "Please Verification Your Email", Toast.LENGTH_SHORT).show();
                                }
                            }catch (NullPointerException ignored){

                            }
                        }
                    }
                });
    }

}
