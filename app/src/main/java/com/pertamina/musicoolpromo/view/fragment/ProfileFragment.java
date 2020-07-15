package com.pertamina.musicoolpromo.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.HistoryPoints;
import com.pertamina.musicoolpromo.model.forgotpass.ForgotPass;
import com.pertamina.musicoolpromo.model.profile.UpdateImage;
import com.pertamina.musicoolpromo.model.register.User;
import com.pertamina.musicoolpromo.model.register.UserGoogle;
import com.pertamina.musicoolpromo.view.activity.MainActivity;
import com.pertamina.musicoolpromo.view.activity.MasterActivity;
import com.pertamina.musicoolpromo.view.activity.PointsHistoryActivity;
import com.pertamina.musicoolpromo.view.activity.RedeemPointActivity;
import com.pertamina.musicoolpromo.view.base.BaseFragment;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.GlobalMethod;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_AGENT;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_CUSTOMER;
import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.ACCOUNT_TYPE_OUTLET;

public class ProfileFragment extends BaseFragment {

    private Button logoutButton;
    private TextView changePassword;
    private LinearLayout seePoint;
    private LinearLayout rootLinear;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FrameLayout frameLayout;
    private RelativeLayout changePhoto;

    private EditText nameProfile;
    private EditText outletProfile;
    private EditText numberProfile;
    private EditText addressProfile;
    private EditText zipcodeProfile;
    private EditText ktpProfile;
    private BetterSpinner provinceProfile;
    private BetterSpinner cityProfile;
    private EditText emailProfile;
    private BetterSpinner districtProfile;
    private ImageView imageProfile;

    private TextView saveProfile;
    private TextView editProfile;

    private ArrayAdapter provinceAdapter;
    private ArrayAdapter cityAdapter;
    private ArrayAdapter districtAdapter;

    ArrayList<String> province;
    ArrayList<String> provinceID;
    ArrayList<String> city;
    ArrayList<String> cityID;
    ArrayList<String> district;
    ArrayList<String> districtID;

    private byte[] inputData = null;
    Bitmap bitmapImage;

    private String uuid;
    private SharePreferenceManager sharePreferenceManager;

    private TextView textViewOutlet;
    private TextView textPoint;

    private String provinceStringID;
    private String districttStringID;
    private String cityStringID;

    private String completeProfile;

    private ProgressDialog pDialog;

    private GlobalMethod globalMethod;


    private File file;
    private RequestBody reqFile;


    private Uri filepath = null;
    private static int IMG_CAMERA = 2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
        setupListener(view);
        generateView(view);
    }

    @Override
    public void setUpView(View view) {

        logoutButton = view.findViewById(R.id.logout_button);
        changePassword = view.findViewById(R.id.change_password);
        seePoint = view.findViewById(R.id.see_point);
        nameProfile = view.findViewById(R.id.nameProfile);
        numberProfile = view.findViewById(R.id.phoneProfile);
        addressProfile = view.findViewById(R.id.addressProfile);
        outletProfile = view.findViewById(R.id.outletProfile);
        provinceProfile = view.findViewById(R.id.provinceProfile);
        cityProfile = view.findViewById(R.id.cityProfile);
        emailProfile = view.findViewById(R.id.emailProfile);
        imageProfile = view.findViewById(R.id.imageProfile);
        rootLinear = view.findViewById(R.id.rootLinear);
        textViewOutlet = view.findViewById(R.id.name_text);
        textPoint = view.findViewById(R.id.profile_point);
        districtProfile = view.findViewById(R.id.districtProfile);
        zipcodeProfile = view.findViewById(R.id.zipcodeProfile);
        frameLayout = view.findViewById(R.id.root);
        editProfile = view.findViewById(R.id.edit_profile);
        saveProfile = view.findViewById(R.id.save_profile);
        changePhoto = view.findViewById(R.id.change_photo);
        ktpProfile = view.findViewById(R.id.ktpProfile);

        sharePreferenceManager = new SharePreferenceManager(Objects.requireNonNull(getContext()));
        globalMethod = new GlobalMethod();

        formIsEnable(false);

    }

    @Override
    public void generateView(View view) {

        checkAccount();
    }

    @Override
    public void setupListener(View view) {

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), sharePreferenceManager.getEmail(), Toast.LENGTH_SHORT).show();
                setSendForgotPassword(sharePreferenceManager.getEmail());
            }
        });

        seePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_AGENT) || sharePreferenceManager.getAccountType().equals(ACCOUNT_TYPE_OUTLET)){
                    Intent intent = new Intent(getContext(), PointsHistoryActivity.class);
                    startActivity(intent);
                }else{
                  Intent intent = new Intent(getContext(), RedeemPointActivity.class);
                  startActivity(intent);
                }
            }
        });

        provinceProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtProfile.setText("");
                cityProfile.setText("");
                getCities(provinceID.get(i));
                provinceStringID = provinceID.get(i);
            }
        });

        cityProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtProfile.setText("");
                getDistricts(cityID.get(i));
                cityStringID = cityID.get(i);
            }
        });

        districtProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districttStringID = districtID.get(i);
            }
        });

        cityProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.size() == 0) {
                    Toast.makeText(getContext(), R.string.choose_province_first, Toast.LENGTH_SHORT).show();
                }
            }
        });

        districtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (district.size() == 0) {
                    Toast.makeText(getContext(), R.string.choose_city_first, Toast.LENGTH_SHORT).show();
                }
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile.setVisibility(View.VISIBLE);
                editProfile.setVisibility(View.GONE);
                getProvinces();
                formIsEnable(true);
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile.setVisibility(View.GONE);
                editProfile.setVisibility(View.VISIBLE);
                formIsEnable(false);
                updateProfile();
            }
        });

        ProfileFragment profileFragment = new ProfileFragment();

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                        .start(getContext(), ProfileFragment.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    filepath = result.getUri();
                    Log.d("gambartotlol", "onActivityResult: " + filepath);
                    imageProfile.setImageURI(filepath);
                    InputStream iStream = getActivity().getContentResolver().openInputStream(filepath);
                    inputData = getBytes(iStream);
                    bitmapImage = BitmapFactory.decodeByteArray(inputData, 0, inputData.length);
                    if (!sharePreferenceManager.getAccountID().equals("CUST")) {
                        getFile(bitmapImage);
                    }else {
                        updateProfile();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.d("gambartotlol", "onActivityResult: " + error);
                    Toast.makeText(getContext(), "LOLL" + error, Toast.LENGTH_SHORT).show();

                }
            } else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imageProfile.setImageBitmap(thumbnail);
            }
        } catch (Exception e) {
            Log.d("ClaimProduct", "onActivityResult: " + e.toString());
            Toast.makeText(getContext(), "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }


    private void checkAccount() {
        if (sharePreferenceManager.getAccountID().equals(ACCOUNT_TYPE_CUSTOMER)) {

            rootLinear.setVisibility(View.GONE);
            textViewOutlet.setVisibility(View.GONE);
            outletProfile.setVisibility(View.GONE);
            changePassword.setVisibility(View.GONE);

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            getUserCust();
        } else {
            editProfile.setVisibility(View.GONE);
            getUser(sharePreferenceManager.getToken());
        }
    }


    private void getUserCust() {
        Log.d("profilpref", "getUserCust: " + sharePreferenceManager.getEmail());
        if (sharePreferenceManager.getEmail() != null) {
            nameProfile.setText(sharePreferenceManager.getName());
            emailProfile.setText(sharePreferenceManager.getEmail());
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(sharePreferenceManager.getImage())
                    .into(imageProfile);
            numberProfile.setText(sharePreferenceManager.getPhone());
            addressProfile.setText(sharePreferenceManager.getAddress());
            zipcodeProfile.setText(sharePreferenceManager.getZipcode());
            provinceProfile.setText(sharePreferenceManager.getProvince());
            cityProfile.setText(sharePreferenceManager.getCity());
            districtProfile.setText(sharePreferenceManager.getDistrict());
            completeProfile = sharePreferenceManager.getComplete();
            ktpProfile.setText(sharePreferenceManager.getKtp());
            districttStringID = sharePreferenceManager.getDistrictID();
            provinceStringID = sharePreferenceManager.getProvinceID();
            cityStringID = sharePreferenceManager.getCityID();
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserGoogle userGoogle = dataSnapshot.getValue(UserGoogle.class);
                    try {
                        nameProfile.setText(userGoogle.getName());
                        emailProfile.setText(userGoogle.getEmail());
                        completeProfile = userGoogle.getCompleteProfile();
                        Glide.with(Objects.requireNonNull(getContext()))
                                .load(userGoogle.getImageProfile())
                                .into(imageProfile);
                        numberProfile.setText(userGoogle.getPhoneNumber());
                        addressProfile.setText(userGoogle.getAddress());
                        zipcodeProfile.setText(userGoogle.getKodepos());
                        provinceProfile.setText(userGoogle.getProvince());
                        cityProfile.setText(userGoogle.getCity());
                        districtProfile.setText(userGoogle.getDistrict());

                        districttStringID = userGoogle.getDistrictID();
                        provinceStringID = userGoogle.getProvinceID();
                        cityStringID = userGoogle.getCityID();

                        sharePreferenceManager.setName(userGoogle.getName());
                        sharePreferenceManager.setPhone(userGoogle.getPhoneNumber());
                        sharePreferenceManager.setProvince(userGoogle.getProvince());
                        sharePreferenceManager.setCity(userGoogle.getCity());
                        sharePreferenceManager.setDistrict(userGoogle.getDistrict());
                        sharePreferenceManager.setAddress(userGoogle.getAddress());
                        sharePreferenceManager.setZipcode(userGoogle.getKodepos());
                        sharePreferenceManager.setImage(userGoogle.getImageProfile());
                        sharePreferenceManager.setEmail(userGoogle.getEmail());
                        sharePreferenceManager.setCompleteProfile(userGoogle.getCompleteProfile());
                        if(userGoogle.getKtp() != null){
                            ktpProfile.setText(userGoogle.getKtp());
                            sharePreferenceManager.setKtp(userGoogle.getKtp());
                        }


                    } catch (NullPointerException e) {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void logOut() {

        if (sharePreferenceManager.getAccountType().equals("CUST")) {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(getContext());
                    sharePreferenceManager.getAccountID();
                    sharePreferenceManager.removeName();
                    sharePreferenceManager.removeEmail();
                    sharePreferenceManager.removePhone();
                    sharePreferenceManager.removeAccountID();
                    sharePreferenceManager.removeAccount();
                    sharePreferenceManager.removeProvince();
                    sharePreferenceManager.removeCity();
                    sharePreferenceManager.removeDistrict();
                    sharePreferenceManager.removeImage();
                    sharePreferenceManager.removeAddress();
                    sharePreferenceManager.removeKtp();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
            sharePreferenceManager.removeToken();
            sharePreferenceManager.removeAccount();
            sharePreferenceManager.removeEmail();
            sharePreferenceManager.removePassword();
            sharePreferenceManager.removeProfile();
            getActivity().finish();
        }
    }


    private void getUser(String token) {
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

            Call<JsonObject> call = service.checkAccount("Bearer " + token);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        if(response.code() == 200) {
                            JsonObject jsonObject = response.body();
                            String email = jsonObject.get("email").getAsString();
                            String point = jsonObject.get("point").getAsString();
                            JsonObject accoutDetail = jsonObject.get("account_detail").getAsJsonObject();
                            String personName = accoutDetail.get("person_name").getAsString();
                            String phone = accoutDetail.get("phone").getAsString();
                            String address = accoutDetail.get("address").getAsString();
                            JsonObject province = accoutDetail.get("province").getAsJsonObject();
                            JsonObject city = accoutDetail.get("city").getAsJsonObject();
                            String provinceString = province.get("name").getAsString();
                            String cityString = city.get("city").getAsString();
                            JsonObject district = accoutDetail.get("district").getAsJsonObject();
                            String districtString = district.get("sub_district").getAsString();
                            String facilityname = accoutDetail.get("facility_name").getAsString();
                            String zipcodeString = accoutDetail.get("zipcode").getAsString();
                            JsonObject avatar = jsonObject.get("avatar_img").getAsJsonObject();
                            String avatarString = avatar.get("url").getAsString();

                            emailProfile.setText(email);
                            nameProfile.setText(personName);
                            numberProfile.setText(phone);
                            provinceProfile.setText(provinceString);
                            addressProfile.setText(address);
                            cityProfile.setText(cityString);
                            zipcodeProfile.setText(zipcodeString);
                            outletProfile.setText(facilityname);
                            textPoint.setText(point);
                            districtProfile.setText(districtString);

                            Glide.with(getContext())
                                    .load(avatarString)
                                    .into(imageProfile);
                        }else if(response.code()==401){
                            Toast.makeText(getContext(), "Sesi anda telah habis silahkan login ulang", Toast.LENGTH_SHORT).show();
                            sharePreferenceManager.removeToken();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    } catch (Exception e) {
                        Log.d("PANTEK", "onResponse: " + e.toString());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lol gagal", "onResponse: " + t);
                }
            });
//        }

    }

    private void setSendForgotPassword(String email) {

        ForgotPass forgotPass = new ForgotPass();
        forgotPass.setEmail(email);
        Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
        GlobalMethod globalMethod = new GlobalMethod();

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {
            Call<JsonObject> call = service
                    .forgotPassword("application/json", forgotPass);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("password", "Successo" + call.toString() + response.body());
                    User user = null;

                    if (response.code() == 200) {
                        globalMethod.snackBar(frameLayout, "Konfigurasi password telah dikirim ke email anda");
                    } else if (response.code() == 400) {
                        try {
                            Log.d("lolfinal", "onResponse - Status : " + response.code() + response.errorBody().string() + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
        }
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

        provinceProfile.setHint(R.string.wait_data);
        provinceProfile.setEnabled(false);

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
                provinceProfile.setHint(R.string.choose_province);
                provinceProfile.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
        setSpinnerAdapter();
    }

    private void getCities(String id) {
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);
        Call<JsonArray> call = service.getCities(id, "1", "0");

        city = new ArrayList<>();
        cityID = new ArrayList<>();
        district = new ArrayList<>();
        districtID = new ArrayList<>();

        cityProfile.setHint(R.string.wait_data);
        cityProfile.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("city").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    Log.d("lol", "onResponse: " + result);
                    city.add(result);
                    cityID.add(resultID);
                }
                cityProfile.setHint(R.string.choose_city);
                cityProfile.setEnabled(true);
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
        districtProfile.setHint(R.string.wait_data);
        districtProfile.setEnabled(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String result = jsonObject.get("sub_district").getAsString();
                    String resultID = jsonObject.get("id").getAsString();
                    district.add(result);
                    districtID.add(resultID);
                }
                districtProfile.setHint(R.string.choose_district);
                districtProfile.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
        setSpinnerAdapter();
    }

    private void setSpinnerAdapter() {

        provinceAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, province);

        provinceProfile.setAdapter(provinceAdapter);

        cityAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, city);

        cityProfile.setAdapter(cityAdapter);

        districtAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, district);

        districtProfile.setAdapter(districtAdapter);
    }

    private void updateImageProfile() {

        Log.d("lolfinal", "updateImageProfile: tolol");
        UpdateImage updateImage = new UpdateImage();
        updateImage.setAvatar_image(uuid);
        updateImage.setId(sharePreferenceManager.getAccountID());
        GlobalMethod globalMethod = new GlobalMethod();

        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        try {
            Call<JsonObject> call = service
                    .updateImage("application/json", "Bearer " + sharePreferenceManager.getToken(), updateImage);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("password", "Successo" + call.toString() + response.code());
                    User user = null;
                    if (response.code() == 200) {
                        globalMethod.snackBar(frameLayout, "Gambar telah di perbarui");
                        update();
                    } else if (response.code() == 400) {
                        try {
                            Log.d("lolfinalprofile", "onResponse - Status : " + response.code() + response.errorBody().string() + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lolfinal", "error: " + t.toString());
                }
            });
        } catch (Exception e) {
        }
    }

    private void updateProfile() {

        final HashMap<String, Object> user = new HashMap<>();
        final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        final StorageReference ref = storageReference.child("users/" + UUID.randomUUID().toString());
        String nameString = nameProfile.getText().toString();
        String addressString = addressProfile.getText().toString();
        String provinceString = provinceProfile.getText().toString();
        String cityString = cityProfile.getText().toString();
        String districtString = districtProfile.getText().toString();
        String numberString = numberProfile.getText().toString();
        String kodeString = zipcodeProfile.getText().toString();
        String emailString = emailProfile.getText().toString();
        String ktpString = ktpProfile.getText().toString();


        if (filepath == null) {
            user.put("phoneNumber", numberString);
            user.put("iduser", currentUser.getUid());
            user.put("name", nameString);
            user.put("email", emailString);
            user.put("gender", "l");
            user.put("province", provinceString);
            user.put("city", cityString);
            user.put("district", districtString);
            user.put("address", addressString);
            user.put("kodepos", kodeString);
            user.put("provinceID", provinceStringID);
            user.put("districtID", districttStringID);
            user.put("cityID", cityStringID);
            user.put("ktp", ktpString);
            user.put("imageProfile", sharePreferenceManager.getImage());

            sharePreferenceManager.setName(nameString);
            sharePreferenceManager.setPhone(numberString);
            sharePreferenceManager.setProvince(provinceString);
            sharePreferenceManager.setCity(cityString);
            sharePreferenceManager.setDistrict(districtString);
            sharePreferenceManager.setAddress(addressString);
            sharePreferenceManager.setZipcode(kodeString);
            sharePreferenceManager.setImage(String.valueOf(currentUser.getPhotoUrl()));
            sharePreferenceManager.setEmail(emailString);
            sharePreferenceManager.setKtp(ktpString);
            sharePreferenceManager.setProvinceID(provinceStringID);
            sharePreferenceManager.setCityID(cityStringID);
            sharePreferenceManager.setDistrictID(districttStringID);

            if (TextUtils.isEmpty(addressString) || TextUtils.isEmpty(numberString) || TextUtils.isEmpty(kodeString)
                    || TextUtils.isEmpty(kodeString) || TextUtils.isEmpty(provinceString) ||TextUtils.isEmpty(cityString ) || TextUtils.isEmpty(districtString) || TextUtils.isEmpty(ktpString))
            {
                user.put("completeProfile", completeProfile);
                sharePreferenceManager.setCompleteProfile(completeProfile);
                dbf.setValue(user);
            } else {
                user.put("completeProfile", "true");
                sharePreferenceManager.setCompleteProfile("true");
                dbf.setValue(user);
            }
            Toast.makeText(getContext(), "Perubahan tersimpan", Toast.LENGTH_SHORT).show();
            ((MasterActivity)getActivity()).getBadgeView();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            UploadTask uploadTask = ref.putFile(filepath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {final
                        String urlGambar = uri.toString();
                            user.put("phoneNumber", numberString);
                            user.put("iduser", currentUser.getUid());
                            user.put("name", nameString);
                            user.put("email", emailString);
                            user.put("gender", "l");
                            user.put("province", provinceString);
                            user.put("city", cityString);
                            user.put("district", districtString);
                            user.put("address", addressString);
                            user.put("kodepos", kodeString);
                            user.put("provinceID", provinceStringID);
                            user.put("districtID", districttStringID);
                            user.put("cityID", cityStringID);
                            user.put("imageProfile", urlGambar);
                            user.put("ktp", ktpString);


                            sharePreferenceManager.setName(nameString);
                            sharePreferenceManager.setPhone(numberString);
                            sharePreferenceManager.setProvince(provinceString);
                            sharePreferenceManager.setCity(cityString);
                            sharePreferenceManager.setDistrict(districtString);
                            sharePreferenceManager.setAddress(addressString);
                            sharePreferenceManager.setZipcode(kodeString);
                            sharePreferenceManager.setImage(urlGambar);
                            sharePreferenceManager.setEmail(emailString);
                            sharePreferenceManager.setKtp(ktpString);
                            sharePreferenceManager.setProvinceID(provinceStringID);
                            sharePreferenceManager.setCityID(cityStringID);
                            sharePreferenceManager.setDistrictID(districttStringID);

                            if (TextUtils.isEmpty(addressString) || TextUtils.isEmpty(numberString) || TextUtils.isEmpty(kodeString)
                                    || TextUtils.isEmpty(kodeString) || TextUtils.isEmpty(provinceString) ||TextUtils.isEmpty(cityString ) || TextUtils.isEmpty(districtString) || TextUtils.isEmpty(ktpString))
                            {
                                user.put("completeProfile", completeProfile);
                                sharePreferenceManager.setCompleteProfile(completeProfile);
                                dbf.setValue(user);
                            } else {
                                user.put("completeProfile", "true");
                                sharePreferenceManager.setCompleteProfile("true");
                                dbf.setValue(user);
                            }
                            Toast.makeText(getContext(), "Perubahan tersimpan ", Toast.LENGTH_SHORT).show();
                            ((MasterActivity)getActivity()).getBadgeView();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    globalMethod.snackBar(frameLayout, "Gagal mengunggah profile");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                }
            });
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
                    if (response.code() == 200) {
                        JsonObject jsonObject = response.body();
                        uuid = jsonObject.get("uuid").getAsString();
                        updateImageProfile();
                    } else if (response.code() == 413) {
                        globalMethod.snackBar(frameLayout, "Gambar terlalu besar");
                    } else {
                        globalMethod.snackBar(frameLayout, response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            pDialog.dismiss();
            Toast.makeText(getContext(), "Registrasi gagal coba lagi !", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onStart() {
        if(sharePreferenceManager.getAccountType().equals("CUST")){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
            super.onStart();
        }else {
            super.onStart();
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

    private void displayLoader(String message) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
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

    private void formIsEnable(Boolean isEnable) {
        nameProfile.setEnabled(isEnable);
        ktpProfile.setEnabled(isEnable);
        numberProfile.setEnabled(isEnable);
        addressProfile.setEnabled(isEnable);
        nameProfile.setEnabled(isEnable);
        provinceProfile.setEnabled(isEnable);
        provinceProfile.setClickable(isEnable);
        cityProfile.setEnabled(isEnable);
        cityProfile.setClickable(isEnable);
        districtProfile.setEnabled(isEnable);
        cityProfile.setClickable(isEnable);
        zipcodeProfile.setClickable(isEnable);
        zipcodeProfile.setEnabled(isEnable);
    }

    private void update() {
        ApiInterface service = ApiClient.getData().create(ApiInterface.class);

        Call<JsonObject> call = service.checkAccount("Bearer " + sharePreferenceManager.getToken());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    String accountType = jsonObject.get("account_type").getAsString();
                    sharePreferenceManager.setAccount(accountType);
                    sharePreferenceManager.setProfile(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("PANTEK", "onResponse: " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("lol gagal", "onResponse: " + t);
            }
        });
    }
}
