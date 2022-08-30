package com.example.whatsapp.api;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.MyApplication;
import com.example.whatsapp.R;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.ApiInvitation;
import com.example.whatsapp.entities.ContactsPostRequest;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.localdb.*;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsApi {
    Retrofit _retrofit;
    WebServiceAPI _webServiceAPI;
    LoginPostRequest _connectedUser;
    MutableLiveData<List<ApiContact>> _apiContactsLiveData;

    public ContactsApi(LoginPostRequest connectedUser, MutableLiveData<List<ApiContact>> apiContactsLiveData) {
        _retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        _webServiceAPI = _retrofit.create(WebServiceAPI.class);
        _connectedUser = connectedUser;
        _apiContactsLiveData = apiContactsLiveData;
    }


    /**
     * Get all contacts of the connected user.
     */
    public void get() {
        Call<List<ApiContact>> call = _webServiceAPI.GetAllContacts(_connectedUser.getId());
        call.enqueue(new Callback<List<ApiContact>>() {
            @Override
            public void onResponse(Call<List<ApiContact>> call, Response<List<ApiContact>> response) {
                new Thread(() -> {
                    _apiContactsLiveData.postValue(response.body());
                }).start();


                new Thread(()->{

                    // clear dao from current contacts.
                    List<Contact> contactList = localDatabase.getInstance().contactDao().index(_connectedUser.getId());
                    for (Contact contact : contactList) {
                        localDatabase.getInstance().contactDao().delete(contact);
                    }
                    // update dao with server response.
                    List<ApiContact> apiContactList = response.body();
                    for (ApiContact contact : apiContactList) {
                        localDatabase.getInstance().contactDao().insert(new Contact
                                ( _connectedUser.getId(), contact.getId(), contact.getName(),
                                        contact.getServer(), contact.getLast(), contact.getLastdate()));
                    }
                    _apiContactsLiveData.postValue(apiContactList);
                }).start();



//                new Thread(() -> {
//                    dao.clear();
//                    dao.insertList(response.body());
//                    postListData.postValue(dao.get());
//                }).start();
            }

            @Override
            public void onFailure(Call<List<ApiContact>> call, Throwable t) {
                Log.d("myError", String.valueOf(t));
            }
        });
    }

    public void invitation(ContactsPostRequest contactsPostRequest, AppCompatActivity appCompatActivity) {
        Call<Void> call = _webServiceAPI
                .PostInvitation(new ApiInvitation(_connectedUser.getId()
                        , contactsPostRequest.getId()
                        , appCompatActivity.getString(R.string.serverPath)));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201)
                    add(contactsPostRequest, appCompatActivity);
                else {
                    appCompatActivity.getIntent()
                            .putExtra("Invalid", "Invalid new contact !");
                    appCompatActivity.finish();
                    appCompatActivity.overridePendingTransition(0, 0);
                    appCompatActivity.startActivity(appCompatActivity.getIntent());
                    appCompatActivity.overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                appCompatActivity.getIntent()
                        .putExtra("Invalid", "Invalid new contact !");
                appCompatActivity.finish();
                appCompatActivity.overridePendingTransition(0, 0);
                appCompatActivity.startActivity(appCompatActivity.getIntent());
                appCompatActivity.overridePendingTransition(0, 0);
            }
        });
    }

    public void add(ContactsPostRequest contactsPostRequest, AppCompatActivity appCompatActivity) {
        Call<Void> call = _webServiceAPI.CreateContact(contactsPostRequest, _connectedUser.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    new Thread(() -> {
                        List<ApiContact> apiContactList = _apiContactsLiveData.getValue();
                        apiContactList.add(new ApiContact(contactsPostRequest));
                        _apiContactsLiveData.postValue(apiContactList);
                    }).start();

                    // Insert new Contact - localdb
                    new Thread(() -> {
                        Contact newContact = new Contact(_connectedUser.getId(),
                                contactsPostRequest.getId(), contactsPostRequest.getName(),
                                contactsPostRequest.getServer(), "", "");
                        localDatabase.getInstance().contactDao().insert(newContact);
                    }).start();

                    appCompatActivity.finish();
                } else {
                    appCompatActivity.getIntent()
                            .putExtra("Invalid", "Invalid new contact !");
                    appCompatActivity.finish();
                    appCompatActivity.overridePendingTransition(0, 0);
                    appCompatActivity.startActivity(appCompatActivity.getIntent());
                    appCompatActivity.overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                appCompatActivity.getIntent()
                        .putExtra("Invalid", "Invalid new contact !");
                appCompatActivity.finish();
                appCompatActivity.overridePendingTransition(0, 0);
                appCompatActivity.startActivity(appCompatActivity.getIntent());
                appCompatActivity.overridePendingTransition(0, 0);
            }
        });
    }
}
