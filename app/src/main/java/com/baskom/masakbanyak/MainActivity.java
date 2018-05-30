package com.baskom.masakbanyak;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.common.collect.Collections2;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baskom.masakbanyak.Constants.verifyToken;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.HomeFragmentInteractionListener,
        TransactionFragment.TransactionFragmentInteractionListener,
        CateringFragment.CateringFragmentInteractionListener,
        ProfileFragment.ProfileFragmentInteractionListener,
        PacketFragment.PacketFragmentInteractionListener {

    private ArrayList<Catering> mCaterings = new ArrayList<>();

    private CanMakeServiceCall mMakeCateringsServiceCall = new CanMakeServiceCall() {
        @Override
        public void makeCall(final Context context, MasakBanyakService service, String access_token) {
            Call<ArrayList<Catering>> call = service.caterings("Bearer "+access_token);

            call.enqueue(new Callback<ArrayList<Catering>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Catering>> call, Response<ArrayList<Catering>> response) {
                            if(response.code() == 200){
                                mCaterings = response.body();
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.content, HomeFragment.newInstance(response.body()));
                                transaction.commit();
                                mProgressBar.setVisibility(View.GONE);
                            }else{
                                try {
                                    Toast.makeText(
                                            context,
                                            response.errorBody().string(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                } catch (IOException e) {
                                    Toast.makeText(
                                            context,
                                            e.toString(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Catering>> call, Throwable t) {
                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
        }
    };

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigation;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = manager.findFragmentById(R.id.content);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!(fragment instanceof HomeFragment)) {
                        transaction.replace(R.id.content, HomeFragment.newInstance(mCaterings));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_transaction:
                    if (!(fragment instanceof TransactionFragment)) {
                        transaction.replace(R.id.content, TransactionFragment.newInstance("01", "02"));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_profile:
                    if (!(fragment instanceof ProfileFragment)) {
                        transaction.replace(R.id.content, ProfileFragment.newInstance("01", "02"));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigation = findViewById(R.id.navigation);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationHelper.removeShiftMode(mBottomNavigation);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mProgressBar = findViewById(R.id.progress_bar);
        FoldingCube foldingCube = new FoldingCube();
        foldingCube.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mProgressBar.setIndeterminateDrawable(foldingCube);

        verifyToken(this, mMakeCateringsServiceCall);
    }

    @Override
    public void onBackPressed() {
        if(!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
        }else {
            super.onBackPressed();

            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.content);
            if (fragment instanceof HomeFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_home);
            } else if (fragment instanceof TransactionFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_transaction);
            } else if (fragment instanceof ProfileFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_profile);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ArrayList<Catering> filteredCateringList = new ArrayList<>(Collections2
                        .filter(mCaterings, new CateringFilter(query)));
                transaction.replace(R.id.content, HomeFragment.newInstance(filteredCateringList));
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHomeFragmentInteraction(Catering catering) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, CateringFragment.newInstance(catering));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCateringFragmentInteraction(Packet packet) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment previousFragment = manager.findFragmentByTag("Packet");
        if(previousFragment != null){
            transaction.remove(previousFragment);
        }
        transaction.addToBackStack(null);
        PacketFragment fragment = PacketFragment.newInstance(packet);
        fragment.show(transaction, "Packet");
    }

    @Override
    public void onPacketFragmentInteraction(Packet packet) {

    }

    @Override
    public void onTransactionFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

}