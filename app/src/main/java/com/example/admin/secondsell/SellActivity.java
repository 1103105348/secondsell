package com.example.admin.secondsell;

import android.os.Bundle;

public class SellActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        CreateNavigation();
    }
}
