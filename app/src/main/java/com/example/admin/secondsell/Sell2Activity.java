package com.example.admin.secondsell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sell2Activity extends ParentActivity {

    private String data_name,data_price,data_content,data_place,data_time,data_other;
    private String select;

    private FirebaseUser user;
    private String userUID;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell2);
        CreateNavigation();


        final Spinner spinner = (Spinner)findViewById(R.id.Sell2_spinner);
        final ArrayAdapter<CharSequence>nadapter = ArrayAdapter
                .createFromResource(
                        this,R.array.commodity_select,android.R.layout.simple_spinner_item
                );
        spinner.setAdapter(nadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select = spinner.getSelectedItem().toString();
                Toast.makeText(Sell2Activity.this,
                        nadapter.getItem(i),Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("commodity");

        Button submit = (Button)findViewById(R.id.Sell2_button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFirebase(select,dbRef);
            }
        });
    }

    private void UploadFirebase(String category, DatabaseReference UplordRef){

        user = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();
        userUID = user.getUid();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
        final String imageurl = bundle.getString("Url");
        final String imageName = bundle.getString("image_name");
        //DatabaseReference categoryRef = UplordRef.child(category);
        EditText ed_name = (EditText)findViewById(R.id.Sell2_editText_name);
        EditText ed_price = (EditText)findViewById(R.id.Sell2_editText_price);
        EditText ed_content = (EditText)findViewById(R.id.Sell2_editText_content);
        EditText ed_place = (EditText)findViewById(R.id.Sell2_editText_place);
        EditText ed_time = (EditText)findViewById(R.id.Sell2_editText_time);
        EditText ed_other = (EditText)findViewById(R.id.Sell2_editText_other);
        data_name = ed_name.getText().toString();
        data_price = ed_price.getText().toString();
        data_content = ed_content.getText().toString();
        data_place = ed_place.getText().toString();
        data_time = ed_time.getText().toString();
        data_other = ed_other.getText().toString();
        UplordRef.child(data_name).child("name").setValue(data_name);
        UplordRef.child(data_name).child("price").setValue(data_price);
        UplordRef.child(data_name).child("place").setValue(data_place);
        UplordRef.child(data_name).child("content").setValue(data_content);
        UplordRef.child(data_name).child("time").setValue(data_time);
        UplordRef.child(data_name).child("other").setValue(data_other);
        UplordRef.child(data_name).child("url").setValue(imageurl);
        UplordRef.child(data_name).child("category").setValue(category);
        UplordRef.child(data_name).child("user").setValue(userUID);
        UplordRef.child(data_name).child("image_name").setValue(imageName);


        Toast.makeText(Sell2Activity.this, "您的商品已上架成功",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Sell2Activity.this ,HomeActivity.class));
        }
    }
}
