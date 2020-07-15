package com.pertamina.musicoolpromo.view.network;

import android.content.Context;
import android.util.Log;

import com.pertamina.musicoolpromo.model.login.Login;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitGet {

    private String token;

    public void getToken(Context context, RetrofitListener retrofitListener) {


        SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(context);
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
                    if (response.code() == 200) {
                        token = response.body();
                        Log.d("taianying", "onResponse: " + token);
                        retrofitListener.onRequestSuccess(token);
                    } else if (response.code() == 403) {
                            retrofitListener.onRequestSuccess("fail");
                            sharePreferenceManager.removeToken();
                            sharePreferenceManager.removeAccount();
                            sharePreferenceManager.removeEmail();
                            sharePreferenceManager.removePassword();
                            sharePreferenceManager.removeProfile();
                    } else {
                            retrofitListener.onRequestSuccess("fail");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        retrofitListener.onRequestError("Koneksi bermasalah");
                    }
                }
            });
        } catch (Exception e) {
            retrofitListener.onRequestError("Koneksi bermasalah");
        }
    }
}
