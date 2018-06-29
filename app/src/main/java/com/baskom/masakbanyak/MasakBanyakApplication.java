package com.baskom.masakbanyak;

import android.app.Application;

import com.baskom.masakbanyak.di.ApplicationComponent;
import com.baskom.masakbanyak.di.ApplicationModule;
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.di.DaggerApplicationComponent;
import com.baskom.masakbanyak.di.NetworkModule;
import com.baskom.masakbanyak.di.StorageModule;

public class MasakBanyakApplication extends Application {
  
  @Override
  public void onCreate() {
    super.onCreate();
    
    ApplicationComponent applicationComponent = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .networkModule(new NetworkModule())
        .storageModule(new StorageModule())
        .build();
  
    Components.setApplicationComponent(applicationComponent);
  }
}
