package com.example.admin.secondsell;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.secondsell.models.Commodity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditActivity extends ParentActivity {

    public static final int REQUEST_UPDATE_IMAGE = 3001;

    private String ImgName;
    private Commodity editCommodity;
    private ImageView ed_imageView;
    private Button update_image, update;
    private String new_name, new_price, new_content, new_place, new_time, new_other;
    private String select, imageURL;
    private String new_url,new_image_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        CreateNavigation();

        final Spinner spinner = (Spinner) findViewById(R.id.edit_spinner);
        final ArrayAdapter<CharSequence> nadapter = ArrayAdapter
                .createFromResource(
                        this, R.array.commodity_select, android.R.layout.simple_spinner_item
                );
        spinner.setAdapter(nadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select = spinner.getSelectedItem().toString();
                Toast.makeText(EditActivity.this,
                        nadapter.getItem(i), Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ed_imageView = (ImageView) findViewById(R.id.edit_imageView);

        Bundle bundle = getIntent().getExtras();
        final String edit_item = bundle.getString("edit_item");
        ImgName = bundle.getString("ImgName");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("commodity");

        Query queryRef = dbRef.orderByChild("name").equalTo(edit_item);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds_second : dataSnapshot.getChildren()) {
                    String Key = ds_second.getKey();
                    Log.e("Key", Key);
                    editCommodity = ds_second.getValue(Commodity.class);
                    setView(editCommodity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        update_image = (Button) findViewById(R.id.edit_button_stroge);
        update = (Button) findViewById(R.id.edit_button_update);
        update_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update_image = new Intent();
                update_image.setClass(EditActivity.this, UpdateImageActivity.class);
                update_image.putExtra("image_name", ImgName);
                startActivityForResult(update_image, REQUEST_UPDATE_IMAGE);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(dbRef, edit_item, select, ImgName,editCommodity);
            }
        });
    }

    private void setView(Commodity editCommodity) {
        EditText ed_name = (EditText) findViewById(R.id.edit_editText_name);
        EditText ed_price = (EditText) findViewById(R.id.edit_editText_price);
        EditText ed_content = (EditText) findViewById(R.id.edit_editText_content);
        EditText ed_place = (EditText) findViewById(R.id.edit_editText_place);
        EditText ed_time = (EditText) findViewById(R.id.edit_editText_time);
        EditText ed_other = (EditText) findViewById(R.id.edit_editText_other);
        ed_imageView = (ImageView) findViewById(R.id.edit_imageView);
        ed_name.setText(editCommodity.getName());
        ed_price.setText(editCommodity.getPrice());
        ed_content.setText(editCommodity.getContent());
        ed_place.setText(editCommodity.getPlace());
        ed_time.setText(editCommodity.getTime());
        ed_other.setText(editCommodity.getOther());
        Glide.with(ed_imageView.getContext())
                .load(editCommodity.getUrl())
                .into(ed_imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_UPDATE_IMAGE) {


                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                final StorageReference removeRef = storageRef.child("/photos/" + ImgName + ".jpg");

                Intent intent = this.getIntent();
                Bundle bundle1 = intent.getExtras();
                Log.e("",(bundle1==null)+"");
                new_url = bundle1.getString("New_url");
                new_image_name =  bundle1.getString("New new_image_name");
                Log.e("C",new_url);
                Log.e("D",new_image_name);
                Glide.with(ed_imageView.getContext())
                        .load(new_url)
                        .into(ed_imageView);

                removeRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                //Uri uti = Uri.parse(getIntent().getStringExtra("New Url"));
            }
        }
    }

    private void updateData(final DatabaseReference dbRef, final String edit_item, String new_category, String ImgName,Commodity editCommodity) {

        dbRef.child(edit_item).removeValue();

        EditText ed_name = (EditText) findViewById(R.id.edit_editText_name);
        EditText ed_price = (EditText) findViewById(R.id.edit_editText_price);
        EditText ed_content = (EditText) findViewById(R.id.edit_editText_content);
        EditText ed_place = (EditText) findViewById(R.id.edit_editText_place);
        EditText ed_time = (EditText) findViewById(R.id.edit_editText_time);
        EditText ed_other = (EditText) findViewById(R.id.edit_editText_other);
        new_name = ed_name.getText().toString();
        new_price = ed_price.getText().toString();
        new_content = ed_content.getText().toString();
        new_place = ed_place.getText().toString();
        new_time = ed_time.getText().toString();
        new_other = ed_other.getText().toString();
        dbRef.child(new_name).child("name").setValue(new_name);
        dbRef.child(new_name).child("price").setValue(new_price);
        dbRef.child(new_name).child("place").setValue(new_place);
        dbRef.child(new_name).child("content").setValue(new_content);
        dbRef.child(new_name).child("time").setValue(new_time);
        dbRef.child(new_name).child("other").setValue(new_other);
        dbRef.child(new_name).child("category").setValue(new_category);
        dbRef.child(new_name).child("url").setValue(new_url);
        dbRef.child(new_name).child("image_name").setValue(new_image_name);
        dbRef.child(new_name).child("user").setValue(editCommodity.getUser());

        /*riversRef.putFile(new_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("url", taskSnapshot.getDownloadUrl().toString());
                        imageURL = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(EditActivity.this, "Image onSuccess.", Toast.LENGTH_SHORT).show();
                        dbRef.child(edit_item).child("url").setValue(imageURL);
                        dbRef.child(edit_item).child("image_name").setValue(imageURL);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditActivity.this, "Image onFailure.", Toast.LENGTH_SHORT).show();
                    }
                });*/


        Toast.makeText(EditActivity.this, "您的商品已更新成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditActivity.this, SellSituationActivity.class));

    }
}
