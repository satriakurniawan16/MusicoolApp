package com.pertamina.musicoolpromo.view.utilities;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class GlobalMethod {

    public void snackBar(View root,String textToDisplay){
        Snackbar snackbar = Snackbar.make(root, textToDisplay, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                //your other action after user hit the "OK" button
            }
        });
        snackbar.show();
    }

}
