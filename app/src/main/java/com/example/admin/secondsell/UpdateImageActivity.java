package com.example.admin.secondsell;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

public class UpdateImageActivity extends ParentActivity {

    private Uri imgUri;
    private Button search, upload;
    private ImageView pickImg;
    private StorageReference imagesRef, storageRef;
    private String imageURL;
    private static final int PICKER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);
        CreateNavigation();

        Bundle bundle = getIntent().getExtras();
        final String ImgName = bundle.getString("image_name");
        Log.e("a", ImgName);

        storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference originalImgRef = storageRef.child("/photos/" + ImgName + ".jpg");
        imagesRef = storageRef.child("images");

        search = (Button) findViewById(R.id.Sell_Search);
        upload = (Button) findViewById(R.id.Sell_Upload);
        pickImg = (ImageView) findViewById(R.id.Sell_imageView);
        Log.e("a", originalImgRef.getDownloadUrl().toString());
        Glide.with(pickImg.getContext())
                .using(new FirebaseImageLoader())
                .load(originalImgRef)
                .into(pickImg);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload(imgUri);
            }
        });

    }

    private void updateImage() {
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
                if (imagesRef != null && !imagesRef.equals("")) {
                    Glide.with(UpdateImageActivity.this)
                            .load(uri)
                            .into(pickImg);
                } else {
                    Toast.makeText(UpdateImageActivity.this, "load image error", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void upload(final Uri uri) {
        final StorageReference riversRef = storageRef.child("/photos/" + uri.getLastPathSegment() + ".jpg");

        AlertDialog.Builder updateImg_messeng = new AlertDialog.Builder(UpdateImageActivity.this);
        updateImg_messeng.setTitle("更換圖片");
        updateImg_messeng.setMessage("按下確認鍵之後，圖片就會直接更換，您確定要更換圖片嗎?");
        updateImg_messeng.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(UpdateImageActivity.this, "了解", Toast.LENGTH_SHORT).show();
            }
        });
        updateImg_messeng.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                riversRef.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("url", taskSnapshot.getDownloadUrl().toString());
                                imageURL = taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(UpdateImageActivity.this, "Image onSuccess.", Toast.LENGTH_SHORT).show();
                                next(imageURL,uri.getLastPathSegment());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(UpdateImageActivity.this, "Image onFailure.", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });
        updateImg_messeng.show();


    }

    private void next(String new_url,String new_image_name) {
        String test ="test";
        Log.e("A", new_url);
        Log.e("B", new_image_name);
        Intent intent = new Intent();
        intent.putExtra("New_url",new_url);
        intent.putExtra("New new_image_name",new_image_name);

        //intent.putExtra("New Uri", imgUri.toString());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

