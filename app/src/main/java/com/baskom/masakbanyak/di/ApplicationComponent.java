package com.baskom.masakbanyak.di;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.ui.activity.LoginActivity;
import com.baskom.masakbanyak.ui.activity.RegisterActivity;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class, StorageModule.class, NetworkModule.class})
public interface ApplicationComponent {
  void inject(MasakBanyakApplication application);
  
  void inject(LoginActivity activity);
  
  void inject(RegisterActivity activity);
  
  Context applicationContext();
  
  SharedPreferences.Editor preferencesEditor();
  
  SharedPreferences preferences();
  
  Retrofit retrofit();
  
  MasakBanyakWebService webService();
}
