package com.baskom.masakbanyak.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.R;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class LoginActivity extends AppCompatActivity {
  private AutoCompleteTextView mEmail;
  private AutoCompleteTextView mPassword;
  private Button mBtnLogin;
  private FloatingActionButton mButtonRegister;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loginCheck();
    
    setContentView(R.layout.activity_login);
    mEmail = findViewById(R.id.et_email);
    mPassword = findViewById(R.id.et_password);
    mBtnLogin = findViewById(R.id.button_login);
    mButtonRegister = findViewById(R.id.fab);
    
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MASAKBANYAK_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    final MasakBanyakWebService service = retrofit.create(MasakBanyakWebService.class);
    
    mBtnLogin.setOnClickListener(v -> {
      String email = mEmail.getText().toString();
      String password = mPassword.getText().toString();
      service.login(email, password).enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
          if (response.code() == 200) {
            SharedPreferences sharedPref = v
                .getContext()
                .getSharedPreferences(
                    getString(R.string.app_preferences_key),
                    Context.MODE_PRIVATE
                );
            SharedPreferences.Editor editor = sharedPref.edit();
            
            editor.putString(
                "access_token",
                response.body()
                    .get("access_token")
                    .getAsString()
            );
            editor.putString("refresh_token",
                response.body()
                    .get("refresh_token")
                    .getAsString()
            );
            editor.commit();
            
            Intent intent = new Intent(
                LoginActivity.this,
                MainActivity.class
            );
            startActivity(intent);
            finish();
          } else {
            try {
              Toast.makeText(LoginActivity.this,
                  response.errorBody().string(),
                  Toast.LENGTH_LONG).show();
            } catch (IOException e) {
              Toast.makeText(LoginActivity.this,
                  e.toString(),
                  Toast.LENGTH_LONG).show();
            }
          }
        }
        
        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
          Toast.makeText(LoginActivity.this,
              t.toString(),
              Toast.LENGTH_LONG).show();
        }
      });
    });
    
    mButtonRegister.setOnClickListener(v -> {
      Intent registerIntent = new Intent(this, RegisterActivity.class);
      startActivity(registerIntent);
    });
  }
  
  public void loginCheck() {
    Intent intent;
    SharedPreferences sharedPref = getSharedPreferences(
        getString(R.string.app_preferences_key),
        Context.MODE_PRIVATE
    );
    
    if (sharedPref.contains("access_token") && sharedPref.contains("refresh_token")) {
      intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }
    
  }
}
