package com.example.admin.secondsell;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    Log.d("onAuthStateChanged","登入:"+user.getUid());
                    userUID = user.getUid();
                }
                else{
                    Log.d("onAuthStateChanged","已登出");
                }

            }
        };

        Button loginButton = (Button) findViewById(R.id.login_button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                startActivity(new Intent(LoginActivity.this, AboutUsActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    public void login(){
        String email = ((EditText)findViewById(R.id.login_editText_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.logic_editTex_password)).getText().toString();
        Log.d("AUTH", email+"/"+password);
        auth.signInWithEmailAndPassword(email,password);
    }
}
