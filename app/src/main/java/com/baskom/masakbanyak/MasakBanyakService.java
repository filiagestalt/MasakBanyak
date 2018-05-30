package com.baskom.masakbanyak;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface MasakBanyakService {
    @FormUrlEncoded
    @POST("/auth/customer/register")
    Call<ResponseBody> register(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/auth/customer/login")
    Call<JsonObject> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/auth/customer/refresh")
    Call<JsonObject> refresh(
            @Field("refresh_token") String refreshToken,
            @Field("customer_id") String customerId
    );

    @FormUrlEncoded
    @POST("/auth/customer/logout")
    Call<ResponseBody> logout(
            @Field("refresh_token") String refreshToken,
            @Field("customer_id") String customer_id
    );

    @GET("/caterings")
    Call<ArrayList<Catering>> caterings(@Header("Authorization") String authorization);

    @GET("/customers/profile")
    Call<Customer> profile(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @PUT("/customers/profile/update")
    Call<ResponseBody> updateProfile(
            @Header("Authorization") String authorization,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email
    );

    @Multipart
    @POST("/customers/profile/avatar")
    Call<ResponseBody> uploadAvatar(
            @Header("Authorization") String authorization,
            @Part("avatar") RequestBody image
    );

}
