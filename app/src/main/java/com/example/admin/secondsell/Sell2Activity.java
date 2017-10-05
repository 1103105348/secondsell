package com.example.admin.secondsell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sell2Activity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CreateNavigation();
    }
}
