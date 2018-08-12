package com.baskom.masakbanyak.di;

import com.baskom.masakbanyak.ui.activity.CateringActivity;
import com.baskom.masakbanyak.ui.activity.MainActivity;
import com.baskom.masakbanyak.ui.activity.MakeOrderActivity;
import com.baskom.masakbanyak.ui.activity.TransactionActivity;
import com.baskom.masakbanyak.ui.fragment.CateringsFragment;
import com.baskom.masakbanyak.ui.fragment.ProfileFragment;
import com.baskom.masakbanyak.ui.fragment.SearchFragment;
import com.baskom.masakbanyak.ui.fragment.TransactionsFragment;

import dagger.Component;

@SessionScope
@Component(modules = {SessionModule.class, ViewModelModule.class}, dependencies = {ApplicationComponent.class})
public interface SessionComponent {
  void inject(MainActivity activity);
  
  void inject(CateringActivity activity);
  
  void inject(MakeOrderActivity activity);
  
  void inject(TransactionActivity activity);
  
  void inject(CateringsFragment fragment);
  
  void inject(TransactionsFragment fragment);
  
  void inject(ProfileFragment fragment);
  
  void inject(SearchFragment fragment);
}
