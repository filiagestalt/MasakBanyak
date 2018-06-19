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
  
  public static void verifyTokenAndExecuteCall(
      final Context context,
      final UnauthorizedServiceCall canMakeServiceCall
  ) {
    final SharedPreferences sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_preferences_key),
        Context.MODE_PRIVATE
    );
    final SharedPreferences.Editor editor = sharedPref.edit();
    
    String access_token_old = sharedPref.getString("access_token", null);
    String refresh_token = sharedPref.getString("refresh_token", null);
    
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Constants.MASAKBANYAK_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    final MasakBanyakWebService service = retrofit.create(MasakBanyakWebService.class);
    
    JWT jwt = new JWT(access_token_old);
    
    if (jwt.isExpired(9)) {
      Call<JsonObject> call = service.refresh(
          refresh_token,
          jwt.getClaim("customer_id").asString()
      );
      
      call.enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
          if (response.isSuccessful()) {
            String access_token = response.body().get("access_token").getAsString();
            editor.putString("access_token", access_token).apply();
            
            canMakeServiceCall.executeCall(access_token, service);
          } else {
            try {
              Toast.makeText(
                  context,
                  response.errorBody().string(),
                  Toast.LENGTH_LONG
              ).show();
            } catch (IOException e) {
              Toast.makeText(
                  context,
                  e.toString(),
                  Toast.LENGTH_LONG
              ).show();
            }
          }
        }
        
        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
          Toast.makeText(
              context,
              t.toString(),
              Toast.LENGTH_LONG
          ).show();
        }
      });
    } else {
      canMakeServiceCall.executeCall(access_token_old, service);
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
