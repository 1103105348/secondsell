package com.example.admin.secondsell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("onAuthStateChanged", "登入:" + user.getUid());
                    userUID = user.getUid();
                } else {
                    Log.d("onAuthStateChanged", "已登出");
                }

            }
        };

        Button loginButton = (Button) findViewById(R.id.login_button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Button registButton = (Button) findViewById(R.id.login_button_regist);
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((DefaultApplication)getApplication()).getAuth().addAuthStateListener(authStateListener);
        ((EditText) findViewById(R.id.login_editText_email)).setText("abc@xx.com");
        ((EditText) findViewById(R.id.logic_editTex_password)).setText("112233");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((DefaultApplication)getApplication()).getAuth().removeAuthStateListener(authStateListener);
    }



    public void login() {
        String email = ((EditText) findViewById(R.id.login_editText_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.logic_editTex_password)).getText().toString();
        Log.d("AUTH", email + "/" + password);
        ((DefaultApplication)getApplication()).getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    String login_message = task.isSuccessful()?"登入成功":"登入失敗";
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage(login_message)
                            .show();
                    startActivity(new Intent(LoginActivity.this, AboutUsActivity.class));
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } if (!task.isSuccessful()){
                    String failure_message = "登入失敗，請重新確認輸入資料";
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage(failure_message)
                            .show();
                }
            }
        });
    }

    public void regist(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
