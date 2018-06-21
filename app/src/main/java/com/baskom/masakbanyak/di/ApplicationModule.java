package com.baskom.masakbanyak.di;

import android.content.Context;

import com.baskom.masakbanyak.MasakBanyakApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
  private MasakBanyakApplication application;
  
  public ApplicationModule(MasakBanyakApplication application) {
    this.application = application;
  }
  
  @Provides
  @Singleton
  public Context provideApplicationContext() {
    return application;
  }
  
}
