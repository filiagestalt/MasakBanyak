package com.baskom.masakbanyak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PacketActivity extends AppCompatActivity {

    private Packet mPacket;
    private TextView mTextViewPacketName;
    private LinearLayout mPacketContents;
    private TextView mTextViewPacketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet);

        mPacket = (Packet) getIntent().getSerializableExtra("packet");

        mTextViewPacketName = findViewById(R.id.packet_name);
        mPacketContents = findViewById(R.id.packet_contents);
        mTextViewPacketPrice = findViewById(R.id.packet_price);

        mTextViewPacketName.setText(mPacket.getName());
        mTextViewPacketPrice.setText(Integer.toString(mPacket.getPrice()*mPacket.getMinimum_quantity()));
    }
}
