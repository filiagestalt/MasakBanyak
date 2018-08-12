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

import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.ui.adapter.CateringsAdapter;
import com.baskom.masakbanyak.viewmodel.CateringViewModel;
import com.baskom.masakbanyak.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

public class SearchFragment extends Fragment {
  private static final String KEYWORD = "keyword";
  
  private String mKeyword;
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  private CateringViewModel mCateringViewModel;
  
  private ArrayList<Catering> mCaterings = new ArrayList<>();
  
  private SwipeRefreshLayout mRefreshLayout;
  private RecyclerView mRecyclerView;
  
  private CateringsAdapter mAdapter;
  
  private CateringsFragment.CateringsFragmentInteractionListener mListener;
  
  public SearchFragment() {
  
  }
  
  public static SearchFragment newInstance(String keyword) {
    SearchFragment fragment = new SearchFragment();
    Bundle args = new Bundle();
    args.putString(KEYWORD, keyword);
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mKeyword = getArguments().getString(KEYWORD);
    }
  
    Components.getSessionComponent().inject(this);
  
    mCateringViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CateringViewModel.class);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
  
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
    
    mCateringViewModel.getSearchedCateringsLiveData(mKeyword).observe(this, caterings -> {
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
    if (context instanceof CateringsFragment.CateringsFragmentInteractionListener) {
      mListener = (CateringsFragment.CateringsFragmentInteractionListener) context;
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
}
