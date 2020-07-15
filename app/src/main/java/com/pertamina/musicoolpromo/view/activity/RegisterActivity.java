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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.register.User;
import com.pertamina.musicoolpromo.model.register.UserDetail;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.GlobalMethod;
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

public class RegisterActivity extends BaseActivity {

    private EditText username;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText retypepassword;
    private TextInputEditText zipText;
    private TextInputEditText phoneNumber;
    private TextInputEditText address;
    private TextInputEditText company;
    private TextInputEditText fileText;
    private TextView textCaution;
    private BetterSpinner cityBetterSpinner;
    private BetterSpinner typeAccountBetterSpinner;
    private BetterSpinner genderBetterSpinner;
    private BetterSpinner provinceBetterSpinner;
    private BetterSpinner districtBetterSpinner;
    private Button registerButton;
    private ProgressDialog pDialog;
    private FirebaseAuth mFirebaseAuth;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> districtAdapter;

    private byte[] inputData = null;
    Bitmap bitmapImage;

    private Button buttonChoose;
    private TextView chooseButton;

    private File file;
    private RequestBody reqFile;


    private Uri filepath = null;
    private static int IMG_CAMERA = 2;

    private String[] gender = {
            "Laki-Laki",
            "Perempuan"
    };

    private String[] type = {
            "Agen",
            "Outlet",
            "Teknisi"
    };

    private String uuid;

    private String usernameString;
    private String emailString;
    private String passwordString;
    private String retypepasswordString;
    private String accountTypeString;
    private String genderString;
    private String phoneString;
    private String provinceString;
    private String cityString;
    private String districtString;
    private String addressString;
    private String zipcodeString;
    private String photoString;
    private String companyString;
    private String provinceStringID;
    private String districttStringID;
    private String cityStringID;

    private RelativeLayout relativeLayout;

    ArrayList<String> province;
    ArrayList<String> provinceID;
    ArrayList<String> city;
    ArrayList<String> cityID;
    ArrayList<String> district;
    ArrayList<String> districtID;


    GlobalMethod globalMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpView();
    }

    @Override
    public void setUpView() {

        filepath = null;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Editext
        username = (EditText) findViewById(R.id.usernameText);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        retypepassword = findViewById(R.id.retype_password);
        buttonChoose = findViewById(R.id.button_choose);
        chooseButton = findViewById(R.id.choose_image);
        chooseButton.setHint("Pilih Foto");
        zipText = findViewById(R.id.zipcode_spinner);
        textCaution = findViewById(R.id.text_caution);

        registerButton = findViewById(R.id.register_button);
        relativeLayout = findViewById(R.id.root);

        provinceBetterSpinner = findViewById(R.id.province_spinner);
        cityBetterSpinner = findViewById(R.id.city_spinner);
        districtBetterSpinner = findViewById(R.id.district_spinner);
        zipText = findViewById(R.id.zipcode_spinner);
        typeAccountBetterSpinner = findViewById(R.id.type_spinner);
        genderBetterSpinner = findViewById(R.id.genderSpinner);
        phoneNumber = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        company = findViewById(R.id.facility);

        city = new ArrayList<>();
        cityID = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance();

        globalMethod = new GlobalMethod();
        getProvinces();
        generateView();
        setupListener();
    }


    @Override
    public void generateView() {

        cityBetterSpinner.setHint("Kota/Kabupaten");
        districtBetterSpinner.setHint("Kecamatan");

        final ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, gender);

        genderBetterSpinner.setAdapter(genderAdapter);

        final ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, type);

        typeAccountBetterSpinner.setAdapter(typeAdapter);

    }

    @Override
    public void setupListener() {
        //register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkRegisterForm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //spinner on click
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
                districttStringID= districtID.get(i);
            }
        });

        typeAccountBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextInputLayout genderTIL = (TextInputLayout) findViewById(R.id.textGender);
                if (i == 2) {
                    genderBetterSpinner.setVisibility(View.VISIBLE);
                    genderTIL.setVisibility(View.VISIBLE);
                    textCaution.setVisibility(View.VISIBLE);
                    chooseButton.setHint("Unggah foto sertifikasi pelatihan");
                } else {
                    genderBetterSpinner.setVisibility(View.GONE);
                    genderTIL.setVisibility(View.GONE);
                    textCaution.setVisibility(View.GONE);
                    chooseButton.setHint("Unggah foto toko");
                }
            }
        });


        provinceBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cityBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.size() == 0) {
                    Toast.makeText(RegisterActivity.this, "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        districtBetterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (district.size() == 0) {
                    Toast.makeText(RegisterActivity.this, "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).
                        start(RegisterActivity.this);
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).
                        start(RegisterActivity.this);
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    private void registerAccount(String validate) throws IOException {

        displayLoader("Mendaftarkan Akun...");
        String accountCode;

        if (accountTypeString.equals("Agen")) {
            accountCode = "AGN";
        } else if (accountTypeString.equals("Outlet")) {
            accountCode = "OTL";
        } else {
            accountCode = "TCH";
        }

        String genderCode;
        if (genderString.equals("Perempuan")) {
            genderCode = "p";
        } else {
            genderCode = "l";
        }


        UserDetail userDetail = new UserDetail();
        userDetail.setFacility_name(companyString);
        userDetail.setPerson_name(usernameString);
        userDetail.setGender(genderCode);
        userDetail.setPhone(phoneString);
        userDetail.setProvince_id(provinceStringID);
        userDetail.setCity_id(cityStringID);
        userDetail.setDistrict_id(districttStringID);
        userDetail.setAddress(addressString);
        userDetail.setZipcode(zipcodeString);

        User user = new User();
        user.setEmail(emailString);
        user.setPassword(passwordString);
        user.setRepeat_password(retypepasswordString);
        user.setAccount_type(accountCode);
        user.setValidation_image(validate);
        user.setAvatar_image(validate);
        user.setDetails(userDetail);
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {

            Call<JsonObject> call = service
                    .registerAccount("application/json", user);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    pDialog.dismiss();
                    if(response.code() == 200){
                        intentEmailVerification();
                    }else if (response.code() == 500 ){
                        globalMethod.snackBar(relativeLayout,"Email anda sudah terdaftar");
                    }else {
                        globalMethod.snackBar(relativeLayout,"Email anda sudah terdaftar");
                    }
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

    private void checkRegisterForm() throws IOException {

        usernameString = username.getText().toString().trim();
        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        retypepasswordString = retypepassword.getText().toString().trim();
        accountTypeString = typeAccountBetterSpinner.getText().toString().trim();
        genderString = genderBetterSpinner.getText().toString().trim();
        phoneString = phoneNumber.getText().toString().trim();
        provinceString = provinceBetterSpinner.getText().toString().trim();
        cityString = cityBetterSpinner.getText().toString().trim();
        districtString = districtBetterSpinner.getText().toString().trim();
        addressString = address.getText().toString().trim();
        zipcodeString = zipText.getText().toString().trim();
        photoString = chooseButton.getText().toString().trim();
        companyString = company.getText().toString().trim();

        TextInputLayout userTIL = (TextInputLayout) findViewById(R.id.textInputLayout2);
        TextInputLayout emailTIL = (TextInputLayout) findViewById(R.id.textInputLayout3);
        TextInputLayout passwordTIL = (TextInputLayout) findViewById(R.id.textInputLayout4);
        TextInputLayout retypepasswordTIL = (TextInputLayout) findViewById(R.id.textInputLayoutPass);
        TextInputLayout accountTIL = (TextInputLayout) findViewById(R.id.textInputLayout6);
        TextInputLayout genderTIL = (TextInputLayout) findViewById(R.id.textGender);
        TextInputLayout phoneTIL = (TextInputLayout) findViewById(R.id.textLayoutPhone);
        TextInputLayout provinceTIL = (TextInputLayout) findViewById(R.id.textLayoutProvince);
        TextInputLayout cityTIL = (TextInputLayout) findViewById(R.id.textLayoutCity);
        TextInputLayout districtTIL = (TextInputLayout) findViewById(R.id.textLayoutDistrict);
        TextInputLayout addressTIL = (TextInputLayout) findViewById(R.id.textAddress);
        TextInputLayout zipTIL = (TextInputLayout) findViewById(R.id.textLayoutZipCode);
        TextInputLayout chooseTIL = (TextInputLayout) findViewById(R.id.textInputLayoutImage);
        TextInputLayout companyTIL = (TextInputLayout) findViewById(R.id.textInputFacility);


        if (TextUtils.isEmpty(usernameString)) {
            userTIL.setError("Username tidak boleh kosong");
            userTIL.findFocus();
            return;
        }else {
            userTIL.setError(null);
        }

        if (TextUtils.isEmpty(accountTypeString)) {
            accountTIL.setError("Jenis Akun tidak boleh kosong");
            accountTIL.findFocus();
            return;
        } else {
            accountTIL.setError(null);
        }


        if (genderString.equals("Teknisi")) {
            if (TextUtils.isEmpty(genderString)) {
                genderTIL.setError("Jenis kelamin tidak boleh kosong");
                genderTIL.findFocus();
                return;
            } else {
                genderTIL.setError(null);
            }
        }

        if(TextUtils.isEmpty(companyString)){
            companyTIL.setError("Nama agen/outlet tidak boleh kosong. Isi pekerja lepas jika anda tidak terikat dengan perusahaan (Teknisi)");
            companyTIL.findFocus();
            return;
        }else {
            companyTIL.setError(null);
        }


        if (TextUtils.isEmpty(emailString)) {
            emailTIL.setError("Alamat email tidak boleh kosong");
            emailTIL.findFocus();
            return;
        } else {
            emailTIL.setError(null);
        }


        if (!isEmailValid(emailString)) {
            emailTIL.setError("Alamat email tidak valid");
            emailTIL.findFocus();
            return;
        } else {
            emailTIL.setError(null);
        }



        if (TextUtils.isEmpty(provinceString)) {
            provinceTIL.setError("Provinsi tidak boleh kosong");
            provinceTIL.findFocus();
            return;
        } else {
            provinceTIL.setError(null);
        }

        if (TextUtils.isEmpty(cityString)) {
            cityTIL.setError("Kota Provinsi tidak boleh kosong");
            cityTIL.findFocus();
            return;
        } else {
            cityTIL.setError(null);
        }

        if (TextUtils.isEmpty(districtString)) {
            districtTIL.setError("Kecamatan tidak boleh kosong");
            districtTIL.findFocus();
            return;
        }else{
            districtTIL.setError(null);
          }


        if (TextUtils.isEmpty(zipcodeString)) {
            zipTIL.setError("Kode pos tidak boleh kosong");
            zipTIL.findFocus();
            return;
        }else {
            zipTIL.setError(null);
        }


        if (TextUtils.isEmpty(addressString)) {
            addressTIL.setError("Alamat tidak boleh kosong");
            addressTIL.findFocus();
            return;
        }else{
            addressTIL.setError(null);
        }

        if (TextUtils.isEmpty(phoneString)) {
            phoneTIL.setError("No hp tidak boleh kosong");
            phoneTIL.findFocus();
            return;
        } else {
            phoneTIL.setError(null);
        }


        if (passwordString.length() < 6) {
            passwordTIL.setError("Kata sandi tidak boleh kurang dari 6 karakter");
            passwordTIL.findFocus();
            return;
        } else {
            passwordTIL.setError(null);
        }


        if(filepath == null)
        {
            chooseTIL.setError("Pilih gambar terlebih dahulu");
            chooseTIL.findFocus();
            return;
        }else {
            chooseTIL.setError(null);
        }


        if (!retypepasswordString.equals(passwordString)) {
            retypepasswordTIL.setError("Kata sandi tidak sama");
            retypepasswordTIL.findFocus();
            return;
        } else {
            retypepasswordTIL.setError(null);
            registerAccount(uuid);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    filepath = result.getUri();
                    chooseButton.setText("Foto telah terpilih");
                    //to getbyteOk
                    InputStream iStream = getContentResolver().openInputStream(filepath);
                    inputData = getBytes(iStream);
                    bitmapImage = BitmapFactory.decodeByteArray(inputData, 0, inputData.length);
                    //generate to Bitmap
                    getFile(bitmapImage);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            }
        } catch (Exception e) {
            Log.d("ClaimProduct", "onActivityResult: " + e.toString());
            Toast.makeText(RegisterActivity.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
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
                    if(response.code() == 200){
                        JsonObject jsonObject = response.body();
                        uuid = jsonObject.get("uuid").getAsString();
                    } else if(response.code() == 413 ){
                        globalMethod.snackBar(relativeLayout,"Gambar terlalu besar");
                    } else {
                        globalMethod.snackBar(relativeLayout,response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            pDialog.dismiss();
            Toast.makeText(this, "Registrasi gagal coba lagi !", Toast.LENGTH_SHORT).show();
        }
    }

    private void intentEmailVerification (){
        Intent intent = new Intent(RegisterActivity.this,EmailVerificationActivity.class);
        intent.putExtra("emailverification",R.string.emailverif);
        startActivity(intent);
        finish();
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

    private void displayLoader(String message) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getFile(Bitmap imageBitmap) {
        file = createTempFile(imageBitmap);
        if(file!= null){
            uploadInformation();
        }else {
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


}
