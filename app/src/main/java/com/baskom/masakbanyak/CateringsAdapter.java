package com.baskom.masakbanyak;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class CateringsAdapter extends RecyclerView.Adapter<CateringsAdapter.ViewHolder> {

    private ArrayList<Catering> caterings = new ArrayList<>();
    private HomeFragment.HomeFragmentInteractionListener listener;

    public CateringsAdapter(ArrayList<Catering> caterings, HomeFragment.HomeFragmentInteractionListener listener) {
        this.caterings = caterings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onHomeFragmentInteraction(caterings.get(position));
                }
            }
        });

        holder.mTextView.setText(caterings.get(position).getName());

        Picasso.get().load(MASAKBANYAK_URL+caterings.get(position).getAvatar())
                .fit()
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return caterings.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.itemview_catering;
    }

    public ArrayList<Catering> getCaterings() {
        return caterings;
    }

    public void setCaterings(ArrayList<Catering> caterings) {
        this.caterings = caterings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout mLayout;
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.catering_item_layout);
            mImageView = itemView.findViewById(R.id.catering_image);
            mTextView = itemView.findViewById(R.id.catering_name);
        }
    }
}
