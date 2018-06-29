package com.baskom.masakbanyak.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.ui.adapter.OrdersAdapter;
import com.baskom.masakbanyak.viewmodel.CateringViewModel;
import com.baskom.masakbanyak.viewmodel.OrderViewModel;
import com.baskom.masakbanyak.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

public class TransactionsFragment extends Fragment {
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  OrderViewModel mOrderViewModel;

  private ArrayList<Order> mOrders = new ArrayList<>();

  private AppBarLayout mAppBarLayout;
  private BottomNavigationView mBottomNavigation;
  private ColorStateList mBottomNavigationIcon;
  private ColorStateList mBottomNavigationText;
  
  private SwipeRefreshLayout mRefreshLayout;
  private RecyclerView mRecyclerView;
  
  private OrdersAdapter mAdapter;
  
  private TransactionFragmentInteractionListener mListener;
  
  public static TransactionsFragment newInstance() {
    TransactionsFragment fragment = new TransactionsFragment();
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    Components.getSessionComponent().inject(this);
    
    mOrderViewModel = ViewModelProviders.of(this, mViewModelFactory).get(OrderViewModel.class);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_transactions, container, false);
    
    mAppBarLayout = getActivity().findViewById(R.id.appBarLayout);
    mBottomNavigation = getActivity().findViewById(R.id.navigation);
    mBottomNavigationIcon = mBottomNavigation.getItemIconTintList();
    mBottomNavigationText = mBottomNavigation.getItemTextColor();
    
    mRefreshLayout = view.findViewById(R.id.refreshLayout);
    mRecyclerView = view.findViewById(R.id.recyclerView);
    
    mAdapter = new OrdersAdapter(mListener);
    
    return view;
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    ViewCompat.setElevation(mAppBarLayout, 0);
    ViewCompat.setElevation(mBottomNavigation, 0);
    mBottomNavigation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    mBottomNavigation.setItemIconTintList(getResources().getColorStateList(R.color.bottom_navigation_alternative));
    mBottomNavigation.setItemTextColor(getResources().getColorStateList(R.color.bottom_navigation_alternative));
    
    mRefreshLayout.setRefreshing(true);
    mRefreshLayout.setOnRefreshListener(mOrderViewModel::refreshOrders);
    
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
  }
  
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mOrderViewModel.getOrdersLiveData().observe(this, orders -> {
      this.mOrders = orders;
      mAdapter.setOrders(mOrders);
      
      mRefreshLayout.setRefreshing(false);
    });
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    
    ViewCompat.setElevation(mAppBarLayout, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    ViewCompat.setElevation(mBottomNavigation, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    mBottomNavigation.setBackgroundColor(getResources().getColor(R.color.white));
    mBottomNavigation.setItemIconTintList(mBottomNavigationIcon);
    mBottomNavigation.setItemTextColor(mBottomNavigationText);
    
    mOrderViewModel.getOrdersLiveData().removeObservers(this);
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof TransactionFragmentInteractionListener) {
      mListener = (TransactionFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement CateringsFragmentInteractionListener");
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }
  
  public interface TransactionFragmentInteractionListener {
    void onTransactionsFragmentInteraction(Order order);
  }
}
