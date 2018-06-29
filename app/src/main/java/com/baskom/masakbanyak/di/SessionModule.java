package com.baskom.masakbanyak.di;

import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;

import dagger.Module;
import dagger.Provides;

@Module
public class SessionModule {
  @Provides
  public JWT provideJWT(SharedPreferences preferences) {
    String access_token = preferences.getString("access_token", "");
    return new JWT(access_token);
  }
}