package com.baskom.masakbanyak.ui.activity;

import android.content.Intent;
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
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.di.DaggerSessionComponent;
import com.baskom.masakbanyak.di.SessionComponent;
import com.baskom.masakbanyak.di.SessionModule;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.repository.CateringRepository;
import com.baskom.masakbanyak.repository.CustomerRepository;
import com.baskom.masakbanyak.ui.fragment.SearchFragment;
import com.baskom.masakbanyak.util.BottomNavigationHelper;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.ui.fragment.CateringsFragment;
import com.baskom.masakbanyak.ui.fragment.ProfileFragment;
import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.ui.fragment.TransactionsFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
    implements CateringsFragment.CateringsFragmentInteractionListener,
    SearchFragment.OnSearchFragmentInteractionListener,
    TransactionsFragment.TransactionFragmentInteractionListener,
    ProfileFragment.ProfileFragmentInteractionListener {
  
  @Inject
  CateringRepository mCateringRepository;
  @Inject
  CustomerRepository mCustomerRepository;
  
  private Toolbar mToolbar;
  private SearchView mSearchView;
  private BottomNavigationView mBottomNavigation;
  
  private CateringsFragment mCateringsFragment = CateringsFragment.newInstance();
  private TransactionsFragment mTransactionsFragment = TransactionsFragment.newInstance();
  private ProfileFragment mProfileFragment = ProfileFragment.newInstance();
  
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    Fragment fragment = manager.findFragmentById(R.id.content);
    
    switch (item.getItemId()) {
      case R.id.navigation_home:
        if (!(fragment instanceof CateringsFragment)) {
          transaction.replace(R.id.content, mCateringsFragment);
          transaction.addToBackStack(null);
          transaction.commit();
        }
        return true;
      case R.id.navigation_transaction:
        if (!(fragment instanceof TransactionsFragment)) {
          transaction.replace(R.id.content, mTransactionsFragment);
          transaction.addToBackStack(null);
          transaction.commit();
        }
        return true;
      case R.id.navigation_profile:
        if (!(fragment instanceof ProfileFragment)) {
          transaction.replace(R.id.content, mProfileFragment);
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
    
    SessionComponent sessionComponent = DaggerSessionComponent.builder()
        .sessionModule(new SessionModule())
        .applicationComponent(Components.getApplicationComponent())
        .build();
    Components.setSessionComponent(sessionComponent);
    Components.getSessionComponent().inject(this);
    
    mToolbar = findViewById(R.id.toolbar);
    mBottomNavigation = findViewById(R.id.navigation);
    
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(R.id.content, CateringsFragment.newInstance());
    transaction.commit();
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    
    BottomNavigationHelper.removeShiftMode(mBottomNavigation);
    mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
  public void onCateringsFragmentInteraction(Catering catering) {
    Intent cateringIntent = new Intent(this, CateringActivity.class);
    cateringIntent.putExtra("catering", catering);
    startActivity(cateringIntent);
  }
  
  @Override
  public void onTransactionsFragmentInteraction(Order order) {
    Intent transactionIntent = new Intent(this, TransactionActivity.class);
    transactionIntent.putExtra("order", order);
    startActivity(transactionIntent);
  }
  
  @Override
  public void onProfileFragmentInteraction() {
  
  }
  
  @Override
  public void onCateringClick(Catering catering) {
    Intent cateringIntent = new Intent(this, CateringActivity.class);
    cateringIntent.putExtra("catering", catering);
    startActivity(cateringIntent);
  }
}