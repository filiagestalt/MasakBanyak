package com.baskom.masakbanyak.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mName;
    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mPhone;
    private AutoCompleteTextView mPassword;
    private AutoCompleteTextView mPasswordConfirm;
    private Button  mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.et_nama_reg);
        mPhone = findViewById(R.id.et_phone_reg);
        mEmail = findViewById(R.id.et_email_reg);
        mPassword = findViewById(R.id.et_password_reg);
        mPasswordConfirm = findViewById(R.id.et_konfirmasiPassword_reg);
        mButton = findViewById(R.id.btn_daftar);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(MASAKBANYAK_URL).build();
        final MasakBanyakWebService service = retrofit.create(MasakBanyakWebService.class);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Kata sandi tidak sama", Toast
                            .LENGTH_SHORT)
                            .show();
                }else{
                    service.register(mName.getText().toString(),
                            mPhone.getText().toString(),
                            mEmail.getText().toString(),
                            mPassword.getText().toString())
                            .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity
                                    .this);
                            if(response.code() == 200){
                                try{
                                    builder.setMessage(response.body().string());
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                                builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(RegisterActivity.this,
                                                LoginActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                                response.body().close();
                            }else{
                                try {
                                    builder.setMessage(response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                builder.setNegativeButton("KEMBALI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                response.errorBody().close();
                            }
                            builder.create().show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, t.toString(), Toast
                                    .LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
