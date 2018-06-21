package com.baskom.masakbanyak.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.ui.fragment.TransactionsFragment;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
  private ArrayList<Order> orders = new ArrayList<>();
  private TransactionsFragment.TransactionFragmentInteractionListener mListener;
  
  public OrdersAdapter(TransactionsFragment.TransactionFragmentInteractionListener mListener) {
    this.mListener = mListener;
  }
  
  public void setOrders(ArrayList<Order> orders) {
    this.orders = orders;
    notifyDataSetChanged();
  }
  
  @NonNull
  @Override
  
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    return new ViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.orderTimeTextView.setText(orders.get(position).getOrderDateDDMMYYYY());
    holder.statusTextView.setText("Status: "+orders.get(position).getStatus().toUpperCase());
  }
  
  @Override
  public int getItemCount() {
    return orders.size();
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.itemview_order;
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView orderTimeTextView;
    private TextView statusTextView;
    
    public ViewHolder(View itemView) {
      super(itemView);
      
      orderTimeTextView = itemView.findViewById(R.id.orderTimeTextView);
      statusTextView = itemView.findViewById(R.id.statusTextView);
    }
  }
}
