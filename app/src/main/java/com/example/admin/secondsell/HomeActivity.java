package com.example.admin.secondsell;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.StorageReference;

public class HomeActivity extends ParentActivity {

    private Button Book,Clothes,ThreeC,Furniture,MakeUp,Ticket,Bag,Other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CreateNavigation();

        Book = (Button)findViewById(R.id.home_button_book);
        Clothes = (Button)findViewById(R.id.home_button_clothes);
        ThreeC = (Button)findViewById(R.id.home_button_3C);
        Furniture = (Button)findViewById(R.id.home_button_Furniture);
        MakeUp = (Button)findViewById(R.id.home_button_makeup);
        Ticket = (Button)findViewById(R.id.home_button_ticket);
        Bag = (Button)findViewById(R.id.home_button_bag);
        Other= (Button)findViewById(R.id.home_button_other);

        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "書" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        Clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "衣服" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        ThreeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "3C" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        Furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "家具" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        MakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "美妝" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        Ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "票卷" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        Bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "包包" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        Other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String search = "其他" ;
                intent.setClass(HomeActivity.this ,CommodityActivity.class );
                intent.putExtra("search",search );
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
