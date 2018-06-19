package com.baskom.masakbanyak.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.util.Util;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CateringsRepository {
  private MutableLiveData<ArrayList<Catering>> cateringsLiveData = new MutableLiveData<>();
  
  private SharedPreferences preferences;
  private JWT jwt;
  private MasakBanyakWebService webService;
  
  @Inject
  public CateringsRepository(SharedPreferences preferences, JWT jwt, MasakBanyakWebService webService) {
    this.preferences = preferences;
    this.jwt = jwt;
    this.webService = webService;
  }
  
  public LiveData<ArrayList<Catering>> getCateringsLiveData() {
    refreshCaterings();
    return cateringsLiveData;
  }
  
  public void refreshCaterings() {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer " + access_token;
      
      Call<ArrayList<Catering>> call = webservice.caterings(authorization);
      
      call.enqueue(new Callback<ArrayList<Catering>>() {
        @Override
        public void onResponse(Call<ArrayList<Catering>> call, Response<ArrayList<Catering>> response) {
          if (response.isSuccessful()) {
            cateringsLiveData.postValue(response.body());
          }
        }
        
        @Override
        public void onFailure(Call<ArrayList<Catering>> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
    });
  }
}