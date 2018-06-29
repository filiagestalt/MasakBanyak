package com.baskom.masakbanyak.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.di.SessionScope;
import com.baskom.masakbanyak.ui.activity.LoginActivity;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.util.Util;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SessionScope
public class CustomerRepository {
  private MutableLiveData<Customer> customerLiveData = new MutableLiveData<>();
  private MutableLiveData<Util.Event<String>> notificationEventLiveData = new MutableLiveData<>();
  
  private Context context;
  private SharedPreferences preferences;
  private JWT jwt;
  private MasakBanyakWebService webService;
  
  @Inject
  public CustomerRepository(Context context, SharedPreferences preferences, JWT jwt, MasakBanyakWebService webService) {
    this.context = context;
    this.preferences = preferences;
    this.jwt = jwt;
    this.webService = webService;
    
    refreshCustomer();
  }
  
  public LiveData<Customer> getCustomerLiveData() {
    return customerLiveData;
  }
  
  public LiveData<Util.Event<String>> getNotificationEventLiveData() {
    return notificationEventLiveData;
  }
  
  public void refreshCustomer() {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer " + access_token;
      String customer_id = jwt.getClaim("customer_id").asString();
      
      webservice.getCustomer(authorization, customer_id).enqueue(new Callback<Customer>() {
        @Override
        public void onResponse(Call<Customer> call, Response<Customer> response) {
          if (response.isSuccessful()) {
            customerLiveData.postValue(response.body());
          }
        }
        
        @Override
        public void onFailure(Call<Customer> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
    });
  }
  
  public void uploadCustomerAvatar(Customer customer, String filename, byte[] file) {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer " + access_token;
      
      RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
      MultipartBody.Part formData = MultipartBody.Part.createFormData("avatar", filename, requestBody);
      
      Call<ResponseBody> call = webservice.uploadAvatar(authorization, customer.getCustomer_id(), formData);
      
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.isSuccessful()) {
            try {
              notificationEventLiveData.postValue(new Util.Event<>(response.body().string()));
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          refreshCustomer();
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
    });
  }
  
  public void updateCustomer(Customer customer) {
    Util.authorizeAndExecuteCall(preferences, jwt, webService, (access_token, webservice) -> {
      String authorization = "Bearer " + access_token;
      
      Call<ResponseBody> call = webservice.updateProfile(
          authorization,
          customer.getCustomer_id(),
          customer.getName(),
          customer.getPhone(),
          customer.getEmail()
      );
      
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.isSuccessful()) {
            try {
              notificationEventLiveData.postValue(new Util.Event<>(response.body().string()));
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          refreshCustomer();
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          Log.d("Network Call Failure", t.toString());
        }
      });
    });
  }
  
  public void logout() {
    String refreshToken = preferences.getString("refresh_token", null);
    String customer_id = jwt.getClaim("customer_id").asString();
    
    Call<ResponseBody> call = webService.logout(refreshToken, customer_id);
    
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          SharedPreferences.Editor editor = preferences.edit();
          editor.clear();
          editor.apply();
          
          Intent intent = new Intent(context, LoginActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          context.startActivity(intent);
        }
      }
      
      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("Network Call Failure", t.toString());
      }
    });
  }
}
