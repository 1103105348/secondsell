package com.example.admin.secondsell;

import android.os.Bundle;
import android.widget.TextView;

import com.example.admin.secondsell.models.Personal;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PersonalDataActivity extends ParentActivity {

    private Personal pd_data;
    private TextView pd_name,pd_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        CreateNavigation();

        FirebaseUser user = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();
        String userUID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("UserID").child(userUID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd_data = dataSnapshot.getValue(Personal.class);
                setView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void setView(){
        pd_name = (TextView)findViewById(R.id.pd_textView_name);
        pd_email = (TextView)findViewById(R.id.pd_textView_email);
        pd_name.setText("使用者姓名："+pd_data.getName());
        pd_email.setText("使用者信箱："+pd_data.getEmail());
    }
}
