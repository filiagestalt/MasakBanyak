package com.baskom.masakbanyak.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.viewmodel.CateringViewModel;
import com.baskom.masakbanyak.viewmodel.ViewModelFactory;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.ui.adapter.PacketsAdapter;
import com.baskom.masakbanyak.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class CateringActivity extends AppCompatActivity {
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  private CateringViewModel mCateringViewModel;
  
  private Catering mCatering;
  private ArrayList<Packet> mPackets = new ArrayList<>();
  
  private ImageView mImageView;
  private TextView mTextViewCateringName;
  private TextView mTextViewCateringEmail;
  private TextView mTextViewCateringAddress;
  private TextView mTextViewCateringPhone;
  private SwipeRefreshLayout mRefreshLayout;
  private RecyclerView mRecyclerView;
  
  private PacketsAdapter mAdapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_catering);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mCatering = (Catering) getIntent().getSerializableExtra("catering");
    mCateringViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CateringViewModel.class);
    
    mImageView = findViewById(R.id.catering_image);
    mTextViewCateringName = findViewById(R.id.catering_name);
    mTextViewCateringEmail = findViewById(R.id.catering_email);
    mTextViewCateringAddress = findViewById(R.id.catering_address);
    mTextViewCateringPhone = findViewById(R.id.catering_phone);
    mRefreshLayout = findViewById(R.id.refreshLayout);
    mRecyclerView = findViewById(R.id.packets);
    
    mAdapter = new PacketsAdapter();
    
    mCateringViewModel.getPacketsLiveData(mCatering).observe(this, packets -> {
      mPackets = packets;
      mAdapter.setPackets(mPackets);
      
      mRefreshLayout.setRefreshing(false);
    });
  
    mRefreshLayout.setRefreshing(true);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    mRefreshLayout.setOnRefreshListener(() -> mCateringViewModel.refreshPackets(mCatering));
    
    Picasso.get().load(MASAKBANYAK_URL + mCatering.getAvatar()).fit().centerCrop()
        .into(mImageView);
    
    mTextViewCateringName.setText(mCatering.getName());
    mTextViewCateringEmail.setText(mCatering.getEmail());
    mTextViewCateringAddress.setText(mCatering.getAddress());
    mTextViewCateringPhone.setText(mCatering.getPhone());
    
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, Configuration.ORIENTATION_PORTRAIT, false));
  }
}
