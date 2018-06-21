package com.baskom.masakbanyak.di;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.ui.activity.CateringActivity;
import com.baskom.masakbanyak.ui.activity.LoginActivity;
import com.baskom.masakbanyak.ui.activity.MainActivity;
import com.baskom.masakbanyak.ui.activity.OrderActivity;
import com.baskom.masakbanyak.ui.activity.RegisterActivity;
import com.baskom.masakbanyak.ui.fragment.CateringsFragment;
import com.baskom.masakbanyak.ui.fragment.ProfileFragment;
import com.baskom.masakbanyak.ui.fragment.TransactionsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, StorageModule.class, NetworkModule.class, ViewModelModule.class, CustomerModule.class})
public interface ApplicationComponent {
  void inject(MasakBanyakApplication application);
  void inject(LoginActivity activity);
  void inject(RegisterActivity activity);
  void inject(MainActivity activity);
  void inject(CateringActivity activity);
  void inject(OrderActivity activity);
  void inject(CateringsFragment fragment);
  void inject(TransactionsFragment fragment);
  void inject(ProfileFragment fragment);
}
