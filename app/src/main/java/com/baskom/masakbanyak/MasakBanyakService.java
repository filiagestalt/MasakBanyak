package com.baskom.masakbanyak;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MasakBanyakService {
    @FormUrlEncoded
    @POST("auth/customer/register")
    Call<ResponseBody> register(@Field("name") String name, @Field("phone") String phone,
                                @Field
            ("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/customer/login")
    Call<JsonObject> login(@Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/customer/refresh")
    Call<JsonObject> refresh(@Field("refresh_token") String refreshToken, @Field("customer_id")
                             String customerId);

    @FormUrlEncoded
    @POST("auth/customer/logout")
    Call<ResponseBody> logout(@Field("refresh_token") String refreshToken, @Field("customer_id")
            String customerId);

    @GET("caterings")
    Call<ArrayList<Catering>> caterings(@Header("Authorization") String authorization);
}
