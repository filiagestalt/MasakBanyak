package com.baskom.masakbanyak;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;
import static com.baskom.masakbanyak.Constants.verifyTokenAndExecuteCall;

public class CateringActivity extends AppCompatActivity {

    private Catering mCatering;
    private ArrayList<Packet> mPackets = new ArrayList<>();
    private ImageView mImageView;
    private TextView mTextViewCateringName;
    private TextView mTextViewCateringEmail;
    private TextView mTextViewCateringAddress;
    private TextView mTextViewCateringPhone;
    private PacketsAdapter mPacketsAdapter;
    private RecyclerView mPacketsRecyclerView;

    private CanMakeServiceCall mMakePacketsCall = new CanMakeServiceCall() {
        @Override
        public void makeCall(MasakBanyakService service, String access_token) {
            Call<ArrayList<Packet>> call = service.packets(
                    "Bearer "+access_token,
                    mCatering.getCatering_id()
            );

            call.enqueue(new Callback<ArrayList<Packet>>() {
                @Override
                public void onResponse(Call<ArrayList<Packet>> call, Response<ArrayList<Packet>> response) {
                    if(response.isSuccessful()){
                        mPackets = response.body();

                        mPacketsAdapter.setPackets(mPackets);
                        mPacketsAdapter.notifyDataSetChanged();
                    }else{
                        try {
                            Toast.makeText(
                                    CateringActivity.this,
                                    response.errorBody().string(),
                                    Toast.LENGTH_LONG
                            ).show();
                        } catch (IOException e) {
                            Toast.makeText(
                                    CateringActivity.this,
                                    e.toString(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Packet>> call, Throwable t) {
                    Toast.makeText(CateringActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering);

        mCatering = (Catering) getIntent().getSerializableExtra("catering");

        mImageView = findViewById(R.id.catering_image);
        mTextViewCateringName = findViewById(R.id.catering_name);
        mTextViewCateringEmail = findViewById(R.id.catering_email);
        mTextViewCateringAddress = findViewById(R.id.catering_address);
        mTextViewCateringPhone = findViewById(R.id.catering_phone);
        mPacketsRecyclerView = findViewById(R.id.packets);

        Picasso.get().load(MASAKBANYAK_URL+mCatering.getAvatar()).fit().centerCrop()
                .into(mImageView);

        mTextViewCateringName.setText(mCatering.getName());
        mTextViewCateringEmail.setText(mCatering.getEmail());
        mTextViewCateringAddress.setText(mCatering.getAddress());
        mTextViewCateringPhone.setText(mCatering.getPhone());

        mPacketsAdapter = new PacketsAdapter(mPackets);

        mPacketsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mPacketsRecyclerView.setAdapter(mPacketsAdapter);

        FoldingCube foldingCube = new FoldingCube();
        foldingCube.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        verifyTokenAndExecuteCall(getApplicationContext(), mMakePacketsCall);
    }
}
