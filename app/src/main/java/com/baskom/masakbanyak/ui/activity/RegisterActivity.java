package com.baskom.masakbanyak.ui.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.R;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
  
  @Inject
  MasakBanyakWebService webService;
  
  private CoordinatorLayout mCoordinatorLayout;
  private AutoCompleteTextView mName;
  private AutoCompleteTextView mEmail;
  private AutoCompleteTextView mPhone;
  private AutoCompleteTextView mPassword;
  private AutoCompleteTextView mPasswordConfirm;
  private Button mButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    
    Components.getApplicationComponent().inject(this);
    
    mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
    mName = findViewById(R.id.et_nama_reg);
    mPhone = findViewById(R.id.et_phone_reg);
    mEmail = findViewById(R.id.et_email_reg);
    mPassword = findViewById(R.id.et_password_reg);
    mPasswordConfirm = findViewById(R.id.et_konfirmasiPassword_reg);
    mButton = findViewById(R.id.btn_daftar);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    mButton.setOnClickListener(v -> {
      if (!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())) {
        showResponse("Kata sandi tidak sama.");
      } else {
        webService.register(mName.getText().toString(), mPhone.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString()).enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
              if (response.isSuccessful()) {
                showResponse(response.body().string());
              } else {
                showResponse(response.errorBody().string());
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          
          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
            showResponse(t.toString());
          }
        });
      }
    });
  }
  
  private void showResponse(String error) {
    Snackbar.make(mCoordinatorLayout, error, Snackbar.LENGTH_SHORT).show();
  }
}
