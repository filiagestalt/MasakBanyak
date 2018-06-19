package com.baskom.masakbanyak.di;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.ui.activity.CateringActivity;
import com.baskom.masakbanyak.ui.activity.MainActivity;
import com.baskom.masakbanyak.ui.activity.OrderActivity;
import com.baskom.masakbanyak.ui.fragment.CateringsFragment;
import com.baskom.masakbanyak.ui.fragment.ProfileFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, StorageModule.class, NetworkModule.class, ViewModelModule.class})
public interface ApplicationComponent {
  void inject(MasakBanyakApplication application);
  void inject(MainActivity activity);
  void inject(CateringActivity activity);
  void inject(OrderActivity activity);
  void inject(CateringsFragment fragment);
  void inject(ProfileFragment fragment);
}
