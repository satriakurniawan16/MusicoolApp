package com.pertamina.musicoolpromo.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.register.UserGoogle;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.GlobalMethod;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends BaseActivity {

    private BetterSpinner provinceBetterSpinner;
    private BetterSpinner districtBetterSpinner;
    private BetterSpinner cityBetterSpinner;
    private BetterSpinner pkSpinner;
    private BetterSpinner merkSpinner;
    private BetterSpinner serviceSpinner;
    private Button button;

    private LinearLayout chooseDate;
    private TextView textDate;
    private EditText textAddress;
    private EditText textUnit;
    private EditText textNote;
    private EditText textktp;
    private EditText voucher;


    private String provinceStringID;
    private String districttStringID;
    private String cityStringID;
    private String addressString;
    private String provinceString;
    private String cityString;
    private String districtString;
    private String brandString;
    private String unitString;
    private String pkString;
    private String serviceString;
    private String noteString;
    private String emailString;
    private String phoneString;
    private String nameString;
    private String ktpString;

    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    private SharePreferenceManager sharePreferenceManager;


    ProgressDialog pDialog;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> districtAdapter;

    private String[] merek = {
            "Samsung",
            "LG",
            "Panasonic",
            "Daikin",
            "Sharp",
            "Medea",
            "TCL",
            "Lainnya"
    };

    private String[] capacity = {
            "1/2",
            "3/4",
            "1",
            "1,5",
            "2",
            "Ukuran Lainnya"
    };

    private String[] service = {
            "Service AC/Retrofit",
            "MC-22 3Kg (Isi Ulang)",
            "MC-22 3Kg (Isi+Tabung)",
            "MC-22 6Kg (Isi Ulang)",
            "MC-22 6Kg (Isi+Tabung)",
            "MC-22 45Kg (Isi Ulang)",
            "MC-22 45Kg (Isi+Tabung)",
            "MC-134 3Kg (Isi Ulang)",
            "MC-134 3Kg (Isi+Tabung)",
            "MC-22 45Kg (Isi Ulang)",
            "MC-134 6Kg (Isi Ulang)",
            "MC-134 6Kg (Isi Ulang)",
            "MC-134 45Kg (Isi+Tabung)",
    };

    ArrayList<String> province;
    ArrayList<String> provinceID;
    ArrayList<String> city;
    ArrayList<String> cityID;
    ArrayList<String> district;
    ArrayList<String> districtID;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setUpView();
        generateView();
        setupListener();

    }

    @Override
    public void setUpView() {
        provinceBetterSpinner = findViewById(R.id.province_spinner);
        districtBetterSpinner = findViewById(R.id.district_spinner);
        cityBetterSpinner = findViewById(R.id.city_spinner);
        merkSpinner = findViewById(R.id.merk_spinner2);
        pkSpinner = findViewById(R.id.pk_spinner);
        serviceSpinner = findViewById(R.id.choose_service);
        button = findViewById(R.id.button);
        textAddress = findViewById(R.id.address);
        textUnit = findViewById(R.id.unit_text);
        textNote = findViewById(R.id.note_text);
        relativeLayout = findViewById(R.id.root);
        textktp = findViewById(R.id.ktp);
        voucher = findViewById(R.id.voucher);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pesan Sekarang");
        toolbar.setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        sharePreferenceManager = new SharePreferenceManager(OrderActivity.this);


        getUser();
    }

    @Override
    public void generateView() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, merek);
        final ArrayAdapter<String> adapterPK = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, capacity);
        final ArrayAdapter<String> adapterService = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, service);

        merkSpinner.setAdapter(adapter);
        serviceSpinner.setAdapter(adapterService);
        pkSpinner.setAdapter(adapterPK);

        getProvinces();
    }

    @Override
    public void setupListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOrderForm();
            }
        });

        provinceBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtBetterSpinner.setText("");
                cityBetterSpinner.setText("");
                getCities(provinceID.get(i));
                provinceStringID = provinceID.get(i);
            }
        });

        cityBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtBetterSpinner.setText("");
                getDistricts(cityID.get(i));
                cityStringID = cityID.get(i);
            }
        });

        districtBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districttStringID = districtID.get(i);
            }
        });
    }

    private void getProvinces() {

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getProvinces();

        province = new ArrayList<>();
        provinceID = new ArrayList<>();
        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

        provinceBetterSpinner.setHint(R.string.wait_data);
        provinceBetterSpinner.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("name").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result + "id = " + resultID);
                    province.add(result);
                    provinceID.add(resultID);
                }
                provinceBetterSpinner.setHint(R.string.choose_province);
                provinceBetterSpinner.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MasterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getCities(String id) {
        Log.d("lol", "getCities: " + id);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getCities(id, "1", "0");

        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

        cityBetterSpinner.setHint(R.string.wait_data);
        cityBetterSpinner.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.d("datacity", "onResponse: " + response.body());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("city").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                    city.add(result);
                    cityID.add(resultID);
                }
                cityBetterSpinner.setHint(R.string.choose_city);
                cityBetterSpinner.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    private void getDistricts(String id) {
        Log.d("lol", "getDistricts: " + id);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getDistricts(id);

        district = new ArrayList<>();
        districtID = new ArrayList<>();

        districtBetterSpinner.setHint(R.string.wait_data);
        districtBetterSpinner.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.d("datacity", "onResponse: " + response.body());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("sub_district").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                    district.add(result);
                    districtID.add(resultID);
                }
                districtBetterSpinner.setHint(R.string.choose_district);
                districtBetterSpinner.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    private void setSpinnerAdapter() {
        provinceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, province);

        provinceBetterSpinner.setAdapter(provinceAdapter);

        cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, city);

        cityBetterSpinner.setAdapter(cityAdapter);

        districtAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, district);

        districtBetterSpinner.setAdapter(districtAdapter);
    }




    private void checkOrderForm() {

        addressString = textAddress.getText().toString().trim();
        provinceString = provinceBetterSpinner.getText().toString().trim();
        cityString = cityBetterSpinner.getText().toString().trim();
        districtString = districtBetterSpinner.getText().toString().trim();
        brandString = merkSpinner.getText().toString().trim();
        unitString = textUnit.getText().toString().trim();
        pkString = pkSpinner.getText().toString().trim();
        serviceString = serviceSpinner.getText().toString().trim();
        noteString = textNote.getText().toString().trim();
        ktpString = textktp.getText().toString().trim();

        if (TextUtils.isEmpty(ktpString)) {
            textktp.setError("No. Ktp tidak boleh kosong");
            textktp.findFocus();
            return;
        }else {
            textktp.setError(null);
        }
        if (TextUtils.isEmpty(addressString)) {
            textAddress.setError("Alamat tidak boleh kosong");
            textAddress.findFocus();
            return;
        }else {
            textAddress.setError(null);
        }
        if (TextUtils.isEmpty(provinceString)) {
            provinceBetterSpinner.setError("Provinsi tidak boleh kosong");
            provinceBetterSpinner.findFocus();
            return;
        }else {
            provinceBetterSpinner.setError(null);
        }
        if (TextUtils.isEmpty(cityString)) {
            cityBetterSpinner.setError("Kota tidak boleh kosong");
            cityBetterSpinner.findFocus();
            return;
        }else {
            cityBetterSpinner.setError(null);
        }
        if (TextUtils.isEmpty(districtString)) {
            districtBetterSpinner.setError("Kecamatan tidak boleh kosong");
            districtBetterSpinner.findFocus();
            return;
        }
        else {
            districtBetterSpinner.setError(null);
        }
        if (TextUtils.isEmpty(brandString)) {
            merkSpinner.setError("Merek tidak boleh kosong");
            merkSpinner.findFocus();
            return;
        }else {
            merkSpinner.setError(null);
        }
        if (TextUtils.isEmpty(unitString) || unitString.equals("0")) {
            textUnit.setError("Unit tidak boleh kosong");
            textUnit.findFocus();
            return;
        }else {
            textUnit.setError(null);
        }
        if (TextUtils.isEmpty(pkString)) {
            pkSpinner.setError("Pk tidak boleh kosong");
            pkSpinner.findFocus();
            return;
        }else {
            pkSpinner.setError(null);
        }
        if (TextUtils.isEmpty(serviceString)) {
            serviceSpinner.setError("Jenis service tidak boleh kosong");
            serviceSpinner.findFocus();
            return;
        } else {
            serviceSpinner.setError(null);
        }
        if (TextUtils.isEmpty(noteString)) {
            textNote.setError("Keterangan tidak boleh kosong");
            textNote.findFocus();
            return;
        } else {
            textNote.setError(null);
            setOrder();
        }

    }

    private void getUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserGoogle userGoogle = dataSnapshot.getValue(UserGoogle.class);
                nameString = userGoogle.getName();
                emailString = userGoogle.getEmail();
                phoneString = userGoogle.getPhoneNumber();
                textAddress.setText(userGoogle.getAddress());
                provinceStringID = userGoogle.getProvinceID();
                cityStringID = userGoogle.getCityID();
                districttStringID  = userGoogle.getDistrictID();
                provinceStringID = userGoogle.getProvinceID();
                provinceString = userGoogle.getProvince();
                districtString = userGoogle.getDistrict();
                cityString = userGoogle.getCity();
                provinceBetterSpinner.setText(provinceString);
                districtBetterSpinner.setText(districtString);
                cityBetterSpinner.setText(cityString);
                if(userGoogle.getKtp() != null){
                    ktpString = userGoogle.getKtp();
                    textktp.setText(ktpString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setOrder() {

        displayLoader();
        ApiInterface service = ApiClient.getDataMusicool().create(ApiInterface.class);
        try {
            Call<ResponseBody> call = service
                    .setOrder(
                            createPartFromString(nameString),
                            createPartFromString(emailString),
                            createPartFromString(phoneString),
                            createPartFromString(addressString),
                            createPartFromString(provinceStringID),
                            createPartFromString(provinceString),
                            createPartFromString(cityStringID),
                            createPartFromString(cityString), //Untuk testing ubah ini,,gak bisa sama
                            createPartFromString(districtString),
                            createPartFromString(unitString), //Untuk testing ubah ini,,gak bisa sama
                            createPartFromString(brandString), //Untuk testing ubah ini,,gak bisa sama
                            createPartFromString("ket"),
                            createPartFromString(pkString),
                            createPartFromString("ket"),
                            createPartFromString(serviceString),
                            createPartFromString(noteString),
                            createPartFromString(ktpString),
                            createPartFromString(voucher.getText().toString()),
                            createPartFromString("android"));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("tolol beut", "onResponse: " + response.code()+ response.message());
                    if (response.isSuccessful()) {
                        pDialog.dismiss();
                        GlobalMethod globalMethod = new GlobalMethod();
                        globalMethod.snackBar(relativeLayout, "Pesanan sukses");
                        Intent intent = new Intent(OrderActivity.this,SendEmailActivity.class);
                        startActivity(intent);
                    } else {
                        pDialog.dismiss();
                        try {
                            Log.d("lolorder", "onResponse: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismiss();

                    Log.d("response final", "error: " + t.toString());
                    Toast.makeText(OrderActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismiss();
            Log.d("response final", "error: " + e);
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
