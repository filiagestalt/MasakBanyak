package com.baskom.masakbanyak.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.ui.fragment.CateringsFragment;
import com.baskom.masakbanyak.model.Catering;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.itangqi.waveloadingview.WaveLoadingView;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class CateringsAdapter extends RecyclerView.Adapter<CateringsAdapter.ViewHolder> {
  private ArrayList<Catering> caterings = new ArrayList<>();
  
  private CateringsFragment.CateringsFragmentInteractionListener listener;
  
  public CateringsAdapter(CateringsFragment.CateringsFragmentInteractionListener listener) {
    this.listener = listener;
  }
  
  public void setCaterings(ArrayList<Catering> caterings) {
    this.caterings = caterings;
    notifyDataSetChanged();
  }
  
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    return new ViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
    holder.mLayout.setOnClickListener(v -> {
      if (listener != null) {
        listener.onCateringsFragmentInteraction(caterings.get(position));
      }
    });
    
    holder.mTextView.setText(caterings.get(position).getName());
    
    Picasso.get().load(MASAKBANYAK_URL + caterings.get(position).getAvatar())
        .fit()
        .centerCrop()
        .into(holder.mImageView);
    
    holder.mWaveView.setCenterTitle(Double.toString(caterings.get(position).getTotalRating()));
    if(caterings.get(position).getTotalRating() == 0){
      holder.mWaveView.setProgressValue(8);
      holder.mWaveView.setAmplitudeRatio(8);
    }else{
      holder.mWaveView.setProgressValue((int) caterings.get(position).getTotalRating()*100/5-9);
      holder.mWaveView.setAmplitudeRatio((int) caterings.get(position).getTotalRating()*100/5-9);
    }
  }
  
  @Override
  public int getItemCount() {
    return caterings.size();
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.itemview_catering;
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    private CardView mLayout;
    private ImageView mImageView;
    private TextView mTextView;
    private WaveLoadingView mWaveView;
    
    public ViewHolder(View itemView) {
      super(itemView);
      
      mLayout = itemView.findViewById(R.id.catering_item_layout);
      mImageView = itemView.findViewById(R.id.catering_image);
      mTextView = itemView.findViewById(R.id.catering_name);
      mWaveView = itemView.findViewById(R.id.catering_rating);
    }
  }
}
