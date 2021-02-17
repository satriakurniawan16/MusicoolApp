package com.pertamina.musicoolpromo.view.activity;

import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.retrofit.RetrofitCust;
import com.pertamina.musicoolpromo.model.retrofit.RetrofitUnit;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_AMOUNT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_ID;

public class CustomerRetrofitActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button buttonFill;
    private ImageView chooseImage;
    private EditText nameText;
    private EditText emailText;
    private EditText phoneNumber;
    private EditText addressText;
    private EditText zipText;
    private BetterSpinner provinceBetterSpinner;
    private BetterSpinner cityBetterSpinner;
    private BetterSpinner districtBetterSpinner;
    private BetterSpinner brandSpinner;
    private BetterSpinner capacitySpinner;

    private ArrayList<String> province;
    private ArrayList<String> provinceID;
    private ArrayList<String> city;
    private ArrayList<String> cityID;
    private ArrayList<String> district;
    private ArrayList<String> districtID;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> districtAdapter;
    private ArrayAdapter<String> brandAdapter;
    private ArrayAdapter<String> capacityAdapter;

    private String provinceIDString;
    private String cityIDString;
    private String districtIDString;
    private EditText modelText;
    private EditText serialText;

    private String emailString;
    private String nameString;
    private String address;
    private String zipcode;
    private String phoneString;
    private String modelString;
    private String brandString;
    private String capacityString;
    private String uuid;
    private String serialString;
    private ProgressDialog pDialog;

    private Uri filepath;
    private File file;
    private RequestBody reqFile;
    private static int IMG_CAMERA = 2;

    private byte[] inputData = null;

    Bitmap bitmapImage;

    SharePreferenceManager sharePreferenceManager;

    private String[] brand = {
            "Samsung",
            "LG",
            "Panasonic",
            "Daikin",
            "Sharp",
            "Midea",
            "TCL",
            "Merek Lainnya"
    };

    private String[] capacity = {
            "1/2",
            "3/4",
            "1",
            "1,5",
            "2",
            "Ukuran Lainnya"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_retrofit);
        setUpView();
        generateView();
        setupListener();
    }

    @Override
    public void setUpView() {

        pDialog = new ProgressDialog(CustomerRetrofitActivity.this);

        sharePreferenceManager = new SharePreferenceManager(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        buttonFill = findViewById(R.id.button_retrofit);
        chooseImage = findViewById(R.id.imageView7);
        nameText = findViewById(R.id.name_customer);
        emailText = findViewById(R.id.email_customer);
        phoneNumber = findViewById(R.id.phone_customer);
        zipText = findViewById(R.id.zip_customer);
        addressText = findViewById(R.id.address_customer);
        provinceBetterSpinner = findViewById(R.id.province_spinner);
        cityBetterSpinner = findViewById(R.id.city_spinner);
        districtBetterSpinner = findViewById(R.id.district_spinner);
        brandSpinner = findViewById(R.id.brand_ac_customer);
        capacitySpinner = findViewById(R.id.capacity_ac_customer);
        modelText = findViewById(R.id.model_ac_customer);
        serialText = findViewById(R.id.serial_customer);

        pDialog = new ProgressDialog(this);
    }

    @Override
    public void generateView() {
        cityBetterSpinner.setHint("Kota/Kabupaten");
        districtBetterSpinner.setHint("Kecamatan");
        provinceBetterSpinner.setHint("Provinsi");


        brandAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, brand);

        brandSpinner.setAdapter(brandAdapter);

        capacityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, capacity);

        capacitySpinner.setAdapter(capacityAdapter);

        getProvinces();
    }

    @Override
    public void setupListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerRetrofitActivity.this, MasterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cityBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.size() == 0) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        districtBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (district.size() == 0) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        provinceBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtBetterSpinner.setText("");
                cityBetterSpinner.setText("");
                getCities(provinceID.get(i));
                provinceIDString = provinceID.get(i);
            }
        });

        cityBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtBetterSpinner.setText("");
                getDistricts(cityID.get(i));
                cityIDString = cityID.get(i);
            }
        });

        districtBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtIDString = districtID.get(i);
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).
                        start(CustomerRetrofitActivity.this);
            }
        });

        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = nameText.getText().toString();
                emailString = emailText.getText().toString();
                phoneString = phoneNumber.getText().toString();
                address = addressText.getText().toString();
                zipcode = zipText.getText().toString();
                brandString = brandSpinner.getText().toString();
                modelString = modelText.getText().toString();
                capacityString = capacitySpinner.getText().toString();
                serialString = serialText.getText().toString();

                if (TextUtils.isEmpty(nameString)) {
                    nameText.setError("Nama customer tidak boleh kosong");
                    nameText.findFocus();
                    return;
                }else {
                    nameText.setError(null);
                }
                if (TextUtils.isEmpty(emailString)) {
                    emailText.setError("Email customer tidak boleh kosong");
                    emailText.findFocus();
                    return;
                }else {
                    emailText.setError(null);
                }
                if (!isEmailValid(emailString)) {
                    emailText.setError("Email tidak valid");
                    emailText.findFocus();
                    return;
                }else {
                    emailText.setError(null);
                }
                if (TextUtils.isEmpty(phoneString)) {
                    phoneNumber.setError("No.Handphone customer tidak boleh kosong");
                    phoneNumber.findFocus();
                    return;
                }else {
                    phoneNumber.setError(null);
                }
                if (TextUtils.isEmpty(provinceIDString)) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih Provinsi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cityIDString)) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih Kota/Kabupaten", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(districtIDString)) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih Kecamatan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    addressText.setError("Alamat customer tidak boleh kosong ");
                    addressText.findFocus();
                    return;
                }else {
                    addressText.setError(null);
                }
                if (TextUtils.isEmpty(zipcode)) {
                    zipText.setError("Kode pos customer tidak boleh kosong");
                    zipText.findFocus();
                    return;
                }else {
                    zipText.setError(null);
                }
                if (TextUtils.isEmpty(brandString)) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih Merek AC", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(serialString)) {
                    serialText.setError("no serial ac tidak boleh kosong");
                    serialText.findFocus();
                    return;
                }else {
                    serialText.setError(null);
                }
                if (TextUtils.isEmpty(capacityString)) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Pilih Kapasitas AC", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (filepath == null) {
                    Toast.makeText(CustomerRetrofitActivity.this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(sharePreferenceManager.getRetrofitType().equals("MUSICOOL")){
                        setRetrofit();
                    }else {
                        setRetrofitBreezon();
                    }
                }
            }
        });


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


    private void getProvinces() {

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getProvinces();

        province = new ArrayList<>();
        provinceID = new ArrayList<>();
        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

        provinceBetterSpinner.setHint("Mengambil data");
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
                provinceBetterSpinner.setHint("Pilih Provinsi");
                provinceBetterSpinner.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    filepath = result.getUri();
                    //to getbyteOk
                    chooseImage.setImageURI(filepath);
                    InputStream iStream = getContentResolver().openInputStream(filepath);
                    inputData = getBytes(iStream);
                    bitmapImage = BitmapFactory.decodeByteArray(inputData, 0, inputData.length);
                    getFile(bitmapImage);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                chooseImage.setImageBitmap(thumbnail);
            }
        } catch (Exception e) {
            Log.d("ClaimProduct", "onActivityResult: " + e.toString());
            Toast.makeText(CustomerRetrofitActivity.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
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
                cityBetterSpinner.setHint("Pilih Kota/Kabupaten");
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

    private void setRetrofit() {
        displayLoader("Menyimpan data retrofit");
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(CustomerRetrofitActivity.this);
        RetrofitCust retrofit = new RetrofitCust();
        retrofit.setEmail(emailString);
        retrofit.setName(nameString);
        retrofit.setPhone(phoneString);
        retrofit.setAddress(address);
        retrofit.setProvince_id(provinceIDString);
        retrofit.setCity_id(cityIDString);
        retrofit.setDistrict_id(districtIDString);
        retrofit.setZipcode(zipcode);
        retrofit.setIdcard_image(uuid);

//        Log.d("lolfinal", "renewalToken: "+ login);

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        try {

            Call<JsonObject> call = service
                    .custRetrofit("application/json", "Bearer " + sharePreferenceManager.getToken(), retrofit);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    pDialog.dismiss();
                    if (response.code() == 200) {
                        Log.d("lolfinal", "SuccessoTOLOL" + call.toString() + response.body());
                        JsonObject jsonObject = response.body();
                        setUnitRetrofit(jsonObject.get("id").getAsString());
                    } else {
                        Toast.makeText(CustomerRetrofitActivity.this, "Terjadi kesalahan :" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinalTOLOL", "error: " + t.toString());
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.d("lolfinalTOLOL", "uploadError: " + e.toString());
            pDialog.dismiss();
        }
    }

    private void setUnitRetrofit(String id) {
        displayLoader("Menyimpan data retrofit");
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(CustomerRetrofitActivity.this);
        RetrofitUnit unit = new RetrofitUnit();

        unit.setId(getIntent().getStringExtra(INTENT_EXTRA_ID));
        unit.setBrand(brandString);
        unit.setModel(modelString);
        unit.setCapacity(capacityString);
        unit.setCustomer_id(id);
        unit.setSerial_number(serialString);

        Log.d("lolfinal", "setUnitRetrofit: " + unit);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        try {

            Call<JsonObject> call = service
                    .setUnitRetrofit("application/json", "Bearer " + sharePreferenceManager.getToken(), "1", "5", unit);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolfinal", "SuccessoTOLOL" + call.toString() + response.body() + response.code() + response.message());
                    pDialog.dismiss();
                    if (response.code() == 200) {
                        JsonObject jsonObject = response.body();
                        JsonObject pointJson = jsonObject.get("point").getAsJsonObject();
                        String point = pointJson.get("amount").getAsString();
                        Intent intent = new Intent(CustomerRetrofitActivity.this, RewardActivity.class);
                        intent.putExtra(INTENT_EXTRA_AMOUNT, point);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CustomerRetrofitActivity.this, "Terjadi kesalahan : " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinalTOLOL", "error: " + t.toString());
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.d("lolfinalTOLOL", "uploadError: " + e.toString());
            pDialog.dismiss();
        }
    }


    private void setRetrofitBreezon() {
        displayLoader("Menyimpan data retrofit");
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(CustomerRetrofitActivity.this);
        RetrofitCust retrofit = new RetrofitCust();
        retrofit.setEmail(emailString);
        retrofit.setName(nameString);
        retrofit.setPhone(phoneString);
        retrofit.setAddress(address);
        retrofit.setProvince_id(provinceIDString);
        retrofit.setCity_id(cityIDString);
        retrofit.setDistrict_id(districtIDString);
        retrofit.setZipcode(zipcode);
        retrofit.setIdcard_image(uuid);

//        Log.d("lolfinal", "renewalToken: "+ login);

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        try {

            Call<JsonObject> call = service
                    .custRetrofitBreezon("application/json", "Bearer " + sharePreferenceManager.getToken(), retrofit);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    pDialog.dismiss();
                    JsonObject jsonObject = response.body();
                    setUnitRetrofitBreezon(jsonObject.get("id").getAsString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinalTOLOL", "error: " + t.toString());
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.d("lolfinalTOLOL", "uploadError: " + e.toString());
            pDialog.dismiss();
        }
    }

    private void setUnitRetrofitBreezon(String id) {
        displayLoader("Menyimpan data retrofit");
        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(CustomerRetrofitActivity.this);
        RetrofitUnit unit = new RetrofitUnit();

        unit.setId(getIntent().getStringExtra(INTENT_EXTRA_ID));
        unit.setBrand(brandString);
        unit.setModel(modelString);
        unit.setCapacity(capacityString);
        unit.setCustomer_id(id);
        unit.setSerial_number(serialString);

        Log.d("lolfinal", "setUnitRetrofit: " + unit);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        try {

            Call<JsonObject> call = service
                    .setUnitRetrofitBreezon("application/json", "Bearer " + sharePreferenceManager.getToken(), "1", "5", unit);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("lolfinal", "SuccessoTOLOL" + call.toString() + response.body() + response.code() + response.message());
                    pDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(CustomerRetrofitActivity.this, "Berhasil submit retrofit", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CustomerRetrofitActivity.this,MasterActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CustomerRetrofitActivity.this, "Terjadi kesalahan : " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinalTOLOL", "error: " + t.toString());
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.d("lolfinalTOLOL", "uploadError: " + e.toString());
            pDialog.dismiss();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void getFile(Bitmap imageBitmap) {
        file = createTempFile(imageBitmap);
        if (file != null) {
            uploadInformation();
        } else {
            getFile(imageBitmap);
        }
        Log.d("response final", "getFile: " + file + reqFile);
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + "_image.jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();


        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        return file;
    }

    public void uploadInformation() {
        displayLoader("Mengunggah gambar");

        Log.e("lolfinal", "disini");
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {
            Call<JsonObject> call = service
                    .uploadImage(MultipartBody.Part.createFormData("photo", file.getName(), reqFile));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    pDialog.dismiss();
                    Log.d("lolfinal", "Success" + call.toString() + response.body());
                    Log.d("lolfinal", "onResponse - Status : " + response.code() + response.errorBody());
                    JsonObject jsonObject = response.body();
                    uuid = jsonObject.get("uuid").getAsString();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void displayLoader(String message) {
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
