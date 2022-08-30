package com.example.whatsapp.api;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.ContactsActivity;
import com.example.whatsapp.MyApplication;
import com.example.whatsapp.R;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.entities.User;
import com.example.whatsapp.localdb.Users;
import com.example.whatsapp.localdb.UsersDao;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterApi {
    Retrofit _retrofit;
    WebServiceAPI _webServiceAPI;
    private UsersDao _usersDao;

    public RegisterApi(UsersDao userDao) {
        _retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        _webServiceAPI = _retrofit.create(WebServiceAPI.class);
        _usersDao = userDao;
    }

    /**
     * Register method.
     */
    public void register(User user, AppCompatActivity appCompatActivity, String encodedImage, String newToken) {

        Call<User> call = _webServiceAPI.Register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {

                    User user = response.body();
                    Intent intent = new Intent(appCompatActivity, ContactsActivity.class);
                    intent.putExtra("username", user.getId());
                    intent.putExtra("password", user.getPassword());
                    intent.putExtra("nickname", user.getNickname());
                    Users userEntity = new Users(user.getId(), encodedImage);
                    _usersDao.insertUser(userEntity);
                    token(user.getId(), newToken, appCompatActivity);
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
                Intent intent = appCompatActivity.getIntent();
                appCompatActivity.getIntent()
                        .putExtra("Invalid", "Invalid username or password!");
                appCompatActivity.finish();
                appCompatActivity.overridePendingTransition(0, 0);
                appCompatActivity.startActivity(intent);
                appCompatActivity.overridePendingTransition(0, 0);
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
