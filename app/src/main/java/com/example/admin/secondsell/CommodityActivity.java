package com.example.admin.secondsell;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.secondsell.models.Commodity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CommodityActivity extends ParentActivity {

    private StorageReference storageRef;
    private StorageReference commodityRef;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        CreateNavigation();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Bundle bundle = this.getIntent().getExtras();
        final String search = bundle.getString("search");


        final ArrayList<Commodity> myDataset = new ArrayList<>();
        final DatabaseReference dbRef = database.getReference("commodity");
        Query queryRef = dbRef.orderByChild("category").equalTo(search);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Commodity> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_name, text_price, text_content;
            public ImageView image;

            public ViewHolder(View v) {
                super(v);
                text_name = (TextView) v.findViewById(R.id.own_text_name);
                text_price = (TextView) v.findViewById(R.id.own_text_price);
                text_content = (TextView) v.findViewById(R.id.info_text_content);
                image = (ImageView) v.findViewById(R.id.info_img);
            }
        }

        public MyAdapter(List<Commodity> data) {
            mData = data;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.text_name.setText(mData.get(position).getName());
            holder.text_price.setText("價格："+ mData.get(position).getPrice());
            holder.text_content.setText(mData.get(position).getContent());
            Glide.with(holder.image.getContext())
                    .load(mData.get(position).getUrl())
                    .into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(CommodityActivity.this, BuyActivity.class);
                    intent.putExtra("wanted", mData.get(position).getName());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Toast.makeText(CommodityActivity.this, "Item " + mData.get(position).getName() + " is clicked.", Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(CommodityActivity.this, "Item " + position + " is long clicked.", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

}
