package com.baskom.masakbanyak.webservice;

import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MasakBanyakWebService {
  @FormUrlEncoded
  @POST("/auth/customers/register")
  Call<ResponseBody> register(
      @Field("name") String name,
      @Field("phone") String phone,
      @Field("email") String email,
      @Field("password") String password
  );
  
  @FormUrlEncoded
  @POST("/auth/customers/login")
  Call<JsonObject> login(
      @Field("email") String email,
      @Field("password") String password
  );
  
  @FormUrlEncoded
  @POST("/auth/customers/refresh")
  Call<JsonObject> refresh(
      @Field("refresh_token") String refreshToken,
      @Field("customer_id") String customerId
  );
  
  @FormUrlEncoded
  @POST("/auth/customers/logout")
  Call<ResponseBody> logout(
      @Field("refresh_token") String refreshToken,
      @Field("customer_id") String customer_id
  );
  
  @GET("/customers/{id}")
  Call<Customer> getCustomer(
      @Header("Authorization") String authorization,
      @Path("id") String customer_id
  );
  
  @FormUrlEncoded
  @PUT("/customers/{id}/update")
  Call<ResponseBody> updateProfile(
      @Header("Authorization") String authorization,
      @Path("id") String customer_id,
      @Field("name") String name,
      @Field("phone") String phone,
      @Field("email") String email
  );
  
  @Multipart
  @POST("/customers/{id}/avatar")
  Call<ResponseBody> uploadAvatar(
      @Header("Authorization") String authorization,
      @Path("id") String customer_id,
      @Part() MultipartBody.Part image
  );
  
  @GET("/caterings")
  Call<ArrayList<Catering>> getCaterings(@Header("Authorization") String authorization);
  
  @GET("/caterings/{id}/packets")
  Call<ArrayList<Packet>> getPackets(@Header("Authorization") String authorization, @Path("id") String catering_id);
  
  @GET("/orders")
  Call<ArrayList<Order>> getOrders(@Header("Authorization") String authorization, @Query("customer_id") String customer_id);
}
