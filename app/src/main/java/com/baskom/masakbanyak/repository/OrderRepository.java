package com.baskom.masakbanyak.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.di.SessionScope;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.util.Util;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SessionScope
public class OrderRepository {
  private MutableLiveData<ArrayList<Order>> ordersLiveData = new MutableLiveData<>();
  private MutableLiveData<Util.Event<String>> notificationEventLiveData = new MutableLiveData<>();
  
  private SharedPreferences preferences;
  private JWT jwt;
  private MasakBanyakWebService webService;
  
  @Inject
  public OrderRepository(SharedPreferences preferences, JWT jwt, MasakBanyakWebService webService) {
    this.preferences = preferences;
    this.jwt = jwt;
    this.webService = webService;
    
    refreshOrders();
  }
  
  public LiveData<ArrayList<Order>> getOrdersLiveData(){
    return ordersLiveData;
  }
  
  public MutableLiveData<Util.Event<String>> getNotificationEventLiveData() {
    return notificationEventLiveData;
  }
  
  public void refreshOrders() {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer "+access_token;
      String customer_id = jwt.getClaim("customer_id").asString();
  
      Call<ArrayList<Order>> call = webservice.getOrdersByCustomer(authorization, customer_id);
      
      call.enqueue(new Callback<ArrayList<Order>>() {
        @Override
        public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
          if(response.isSuccessful()){
            ordersLiveData.postValue(response.body());
          }
        }
  
        @Override
        public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
    });
  }
  
  public void refundOrder(Order order) {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer " + access_token;
      
      Call<ResponseBody> call = webservice.refundOrder(authorization, order.getOrder_id());
      
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.isSuccessful()) {
            notificationEventLiveData.postValue(new Util.Event<>("Pesanan telah berhasil dibatalkan."));
          } else {
            try {
              notificationEventLiveData.postValue(new Util.Event<>(response.errorBody().string()));
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
      
    });
  }
}
