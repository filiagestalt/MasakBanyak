package com.baskom.masakbanyak.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.util.Util;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CateringRepository {
  private MutableLiveData<ArrayList<Catering>> cateringsLiveData = new MutableLiveData<>();
  private MutableLiveData<ArrayList<Packet>> packetsLiveData = new MutableLiveData<>();
  
  private SharedPreferences preferences;
  private JWT jwt;
  private MasakBanyakWebService webService;
  
  @Inject
  public CateringRepository(SharedPreferences preferences, JWT jwt, MasakBanyakWebService webService) {
    this.preferences = preferences;
    this.jwt = jwt;
    this.webService = webService;
    
    refreshCaterings();
  }
  
  public LiveData<ArrayList<Catering>> getCateringsLiveData() {
    return cateringsLiveData;
  }
  
  public LiveData<ArrayList<Packet>> getPacketsLiveData(Catering catering) {
    refreshPackets(catering);
    return packetsLiveData;
  }
  
  public void refreshCaterings() {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer " + access_token;
      
      Call<ArrayList<Catering>> call = webservice.getCaterings(authorization);
      
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
  
  public void refreshPackets(Catering catering){
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (String access_token, MasakBanyakWebService service) -> {
      String authorization = "Bearer " + access_token;
      
      Call<ArrayList<Packet>> call = service.getPackets(authorization, catering.getCatering_id());
      
      call.enqueue(new Callback<ArrayList<Packet>>() {
        @Override
        public void onResponse(Call<ArrayList<Packet>> call, Response<ArrayList<Packet>> response) {
          if (response.isSuccessful()) {
            packetsLiveData.postValue(response.body());
          }
        }
        
        @Override
        public void onFailure(Call<ArrayList<Packet>> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
    });
  }
}