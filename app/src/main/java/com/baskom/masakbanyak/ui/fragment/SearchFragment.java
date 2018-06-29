package com.baskom.masakbanyak.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.model.Catering;

public class SearchFragment extends Fragment {
  private static final String QUERY = "query";
  
  private String mQuery;
  
  private OnSearchFragmentInteractionListener mListener;
  
  public SearchFragment() {
  
  }
  
  public static SearchFragment newInstance(String query) {
    SearchFragment fragment = new SearchFragment();
    Bundle args = new Bundle();
    args.putString(QUERY, query);
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mQuery = getArguments().getString(QUERY);
    }
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    
    return view;
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnSearchFragmentInteractionListener) {
      mListener = (OnSearchFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnSearchFragmentInteractionListener");
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }
  
  public interface OnSearchFragmentInteractionListener {
    void onCateringClick(Catering catering);
  }
}
