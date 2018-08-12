package com.baskom.masakbanyak.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.R;
import com.google.gson.JsonObject;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
  
  @Inject
  SharedPreferences preferences;
  @Inject
  MasakBanyakWebService webService;
  
  private CoordinatorLayout mCoordinatorLayout;
  private AutoCompleteTextView mEmail;
  private AutoCompleteTextView mPassword;
  private Button mButtonLogin;
  private Button mButtonRegister;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    
    Components.getApplicationComponent().inject(this);
    
    if (preferences.contains("access_token") && preferences.contains("refresh_token")) {
      Intent mainIntent = new Intent(this, MainActivity.class);
      startActivity(mainIntent);
      finish();
    }
    
    mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
    mEmail = findViewById(R.id.et_email);
    mPassword = findViewById(R.id.et_password);
    mButtonLogin = findViewById(R.id.button_login);
    mButtonRegister = findViewById(R.id.button_register);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    mButtonLogin.setOnClickListener(v -> {
      String email = mEmail.getText().toString();
      String password = mPassword.getText().toString();
      
      webService.login(email, password).enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
          if (response.code() == 200) {
            SharedPreferences.Editor editor = preferences.edit();
            
            editor.putString("access_token", response.body().get("access_token").getAsString());
            editor.putString("refresh_token", response.body().get("refresh_token").getAsString());
            editor.apply();
            
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
          } else {
            try {
              showResponse(response.errorBody().string());
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        
        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
          showResponse(t.toString());
        }
      });
    });
    
    mButtonRegister.setOnClickListener(v -> {
      Intent registerIntent = new Intent(this, RegisterActivity.class);
      startActivity(registerIntent);
    });
  }
  
  private void showResponse(String response) {
    Snackbar.make(mCoordinatorLayout, response, Snackbar.LENGTH_SHORT).show();
  }
}
