package com.baskom.masakbanyak.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.ui.activity.PacketActivity;
import com.baskom.masakbanyak.model.Packet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class PacketsAdapter extends RecyclerView.Adapter<PacketsAdapter.ViewHolder> {
  
  private ArrayList<Packet> packets = new ArrayList<>();
  
  public void setPackets(ArrayList<Packet> packets) {
    this.packets = packets;
    notifyDataSetChanged();
  }
  
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    return new ViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    holder.mTextView.setText(packets.get(position).getName());
    holder.mLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), PacketActivity.class);
        intent.putExtra("packet", packets.get(position));
        
        view.getContext().startActivity(intent);
      }
    });
    
    Picasso.get().load(MASAKBANYAK_URL + packets.get(position).getImages().get(0))
        .fit()
        .centerCrop()
        .into(holder.mImageView);
  }
  
  @Override
  public int getItemCount() {
    return packets.size();
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.itemview_packet;
  }
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private CardView mLayout;
    private ImageView mImageView;
    private TextView mTextView;
    
    public ViewHolder(View itemView) {
      super(itemView);
      
      mLayout = itemView.findViewById(R.id.packet_item_layout);
      mImageView = itemView.findViewById(R.id.packet_image);
      mTextView = itemView.findViewById(R.id.packet_name);
    }
  }
}
