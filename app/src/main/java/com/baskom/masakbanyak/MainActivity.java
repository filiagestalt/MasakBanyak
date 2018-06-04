package com.baskom.masakbanyak;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.HomeFragmentInteractionListener,
        TransactionFragment.TransactionFragmentInteractionListener,
        ProfileFragment.ProfileFragmentInteractionListener {

    private ArrayList<Catering> mCaterings = new ArrayList<>();

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private BottomNavigationView mBottomNavigation;

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
                        transaction.replace(R.id.content, HomeFragment.newInstance());
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

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, HomeFragment.newInstance());
        transaction.commit();
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
                // TODO: Make SearchFragment and call it from here
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                ArrayList<Catering> filteredCateringList = new ArrayList<>(Collections2
//                        .filter(mCaterings, new CateringFilter(query)));
//                transaction.replace(R.id.content, HomeFragment.newInstance(filteredCateringList));
//                transaction.addToBackStack(null);
//                transaction.commit();

                return false;
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
        Intent cateringIntent = new Intent(this, CateringActivity.class);
        cateringIntent.putExtra("catering", catering);
        startActivity(cateringIntent);
    }

    @Override
    public void onTransactionFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

}