package com.pertamina.musicoolpromo.viewmodel;

import androidx.lifecycle.ViewModel;

import com.pertamina.musicoolpromo.model.register.User;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;

public class UserViewModel extends ViewModel {

    private ApiInterface service = ApiClient.getData().create(ApiInterface.class);

    public void setItems(final User user) {

    }

}
