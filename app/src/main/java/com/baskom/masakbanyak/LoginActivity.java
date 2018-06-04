package com.baskom.masakbanyak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.bartoszlipinski.constraint.StaggeredAnimationGroup;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class LoginActivity extends AppCompatActivity {
    private StaggeredAnimationGroup gLoginForm;
    private StaggeredAnimationGroup gLoginDaftarBtn;
    private Button mBtnPreLogin;
    private Button mBtnPreDaftar;
    private Button mBtnLogin;
    private int hideFlag = 0;
    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginCheck();

        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        gLoginForm = findViewById(R.id.groupLoginForm);
        gLoginDaftarBtn = findViewById(R.id.groupLoginRegisterBtn);
        mBtnPreLogin = findViewById(R.id.btn_pre_login);
        mBtnPreDaftar = findViewById(R.id.btn_pre_daftar);
        mBtnLogin = findViewById(R.id.button_login);
        gLoginForm.hide();

        mBtnPreLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gLoginForm.show();
                gLoginDaftarBtn.hide();
                hideFlag = 1;
            }
        });

        mBtnPreDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MASAKBANYAK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MasakBanyakService service = retrofit.create(MasakBanyakService.class);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                service.login(email, password).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.code() == 200){
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
                        }else{
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (hideFlag == 1){
            gLoginForm.hide();
            gLoginDaftarBtn.show(true);
            hideFlag = 0;
        }else{
            super.onBackPressed();
        }
    }

    public void loginCheck(){
        Intent intent;
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.app_preferences_key),
                Context.MODE_PRIVATE
        );

        if(sharedPref.contains("access_token") && sharedPref.contains("refresh_token")){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
