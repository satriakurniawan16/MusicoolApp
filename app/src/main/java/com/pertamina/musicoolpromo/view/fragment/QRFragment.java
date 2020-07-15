package com.pertamina.musicoolpromo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mukesh.OtpView;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.activity.FakeProductActivity;
import com.pertamina.musicoolpromo.view.activity.GenuineProductActivity;
import com.pertamina.musicoolpromo.view.activity.MainActivity;
import com.pertamina.musicoolpromo.view.activity.MasterActivity;
import com.pertamina.musicoolpromo.view.activity.RetrofitVerifiedActivity;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_CUSTOMER;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_OUTLET;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_TECHNICIAN;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_AMOUNT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_ID;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_LASTPOSITION;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_NAME;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_SCANNED_BY;

public class QRFragment extends BaseFragment {

    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private Activity activity;
    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;
    final int REQUEST_CODE_CAMERA = 999;
    private MasterActivity myContext;
    private ProgressDialog pDialog;
    private Button showDialog;
    private SharePreferenceManager sharePreferenceManager;

    public QRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpView(view);
        generateView(view);
        setupListener(view);

    }

    @Override
    public void setUpView(View view) {
        activity = getActivity();
        pDialog = new ProgressDialog(getContext());
        scannerView = view.findViewById(R.id.scanner_view);
        showDialog = view.findViewById(R.id.button_code);
        mCodeScanner = new CodeScanner(activity, scannerView);
        sharePreferenceManager = new SharePreferenceManager(getContext());
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void generateView(View view) {
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
                        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                            getTechnician(result.getText());
                        } else {
                            getCodeType(result.getText());
                        }
                    }
                });
            }
        });

        checkCameraPermission();

        mCodeScanner.setErrorCallback(error -> activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QRFragment.this.getActivity(), QRFragment.this.getString(R.string.error, error), Toast.LENGTH_LONG).show();
                    }
                }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
    public void setupListener(View view) {
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_TECHNICIAN) || sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_OUTLET) ){
                    showchooseDialog();
                }
                else {
                    showDialog();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
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

    private void checkCameraPermission() {
        Dexter.withActivity(activity)
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

    @Override
    public void onAttach(Activity activity) {
        myContext = (MasterActivity) activity;
        super.onAttach(activity);
    }


    private void getTechnician(String Code) {
        displayLoader();
        ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);
        Call<JsonObject> call = service.getTechnician();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    JsonObject jsonObject = response.body();
                    JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                    pDialog.dismiss();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject json = jsonArray.get(i).getAsJsonObject();
                        String name = json.get("nama").getAsString();
                        String id = json.get("id").getAsString();
                        if (id.equals(Code)) {
                            Intent intent = new Intent(getContext(), GenuineProductActivity.class);
                            intent.putExtra(INTENT_EXTRA_ID, Code);
                            intent.putExtra(INTENT_EXTRA_NAME, name);
                            startActivity(intent);
                            return;
                        }
                    }
                    Intent intent = new Intent(getContext(), FakeProductActivity.class);
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

    private void getCodeType(String code) {
        displayLoader();
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.checkCode("Bearer " + sharePreferenceManager.getToken(), code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                pDialog.dismiss();
                Log.d("tolil", "onResponse: " + response.code() + response.message() + response.body());
               if (response.code() == 200) {
                    String codeType = jsonObject.get("type").getAsString();
                    if (codeType.equals("retrofit")) {
                        if (sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_TECHNICIAN) ) {
                            getRetrofit(code);
                        } else {
                            Toast.makeText(getContext(), "Fitur ini hanya bisa digunakan oleh teknisi dan outlet", Toast.LENGTH_SHORT).show();
                            mCodeScanner.startPreview();
                        }
                    } else {
                        setProduct(code);
                    }
                } else if(response.code() == 401){
                   Toast.makeText(getContext(), "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getContext(), MainActivity.class));
                   sharePreferenceManager.removeToken();
                   Objects.requireNonNull(getActivity()).finish();
                }else  {
                    Intent intent = new Intent(getContext(), FakeProductActivity.class);
                    startActivity(intent);
                }

                    Log.d("xianying", "onResponse: " + jsonObject);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }

    private void getRetrofit(String code) {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.checkRetrofit("Bearer " + sharePreferenceManager.getToken(), code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    final JsonObject jsonObject = response.body();
                    String codeResult = jsonObject.get("id").getAsString();
                    pDialog.dismiss();
                    if (code.equals(codeResult)) {
                        Intent intent = new Intent(getContext(), RetrofitVerifiedActivity.class);
                        if (!jsonObject.get("submited_by").isJsonNull()) {
                            String submitResult = jsonObject.get("submited_by").getAsString();
                            intent.putExtra(INTENT_EXTRA_SCANNED_BY, submitResult);
                            intent.putExtra(INTENT_EXTRA_ID, codeResult);
                        }
                        intent.putExtra(INTENT_EXTRA_ID, codeResult);
                        startActivity(intent);
                    }
                }else if(response.code() == 401){
                    Toast.makeText(getContext(), "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
                    sharePreferenceManager.removeToken();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }else {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
                pDialog.dismiss();
            }
        });
    }


    private void getProduct(String code,String scanBy,String accType) {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getProduct("Bearer " + sharePreferenceManager.getToken(), "application/json", code);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    final JsonArray jsonArray = response.body();
                    final JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    String codeResult = jsonObject.get("id").getAsString();
                    JsonObject productResult = jsonObject.get("product").getAsJsonObject();
                    String name = productResult.get("name").getAsString();
                    String lastPosition = jsonObject.get("last_position").getAsString();

                    pDialog.dismiss();
                    if (code.equals(codeResult)) {
                            Intent intent = new Intent(getContext(), GenuineProductActivity.class);
                            intent.putExtra(INTENT_EXTRA_ID, code);
                            intent.putExtra(INTENT_EXTRA_NAME, name);
                            intent.putExtra(INTENT_EXTRA_SCANNED_BY, scanBy);
                            intent.putExtra(INTENT_EXTRA_LASTPOSITION, accType);
                            startActivity(intent);
                    }
                }else if(response.code() == 401){
                    Toast.makeText(getContext(), "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
                    sharePreferenceManager.removeToken();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
                pDialog.dismiss();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.code_popup,
                null);

        Button button = mView.findViewById(R.id.submit);
        OtpView otpView;
        otpView = mView.findViewById(R.id.otp_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = Objects.requireNonNull(otpView.getText()).toString();
                if (code.length() < 9) {
                    Toast.makeText(getContext(), "Kode tidak boleh kurang dari 9", Toast.LENGTH_SHORT).show();
                } else {
                    SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
                    if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                        getTechnician(code);
                    } else {
                        getCodeType(code);
                    }
                }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showDialogRetrofit() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.code_pop_up_retrofit,
                null);

        Button button = mView.findViewById(R.id.submit);
        OtpView otpView;
        otpView = mView.findViewById(R.id.otp_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = Objects.requireNonNull(otpView.getText()).toString();
                if (code.length() < 9) {
                    Toast.makeText(getContext(), "Kode tidak boleh kurang dari 10", Toast.LENGTH_SHORT).show();
                } else {
                    SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
                    if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {
                        getTechnician(code);
                    } else {
                        getCodeType(code);
                    }
                }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showchooseDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.pop_up_select,
                null);

        TextView productText = mView.findViewById(R.id.product);
        TextView retrofitText = mView.findViewById(R.id.retrofit);

        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        retrofitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRetrofit();
            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void setProduct(String code) {
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonObject> call = service.setProduct("Bearer " + sharePreferenceManager.getToken(), "application/json", code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                pDialog.dismiss();
                Log.d("ikantongkol", "onResponse: " + response.code() + response.message() + response.body());
                if (response.code() == 400) {
                    try {
                        assert response.errorBody() != null;
                        String jsonString = response.errorBody().string();
                        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
                        if (jsonObject.has("scanned_by") == true) {
                            JsonObject jsonScan = jsonObject.get("scanned_by").getAsJsonObject();
                            String scanby = jsonScan.get("account_id").getAsString();
                            String accType = jsonScan.get("account_type").getAsString();
                            getProduct(code,scanby,accType);
                        }else {
                            getProduct(code,"-","-");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    JsonObject point = jsonObject.get("create_point_history").getAsJsonObject();
                    String name = point.get("description").getAsString();
                    String amount = point.get("amount").getAsString();
                    Intent intent = new Intent(getContext(), GenuineProductActivity.class);
                    intent.putExtra(INTENT_EXTRA_ID, code);
                    intent.putExtra(INTENT_EXTRA_NAME, name);
                    intent.putExtra(INTENT_EXTRA_AMOUNT, amount);
                    startActivity(intent);
                } else if (response.code() == 401){
                    Toast.makeText(getContext(), "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
                    sharePreferenceManager.removeToken();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
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

}
