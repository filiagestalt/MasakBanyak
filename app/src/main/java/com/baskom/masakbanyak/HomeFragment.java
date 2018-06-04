package com.baskom.masakbanyak;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baskom.masakbanyak.Constants.verifyTokenAndExecuteCall;


public class HomeFragment extends Fragment {

    private ArrayList<Catering> mCaterings = new ArrayList<>();

    private ProgressBar mProgressBar;
    private CateringsAdapter mCateringsAdapter;
    private RecyclerView mCateringsRecyclerView;

    private HomeFragmentInteractionListener mListener;
    private CanMakeServiceCall mCateringsCall = new CanMakeServiceCall() {
        @Override
        public void makeCall(MasakBanyakService service, String access_token) {
            Call<ArrayList<Catering>> call = service.caterings("Bearer "+access_token);

            call.enqueue(new Callback<ArrayList<Catering>>() {
                @Override
                public void onResponse(Call<ArrayList<Catering>> call, Response<ArrayList<Catering>> response) {
                    if(response.isSuccessful()){
                        mCaterings = response.body();

                        mCateringsAdapter.setCaterings(mCaterings);
                        mCateringsAdapter.notifyDataSetChanged();

                        mCateringsRecyclerView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }else{
                        try {
                            Toast.makeText(
                                    getContext(),
                                    response.errorBody().string(),
                                    Toast.LENGTH_LONG
                            ).show();
                        } catch (IOException e) {
                            Toast.makeText(
                                    getContext(),
                                    e.toString(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Catering>> call, Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    };

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mCateringsRecyclerView = view.findViewById(R.id.caterings);
        mProgressBar = view.findViewById(R.id.progress_bar);

        mCateringsAdapter = new CateringsAdapter(mCaterings, mListener);
        mCateringsRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        view.getContext(),
                        Configuration.ORIENTATION_PORTRAIT,
                        false
                )
        );
        mCateringsRecyclerView.setAdapter(mCateringsAdapter);
        mCateringsRecyclerView.setVisibility(View.INVISIBLE);

        FoldingCube foldingCube = new FoldingCube();
        foldingCube.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mProgressBar.setIndeterminateDrawable(foldingCube);

        verifyTokenAndExecuteCall(getContext().getApplicationContext(), mCateringsCall);

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
