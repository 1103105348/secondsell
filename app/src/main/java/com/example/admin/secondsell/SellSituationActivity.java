package com.example.admin.secondsell;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.secondsell.models.Commodity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SellSituationActivity extends ParentActivity {

    private MyAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FirebaseUser user;
    private String userUID;

    ArrayList<Commodity> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        CreateNavigation();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        user = ((DefaultApplication) getApplication()).getAuth().getCurrentUser();
        userUID = user.getUid();

        myDataset = new ArrayList<>();
        final DatabaseReference dbRef = database.getReference("commodity");
        Query queryRef = dbRef.orderByChild("user").equalTo(userUID);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myDataset.clear();
                for (DataSnapshot ds_second : dataSnapshot.getChildren()) {
                    String Key = ds_second.getKey();
                    Log.e("Key", Key);
                    Commodity myCommodity = ds_second.getValue(Commodity.class);
                    myDataset.add(myCommodity);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public class MyAdapter extends RecyclerView.Adapter<SellSituationActivity.MyAdapter.ViewHolder> {
        private List<Commodity> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_name, text_price, text_content;
            public ImageView image;
            public Button edit, remove;

            public ViewHolder(View v) {
                super(v);
                text_name = (TextView) v.findViewById(R.id.own_text_name);
                text_price = (TextView) v.findViewById(R.id.own_text_price);
                text_content = (TextView) v.findViewById(R.id.own_text_content);
                image = (ImageView) v.findViewById(R.id.own_img);
                edit = (Button) v.findViewById(R.id.own_button_edit);
                remove = (Button) v.findViewById(R.id.own_button_remove);

            }
        }

        public List<Commodity> getmData() {
            return mData;
        }

        public MyAdapter(List<Commodity> data) {
            mData = data;
        }

        @Override
        public SellSituationActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.own_item, parent, false);
            SellSituationActivity.MyAdapter.ViewHolder vh = new SellSituationActivity.MyAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.text_name.setText(mData.get(position).getName());
            holder.text_price.setText("價格：" + mData.get(position).getPrice());
            holder.text_content.setText(mData.get(position).getContent());
            final String imageName = mData.get(position).getImage_name();
            Glide.with(holder.image.getContext())
                    .load(mData.get(position).getUrl())
                    .into(holder.image);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edit(mData.get(position), imageName);
                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(mData.get(position), imageName);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private void edit(final Commodity edit_item, final String ImgName) {
        Intent intent = new Intent();
        intent.setClass(SellSituationActivity.this, EditActivity.class);
        intent.putExtra("edit_item",edit_item.getName());
        intent.putExtra("ImgName",edit_item.getImage_name());
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void remove(final Commodity remove_item, final String ImgName) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("commodity");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference removeImgRef = storageRef.child("/photos/" + ImgName + ".jpg");

        AlertDialog.Builder remove_messeng = new AlertDialog.Builder(SellSituationActivity.this);
        remove_messeng.setTitle("結束商品刊登");
        remove_messeng.setMessage("您確定要刪除商品－" + remove_item.getName() + "，並結束刊登嗎?");
        remove_messeng.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(SellSituationActivity.this, "了解", Toast.LENGTH_SHORT).show();
            }
        });
        remove_messeng.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dbRef.child(remove_item.getName()).removeValue();
                removeImgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SellSituationActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        myDataset.remove(remove_item);
                        mAdapter.notifyDataSetChanged();
                        Log.e("data",myDataset.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SellSituationActivity.this, "似乎發生了問題，請稍後再試", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        remove_messeng.show();
    }
}

