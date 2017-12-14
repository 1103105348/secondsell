package com.example.admin.secondsell;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("UserID");



        Button register_button = (Button) findViewById(R.id.regist_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(dbRef);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();

    }

    public void register(final DatabaseReference userIDRef){


        String register_email = ((EditText) findViewById(R.id.regist_editText_email)).getText().toString();
        String register_password = ((EditText) findViewById(R.id.regist_editText_password)).getText().toString();


        ((DefaultApplication)getApplication()).getAuth()
                .createUserWithEmailAndPassword(register_email,register_password)
                .addOnCompleteListener( this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("X", "createUserWithEmail:success");
                    Toast.makeText(RegisterActivity.this, "註冊成功，可以登入囉!",
                            Toast.LENGTH_SHORT).show();
                    user = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();
                    userUID = user.getUid();
                    next(userIDRef,userUID,user);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else {
                    // If sign in fails, display a message to the user.

                    Log.w("X", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
    private void next(DatabaseReference USERIDREF,String userUID,FirebaseUser user){
        String email = user.getEmail();
        String register_ID = ((EditText) findViewById(R.id.register_editText_ID)).getText().toString();
        USERIDREF.child(userUID).child("name").setValue(register_ID);
        USERIDREF.child(userUID).child("email").setValue(email);
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();


    }

}
