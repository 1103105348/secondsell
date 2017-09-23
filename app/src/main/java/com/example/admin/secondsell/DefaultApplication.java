package com.example.admin.secondsell;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 2017/9/22.
 */

public class DefaultApplication extends Application {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebaseAuth();
    }


    private void initFirebaseAuth(){

        auth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }
}
