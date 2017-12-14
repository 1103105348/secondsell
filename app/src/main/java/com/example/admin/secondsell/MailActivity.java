package com.example.admin.secondsell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MailActivity extends ParentActivity {

    private EditText ed_subject,ed_text;
    private TextView tv_email;
    private Button send;
    private String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        CreateNavigation();

        Bundle bundle =  getIntent().getExtras();
        String userUID = bundle.getString("userUID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("UserID").child(userUID).child("email");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                to = dataSnapshot.getValue(String.class);
                Log.e("to", to);
                setView(to);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ed_subject = (EditText)findViewById(R.id.mail_editText_subject);
        ed_text = (EditText)findViewById(R.id.mail_editText_text);
        tv_email = (TextView) findViewById(R.id.mail_textView_email);
        send = (Button) findViewById(R.id.mail_button);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = "[二手拍賣]" + ed_subject.getText().toString();
                String text = ed_text.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT,subject);
                email.putExtra(Intent.EXTRA_TEXT,text);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,"Send e-mail form"));
            }
        });
    }
    private void setView(String to){
        tv_email.setText(to);
    }
}
