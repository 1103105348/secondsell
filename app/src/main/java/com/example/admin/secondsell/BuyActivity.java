package com.example.admin.secondsell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.secondsell.models.Commodity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyActivity extends ParentActivity {

    private TextView text_name,text_price,text_time,text_place,text_content,text_other,text_user;
    private ImageView image;
    private Commodity myCommodity;
    private Button back,subscripe,buy;
    private FirebaseUser user;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        CreateNavigation();

        Bundle bundle = getIntent().getExtras();
        String wanted = bundle.getString("wanted");

        user = ((DefaultApplication)getApplication()).getAuth().getCurrentUser();
        userUID = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("commodity");




        Query queryRef = dbRef.orderByChild("name").equalTo(wanted);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds_second : dataSnapshot.getChildren()) {
                    String Key = ds_second.getKey();
                    Log.e("Key", Key);
                    myCommodity = ds_second.getValue(Commodity.class);
                    setView();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        text_name = (TextView)findViewById(R.id.buy_textView_name);
        text_price = (TextView)findViewById(R.id.buy_textView_price);
        text_content = (TextView)findViewById(R.id.buy_textView_content);
        text_time = (TextView)findViewById(R.id.buy_textView_time);
        text_place = (TextView)findViewById(R.id.buy_textView_place);
        text_other = (TextView)findViewById(R.id.buy_textView_other);
        text_user = (TextView)findViewById(R.id.buy_textView_user);
        image = (ImageView)findViewById(R.id.buy_imageView);

        buy = (Button)findViewById(R.id.buy_button_buy);
        back = (Button)findViewById(R.id.buy_button_back);
        subscripe = (Button)findViewById(R.id.buy_button_subscripe);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        subscripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscripe();
            }
        });
    }

    private  void setView(){
        text_name.setText("商品名稱："+myCommodity.getName());
        text_price.setText("價格："+myCommodity.getPrice());
        text_content.setText("商品內容："+myCommodity.getContent());
        text_time.setText("可交易時間："+myCommodity.getTime());
        text_place.setText("可交易地點："+myCommodity.getPlace());
        text_other.setText("備註："+myCommodity.getOther());
        Glide.with(image.getContext())
                .load(myCommodity.getUrl())
                .into(image);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userNameRef = database.getReference("UserID");
        DatabaseReference IDRef = userNameRef.child(myCommodity.getUser()).child("name");
        IDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String nickname = dataSnapshot.getValue(String.class);
                setUserName(nickname);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private  void setUserName(String nickname){
        text_user.setText("賣家："+nickname);
    }
    private  void back(){
        Intent intent = new Intent();
        String back_to_commodity = myCommodity.getCategory();
        intent.setClass(BuyActivity.this ,CommodityActivity.class );
        intent.putExtra("search",back_to_commodity);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void buy(){

    }
    private void subscripe(){

    }
}
