package com.baskom.masakbanyak.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.Constants;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.R;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
  
  public static void authorizeAndExecuteCall(SharedPreferences preferences, JWT jwt, MasakBanyakWebService webService, UnauthorizedServiceCall unauthorizedServiceCall) {
    SharedPreferences.Editor preferencesEditor = preferences.edit();
    String customer_id = jwt.getClaim("customer_id").asString();
    String access_token = preferences.getString("access_token", "");
    String refresh_token = preferences.getString("refresh_token", "");
    
    if (jwt.isExpired(9)) {
      Call<JsonObject> call = webService.refresh(refresh_token, customer_id);
      
      call.enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
          if (response.isSuccessful()) {
            String new_access_token = response.body().get("access_token").getAsString();
            preferencesEditor.putString("access_token", new_access_token).apply();
            
            unauthorizedServiceCall.executeCall(new_access_token, webService);
          }
        }
        
        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
        
        }
      });
    } else {
      unauthorizedServiceCall.executeCall(access_token, webService);
    }
  }
  
  public static class Event<T> {
    private T content;
  
    private boolean hasBeenHandled = false;
    
    public Event(T content) {
      this.content = content;
    }
    
    public T getContentIfNotHandled(){
      if(hasBeenHandled){
        return null;
      }
      
      hasBeenHandled = true;
      return content;
    }
  }
  
  @FunctionalInterface
  public interface UnauthorizedServiceCall {
    void executeCall(String access_token, MasakBanyakWebService webservice);
  }
  
}
