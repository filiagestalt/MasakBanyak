package com.baskom.masakbanyak.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.ui.ViewModelFactory;
import com.baskom.masakbanyak.ui.fragment.viewmodel.CateringsViewModel;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.ui.adapter.CateringsAdapter;
import com.baskom.masakbanyak.R;
import com.github.ybq.android.spinkit.style.FoldingCube;

import java.util.ArrayList;

import javax.inject.Inject;


public class CateringsFragment extends Fragment {
  
  private ArrayList<Catering> mCaterings = new ArrayList<>();
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  private CateringsViewModel mCateringsViewModel;
  
  private ProgressBar mProgressBar;
  private CateringsAdapter mAdapter;
  private RecyclerView mRecyclerView;
  
  private HomeFragmentInteractionListener mListener;
  
  public static CateringsFragment newInstance() {
    CateringsFragment fragment = new CateringsFragment();
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mCateringsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CateringsViewModel.class);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
  
    mProgressBar = view.findViewById(R.id.progress_bar);
    mRecyclerView = view.findViewById(R.id.caterings);
    
    return view;
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  
    FoldingCube foldingCube = new FoldingCube();
    foldingCube.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
  
    mProgressBar.setIndeterminateDrawable(foldingCube);
    
    mAdapter = new CateringsAdapter(mListener);
    
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), Configuration.ORIENTATION_PORTRAIT, false));
    mRecyclerView.setVisibility(View.INVISIBLE);
  }
  
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mCateringsViewModel.getCateringsLiveData().observe(this, caterings -> {
      this.mCaterings = caterings;
      
      mAdapter.setCaterings(mCaterings);
      
      mProgressBar.setVisibility(View.GONE);
      mRecyclerView.setVisibility(View.VISIBLE);
    });
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    
    mCateringsViewModel.getCateringsLiveData().removeObservers(this);
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof HomeFragmentInteractionListener) {
      mListener = (HomeFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement HomeFragmentInteractionListener");
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }
  
  public interface HomeFragmentInteractionListener {
    void onHomeFragmentInteraction(Catering catering);
  }
}
