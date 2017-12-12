package com.example.admin.secondsell;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SellActivity extends ParentActivity {

    private static final int PICKER = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    private StorageReference storageRef;
    // Points to "images"
    private StorageReference imagesRef;
    private StorageReference spaceRef ;
    //Button
    private Button search;
    private Button upload;
    private ImageView pickImg;

    private String do_nothing = "error";
    private String load_img_fail = "load image fail";
    private String imageURL;
    private Uri imgUri;
    private Task<UploadTask.TaskSnapshot> uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        CreateNavigation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storageRef = FirebaseStorage.getInstance().getReference();
        imagesRef = storageRef.child("images");
        spaceRef = storageRef.child("images/space.jpg");

        search = (Button) findViewById(R.id.Sell_Search);
        upload = (Button) findViewById(R.id.Sell_Upload);
        pickImg = (ImageView) findViewById(R.id.Sell_imageView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload(imgUri);
            }
        });


    }

    private void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(SellActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_write = ActivityCompat.checkSelfPermission(SellActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else if(permission_write!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        }else {
            getLocalImg();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocalImg();
                } else {
                    Toast.makeText(SellActivity.this, do_nothing, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private void getLocalImg() {
        Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
        picker.setType("image/*");
        picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        Intent destIntent = Intent.createChooser(picker, null);
        startActivityForResult(destIntent, PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                imgUri = uri;
                //upload(uri);
                /*imgUri = imagesRef.getPath(Storage_test.this, uri);*/
                if (imagesRef != null && !imagesRef.equals("")) {
                    Glide.with(SellActivity.this).load(uri).into(pickImg);
                } else {
                    Toast.makeText(SellActivity.this, load_img_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void upload(final Uri uri) {
        final StorageReference riversRef = storageRef.child("/photos/" + uri.getLastPathSegment()+".jpg");

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("url",taskSnapshot.getDownloadUrl().toString());
                        Log.e("url",riversRef.getDownloadUrl().toString());
                        imageURL = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(SellActivity.this,"Image onSuccess." , Toast.LENGTH_SHORT).show();
                        next(imageURL,uri.getLastPathSegment());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SellActivity.this, "Image onFailure.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void next(String url,String image_name){

        Log.e("A",image_name);
        Intent intent = new Intent();
        intent.setClass(SellActivity.this ,Sell2Activity.class );
        intent.putExtra("Url",url);
        intent.putExtra("image_name",image_name);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}

