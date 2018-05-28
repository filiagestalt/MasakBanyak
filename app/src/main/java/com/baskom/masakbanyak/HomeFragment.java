package com.baskom.masakbanyak;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM = "Catering List";

    private ArrayList<Catering> mCateringList = new ArrayList<>();

    private HomeFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private CateringListAdapter mCateringListAdapter;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(ArrayList<Catering> cateringList) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, cateringList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCateringList = (ArrayList<Catering>) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.catering_list);
        mCateringListAdapter = new CateringListAdapter(mCateringList, mListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                Configuration.ORIENTATION_PORTRAIT, false));
        mRecyclerView.setAdapter(mCateringListAdapter);

        return view;
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
