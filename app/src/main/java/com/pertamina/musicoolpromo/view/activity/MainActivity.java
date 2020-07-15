package com.pertamina.musicoolpromo.view.activity;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukesh.OtpView;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.register.UserGoogle;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    Button signInButton;
    private static final int RC_SIGN = 9001;
    private static final String TAG = "Google Sign";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog pDialog;
    private GoogleApiClient googleApiClient;
    private SharePreferenceManager sharePreferenceManager;

    private String phoneNumber = "";
    private String name = "";
    private String gender = "";
    private String address = "";
    private String email = "";
    private String province = "";
    private String city = "";
    private String district = "";
    private String image = "";
    private String kodepos = "";
    private String imageProfile = "";
    private String completeProfile = "";

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onStart() {
        super.onStart();
        sharePreferenceManager = new SharePreferenceManager(MainActivity.this);
        if(sharePreferenceManager.getToken()!= null){
            updateUI(null);
        }
        if(mFirebaseAuth.getCurrentUser()!= null){
            updateUI(mFirebaseAuth.getCurrentUser());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();


    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    public void setUpView() {
        //button google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "You Have An Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mFirebaseAuth=FirebaseAuth.getInstance();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mFirebaseAuth = FirebaseAuth.getInstance();

        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                signInwithGoogle();
                showDialog();
            }
        });

        TextView textTnC = findViewById(R.id.termandconditions);
        textTnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TermConditionActivity.class);
                startActivity(intent);
            }
        });

        TextView textPrivacy = findViewById(R.id.privacypolicy);
        textPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void generateView() {

    }

    @Override
    public void setupListener() {

    }

    protected void signInwithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google Sign Failed because: ", e);
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.login_pop_up,
                null);

        LinearLayout custLogin = mView.findViewById(R.id.customersignIn);
        LinearLayout adminLogin = mView.findViewById(R.id.adminSignIn);

        custLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInwithGoogle();
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: "+acct.getId());
        displayLoader();
        AuthCredential credential= GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //jika sign succes update ui
                            Log.d(TAG, "onComplete: success");
                            sharePreferenceManager.setAccountID("CUST");
                            sharePreferenceManager.setAccount("CUST");

                            final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                            final HashMap<String, Object> user= new HashMap<>();
                            imageProfile = currentUser.getPhotoUrl().toString();
                            name = currentUser.getDisplayName();
                            email = currentUser.getEmail();
                            Log.d(TAG, "googleid: " + currentUser.getUid());
                            completeProfile = "false";

                            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Intent pindah = new Intent(MainActivity.this,MasterActivity.class);
                                        UserGoogle userGoogle = dataSnapshot.getValue(UserGoogle.class);
                                        sharePreferenceManager.setName(userGoogle.getName());
                                        sharePreferenceManager.setPhone(userGoogle.getPhoneNumber());
                                        sharePreferenceManager.setProvince(userGoogle.getProvince());
                                        sharePreferenceManager.setCity(userGoogle.getCity());
                                        sharePreferenceManager.setDistrict(userGoogle.getDistrict());
                                        sharePreferenceManager.setAddress(userGoogle.getAddress());
                                        sharePreferenceManager.setZipcode(userGoogle.getKodepos());
                                        sharePreferenceManager.setImage(String.valueOf(currentUser.getPhotoUrl()));
                                        sharePreferenceManager.setEmail(email);
                                        sharePreferenceManager.setCompleteProfile(userGoogle.getCompleteProfile());
                                        startActivity(pindah);
                                    } else {
                                        user.put("phoneNumber",phoneNumber);
                                        user.put("iduser",currentUser.getUid());
                                        user.put("name",name);
                                        user.put("email",email);
                                        user.put("gender",gender);
                                        user.put("province",province);
                                        user.put("city",city);
                                        user.put("district",district);
                                        user.put("address",address);
                                        user.put("kodepos",kodepos);
                                        user.put("completeProfile",completeProfile);
                                        user.put("imageProfile",imageProfile);
                                        dbf.setValue(user);

                                        sharePreferenceManager.setName(name);
                                        sharePreferenceManager.setPhone(phoneNumber);
                                        sharePreferenceManager.setProvince(province);
                                        sharePreferenceManager.setCity(city);
                                        sharePreferenceManager.setDistrict(district);
                                        sharePreferenceManager.setAddress(address);
                                        sharePreferenceManager.setZipcode(kodepos);
                                        sharePreferenceManager.setImage(String.valueOf(currentUser.getPhotoUrl()));
                                        sharePreferenceManager.setEmail(email);
                                        sharePreferenceManager.setCompleteProfile(completeProfile);

                                        updateUI(currentUser);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.w(TAG, "onFailure: ", task.getException() );
                                }
                            });
                        }else {
                            Log.w(TAG, "onFailure: ", task.getException() );
                            updateUI(null);
                        }
                    }
                });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Memverifikasi Akun...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    private void updateUI(FirebaseUser fuser) {
        try {
            if(fuser != null) {
                Intent intent = new Intent(MainActivity.this, MasterActivity.class);
                startActivity(intent);
                finish();
            }else if(sharePreferenceManager.getToken() != null)
            {
                if(sharePreferenceManager.getAccountType().equals("DPT")) {
                    Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(MainActivity.this, MasterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "updateUI: ", e);
        }
    }


}
