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
    public static final String URL = "http://192.168.0.33:3000";

    public static void verifyToken(final Context context, final CanMakeServiceCall canMakeServiceCall){
        final SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.app_preference_key),
                Context.MODE_PRIVATE
        );
        final SharedPreferences.Editor editor = sharedPref.edit();

        String access_token_old = sharedPref.getString("access_token", null);
        String refresh_token = sharedPref.getString("refresh_token", null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MasakBanyakService service = retrofit.create(MasakBanyakService.class);

        JWT jwt = new JWT(access_token_old);

        if(jwt.isExpired(9)){
            Call<JsonObject> call = service.refresh(
                    refresh_token,
                    jwt.getClaim("customer_id").asString()
            );

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        String access_token = response.body().get("access_token").getAsString();
                        editor.putString("access_token", access_token).apply();

                        canMakeServiceCall.makeCall(context, service, access_token);
                    }else{
                        try {
                            Toast.makeText(
                                    context,
                                    response.errorBody().string(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        } catch (IOException e) {
                            Toast.makeText(
                                    context,
                                    e.toString(),
                                    Toast.LENGTH_SHORT
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
        }else{
            canMakeServiceCall.makeCall(context, service, access_token_old);
        }
    }
}
