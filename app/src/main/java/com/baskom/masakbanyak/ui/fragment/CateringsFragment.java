package com.baskom.masakbanyak.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.viewmodel.CateringViewModel;
import com.baskom.masakbanyak.viewmodel.ViewModelFactory;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.ui.adapter.CateringsAdapter;
import com.baskom.masakbanyak.R;

import java.util.ArrayList;

import javax.inject.Inject;


public class CateringsFragment extends Fragment {
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  private CateringViewModel mCateringViewModel;
  
  private ArrayList<Catering> mCaterings = new ArrayList<>();
  
  private SwipeRefreshLayout mRefreshLayout;
  private RecyclerView mRecyclerView;
  
  private CateringsAdapter mAdapter;
  
  private CateringsFragmentInteractionListener mListener;
  
  public static CateringsFragment newInstance() {
    CateringsFragment fragment = new CateringsFragment();
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mCateringViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CateringViewModel.class);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_caterings, container, false);
  
    mRefreshLayout = view.findViewById(R.id.refreshLayout);
    mRecyclerView = view.findViewById(R.id.recyclerView);
  
    mAdapter = new CateringsAdapter(mListener);
    
    return view;
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    mRefreshLayout.setRefreshing(true);
    mRefreshLayout.setOnRefreshListener(mCateringViewModel::refreshCaterings);
    
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), Configuration.ORIENTATION_PORTRAIT, false));
  }
  
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mCateringViewModel.getCateringsLiveData().observe(this, caterings -> {
      this.mCaterings = caterings;
      mAdapter.setCaterings(mCaterings);
      
      mRefreshLayout.setRefreshing(false);
    });
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    
    mCateringViewModel.getCateringsLiveData().removeObservers(this);
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof CateringsFragmentInteractionListener) {
      mListener = (CateringsFragmentInteractionListener) context;
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
  
  public interface CateringsFragmentInteractionListener {
    void onHomeFragmentInteraction(Catering catering);
  }
}
