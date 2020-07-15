package com.pertamina.musicoolpromo.view.network;

public interface RetrofitListener {
    void onRequestSuccess(String jsonString);
    void onRequestError(String error);
}
