package com.baskom.masakbanyak.di;

import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.Constants;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    public Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.MASAKBANYAK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public MasakBanyakWebService provideWebService(Retrofit retrofit){
        return retrofit.create(MasakBanyakWebService.class);
    }

    @Provides
    public JWT provideJWT(SharedPreferences preferences){
        String access_token = preferences.getString("access_token", "");
        return new JWT(access_token);
    }
}
