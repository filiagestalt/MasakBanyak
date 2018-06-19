package com.baskom.masakbanyak;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {
//  public static final String MASAKBANYAK_URL = "http://192.168.0.33:3000";
  public static final String MASAKBANYAK_URL = "http://masakbanyak.us-3.evennode.com";
  public static final int PICK_IMAGE_REQUEST_CODE = 1;
}
