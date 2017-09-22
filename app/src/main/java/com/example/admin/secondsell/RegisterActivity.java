package com.example.admin.secondsell;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        Button register_button = (Button) findViewById(R.id.regist_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();

    }

    public void register(){

        String email = ((EditText) findViewById(R.id.regist_editText_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.regist_editText_password)).getText().toString();

        ((DefaultApplication)getApplication()).getAuth().createUserWithEmailAndPassword(email,password).addOnCompleteListener( this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("X", "createUserWithEmail:success");
                    user = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("X", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }


                String register_message = task.isComplete()?"註冊成功":"註冊失敗";
                new AlertDialog.Builder(RegisterActivity.this)
                        .setMessage(register_message)
                        .setPositiveButton("OK",null)
                        .show();
            }
        });
    }
}
