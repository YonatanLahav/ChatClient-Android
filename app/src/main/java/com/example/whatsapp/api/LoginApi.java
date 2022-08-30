package com.example.whatsapp.api;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.ContactsActivity;
import com.example.whatsapp.LoginActivity;
import com.example.whatsapp.MyApplication;
import com.example.whatsapp.R;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.entities.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginApi {
    Retrofit _retrofit;
    WebServiceAPI _webServiceAPI;

    public LoginApi() {
        _retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        _webServiceAPI = _retrofit.create(WebServiceAPI.class);
    }

    /**
     * Login method.
     */
    public void login(String username, String password, AppCompatActivity appCompatActivity, String newToken) {
        Call<User> call = _webServiceAPI.Login(new LoginPostRequest(username, password));
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User user = response.body();
                    Intent intent = new Intent(appCompatActivity, ContactsActivity.class);
                    intent.putExtra("username", user.getId());
                    intent.putExtra("password", user.getPassword());
                    intent.putExtra("nickname", user.getNickname());
                    token(username, newToken, appCompatActivity);
                    appCompatActivity.finish();
                    appCompatActivity.startActivity(intent);
                } else {
                    Intent intent = appCompatActivity.getIntent();
                    appCompatActivity.getIntent()
                            .putExtra("Invalid", "Invalid username or password!");
                    appCompatActivity.finish();
                    appCompatActivity.overridePendingTransition(0, 0);
                    appCompatActivity.startActivity(intent);
                    appCompatActivity.overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    /**
     * token method.
     */
    public void token(String username, String token, AppCompatActivity appCompatActivity) {
        Call<String> call = _webServiceAPI.UpdateToken(new LoginPostRequest(username, token));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String name = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
