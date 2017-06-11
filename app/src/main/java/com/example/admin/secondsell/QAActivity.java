package com.example.admin.secondsell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QAActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        CreateNavigation();
    }
}
