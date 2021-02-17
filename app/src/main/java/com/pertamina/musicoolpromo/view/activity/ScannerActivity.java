package com.pertamina.musicoolpromo.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mukesh.OtpView;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.login.Login;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends BaseActivity {

    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private Activity activity;
    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;
    final int REQUEST_CODE_CAMERA = 999;
    private MasterActivity myContext;
    private ProgressDialog pDialog;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        setUpView();
        generateView();
        setupListener();
    }


    @Override
    public void setUpView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        pDialog = new ProgressDialog(this);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void generateView() {

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(300);
                        }
                        String resultcode = result.getText();
                        int length = resultcode.length();
                        Log.d("length", "run: " + length);
                        getCodeType(result.getText());
                    }
                });
            }
        });
        checkCameraPermission();

        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(this, getString(R.string.error, error), Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }


    }

    @Override
    public void setupListener() {
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void checkCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void getProduct(String code) {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getProduct("Bearer " + sharePreferenceManager.getToken(), "application/json", code);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                final JsonArray jsonArray = response.body();
                final JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                String codeResult = jsonObject.get("id").getAsString();
                String lastPosition = jsonObject.get("last_position").getAsString();
                String lastScanned = jsonObject.get("last_scanned_by").getAsString();
                JsonObject productResult = jsonObject.get("product").getAsJsonObject();
                String name = productResult.get("name").getAsString();
                pDialog.dismiss();
                if (code.equals(codeResult)) {
                    if (sharePreferenceManager.getAccountType().equals("DPT")) {
                        Intent intent = new Intent(ScannerActivity.this, GenuineDepotActivity.class);
                        intent.putExtra("id", codeResult);
                        intent.putExtra("name", name);
                        intent.putExtra("scanned_by", lastScanned);
                        intent.putExtra("last_position", lastPosition);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ScannerActivity.this, GenuineProductActivity.class);
                        intent.putExtra("id", codeResult);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
                pDialog.dismiss();
            }
        });
    }

    private void setProduct(String code) {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.setProduct("Bearer " + sharePreferenceManager.getToken(), "application/json", code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                pDialog.dismiss();
                Log.d("ikantongkol", "onResponse: " + response.code() + response.message() + response.body());
                if (response.code() == 400) {
                    getProduct(code);
                } else {
                    JsonObject jsonObject = response.body();
                    JsonObject point = jsonObject.get("create_point_history").getAsJsonObject();
                    String name = point.get("description").getAsString();
                    String amount = point.get("amount").getAsString();
                    Intent intent = new Intent(ScannerActivity.this, ProductVerifiedActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("amount", amount);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
                pDialog.dismiss();
            }
        });
    }

    private void getProductBreezon(String code) {
        setProductBreezon(code);
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getProductBreezon("Bearer " + sharePreferenceManager.getToken(), "application/json", code);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                final JsonArray jsonArray = response.body();
                final JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                Log.d("tesbreezon", "onResponse: " + jsonObject);
                String codeResult = jsonObject.get("id").getAsString();
                String lastPosition = "";
                String lastScanned = "";
                JsonElement el = jsonObject.get("last_position");
                if(el != null || el.isJsonNull()){
                    lastPosition = jsonObject.get("last_position").getAsString();
                    lastScanned = jsonObject.get("last_scanned_by").getAsString();
                }
                JsonObject productResult = jsonObject.get("breezon_product").getAsJsonObject();
                String name = productResult.get("name").getAsString();
                pDialog.dismiss();
                if (code.equals(codeResult)) {
                    if (sharePreferenceManager.getAccountType().equals("DPT")) {
                        Intent intent = new Intent(ScannerActivity.this, GenuineDepotActivity.class);
                        intent.putExtra("id", codeResult);
                        intent.putExtra("name", name);
                        intent.putExtra("scanned_by", lastScanned);
                        intent.putExtra("last_position", lastPosition);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ScannerActivity.this, GenuineProductActivity.class);
                        intent.putExtra("id", codeResult);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
                pDialog.dismiss();
            }
        });
    }

    private void setProductBreezon(String code) {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.setProductBreezon("Bearer " + sharePreferenceManager.getToken(), "application/json", code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
                pDialog.dismiss();
            }
        });
    }


    private void displayLoader() {
        pDialog.setMessage("Tunggu sebentar");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void getCodeType(String code) {
        displayLoader();
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(ScannerActivity.this);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.checkCode("Bearer " + sharePreferenceManager.getToken(), code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                pDialog.dismiss();
                Log.d("tolil", "onResponse: " + response.code() + response.message() + response.body());
                if (response.code() == 400) {
                    Intent intent = new Intent(ScannerActivity.this, FakeNewProductActivity.class);
                    startActivity(intent);
                } else if (response.code() == 200) {
                    String codeType = jsonObject.get("type").getAsString();
                    if (codeType.equals("retrofit")) {
                        Toast.makeText(ScannerActivity.this, "Fitur ini hanya bisa digunakan oleh teknisi dan outlet", Toast.LENGTH_SHORT).show();
                        mCodeScanner.startPreview();
                    } else if (codeType.equals("breezon_retrofit")) {
                        Toast.makeText(ScannerActivity.this, "Fitur ini hanya bisa digunakan oleh teknisi dan outley", Toast.LENGTH_SHORT).show();
                        mCodeScanner.startPreview();
                    } else if (codeType.equals("breezon_product")) {
                        getProductBreezon(code);
                    } else {
                        setProduct(code);
                    }
                }

                Log.d("xianying", "onResponse: " + jsonObject);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
        } else if (item.getItemId() == R.id.input_code) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    private void logout() {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(this);
        sharePreferenceManager.removeToken();
        sharePreferenceManager.removeAccount();
        sharePreferenceManager.removeEmail();
        sharePreferenceManager.removePassword();
        sharePreferenceManager.removeProfile();
        startActivity(new Intent(ScannerActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        renewalToken();
    }

    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ScannerActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.code_popup,
                null);

        Button button = mView.findViewById(R.id.submit);
        OtpView otpView;
        otpView = mView.findViewById(R.id.otp_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = Objects.requireNonNull(otpView.getText()).toString();
                if (code.length() < 9) {
                    Toast.makeText(ScannerActivity.this, "Kode tidak boleh kurang dari 9", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(otpView.getText()).toString(), Toast.LENGTH_SHORT).show();
                    getCodeType(code);
                }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    private void renewalToken() {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(ScannerActivity.this);
        String email = sharePreferenceManager.getEmail();
        String password = sharePreferenceManager.getPassword();
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        login.setApp_version("0.9");

        Log.d("lolfinal", "renewalToken: " + login);

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<String> call = service
                    .newToken("application/json", login);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("lolfinal", "SuccessoTOLOL" + call.toString() + response.body());
//                    Log.d("lolfinal", "onResponse - Status : " + response.code() + response.errorBody().toString() + response.message());
                    sharePreferenceManager.setToken(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
            Log.d("lolfinal", "uploadError: " + e.toString());
        }
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
