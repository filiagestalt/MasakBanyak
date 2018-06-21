package com.baskom.masakbanyak.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.repository.CateringRepository;
import com.baskom.masakbanyak.repository.CustomerRepository;
import com.baskom.masakbanyak.repository.OrderRepository;
import com.baskom.masakbanyak.util.BottomNavigationHelper;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.ui.fragment.CateringsFragment;
import com.baskom.masakbanyak.ui.fragment.ProfileFragment;
import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.ui.fragment.TransactionsFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
    implements CateringsFragment.CateringsFragmentInteractionListener,
    TransactionsFragment.TransactionFragmentInteractionListener,
    ProfileFragment.ProfileFragmentInteractionListener {
  
  @Inject
  CateringRepository mCateringRepository;
  //@Inject
  //OrderRepository mOrderRepository;
  @Inject
  CustomerRepository mCustomerRepository;
  
  private Toolbar mToolbar;
  private SearchView mSearchView;
  private BottomNavigationView mBottomNavigation;
  
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    Fragment fragment = manager.findFragmentById(R.id.content);
    
    switch (item.getItemId()) {
      case R.id.navigation_home:
        if (!(fragment instanceof CateringsFragment)) {
          transaction.replace(R.id.content, CateringsFragment.newInstance());
          transaction.addToBackStack(null);
          transaction.commit();
        }
        return true;
      case R.id.navigation_transaction:
        if (!(fragment instanceof TransactionsFragment)) {
          transaction.replace(R.id.content, TransactionsFragment.newInstance());
          transaction.addToBackStack(null);
          transaction.commit();
        }
        return true;
      case R.id.navigation_profile:
        if (!(fragment instanceof ProfileFragment)) {
          transaction.replace(R.id.content, ProfileFragment.newInstance());
          transaction.addToBackStack(null);
          transaction.commit();
        }
        return true;
    }
    return false;
  };
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mToolbar = findViewById(R.id.toolbar);
    mBottomNavigation = findViewById(R.id.navigation);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    
    BottomNavigationHelper.removeShiftMode(mBottomNavigation);
    mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(R.id.content, CateringsFragment.newInstance());
    transaction.commit();
  }
  
  @Override
  public void onBackPressed() {
    if (!mSearchView.isIconified()) {
      mSearchView.onActionViewCollapsed();
    } else {
      super.onBackPressed();
      
      FragmentManager manager = getSupportFragmentManager();
      Fragment fragment = manager.findFragmentById(R.id.content);
      if (fragment instanceof CateringsFragment) {
        mBottomNavigation.setSelectedItemId(R.id.navigation_home);
      } else if (fragment instanceof TransactionsFragment) {
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
    View searchViewUnderline = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
    searchViewUnderline.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        // TODO: Make SearchFragment and call it from here
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
  public void onTransactionFragmentInteraction() {
  
  }
  
  @Override
  public void onProfileFragmentInteraction() {
  
  }
  
}