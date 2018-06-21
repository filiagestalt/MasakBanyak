package com.baskom.masakbanyak;

import android.app.Application;

import com.baskom.masakbanyak.di.ApplicationComponent;
import com.baskom.masakbanyak.di.ApplicationModule;
import com.baskom.masakbanyak.di.DaggerApplicationComponent;
import com.baskom.masakbanyak.di.NetworkModule;
import com.baskom.masakbanyak.di.StorageModule;

public class MasakBanyakApplication extends Application {
  private static MasakBanyakApplication instance;
  
  private ApplicationComponent applicationComponent;
  
  @Override
  public void onCreate() {
    super.onCreate();
    
    instance = this;
    
    applicationComponent = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .networkModule(new NetworkModule())
        .storageModule(new StorageModule())
        .build();
  }
  
  public static MasakBanyakApplication getInstance() {
    return instance;
  }
  
  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
}
