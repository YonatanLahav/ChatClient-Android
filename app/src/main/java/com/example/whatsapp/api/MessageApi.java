package com.example.whatsapp.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.MyApplication;
import com.example.whatsapp.R;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.ApiMessage;
import com.example.whatsapp.entities.ApiTransfer;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.entities.MessagePostRequest;
import com.example.whatsapp.localdb.Contact;
import com.example.whatsapp.localdb.Message;
import com.example.whatsapp.localdb.localDatabase;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageApi {
    Retrofit _retrofit;
    WebServiceAPI _webServiceAPI;
    LoginPostRequest _connectedUser;
    ApiContact _contact;
    MutableLiveData<List<ApiMessage>> _apiMessagesListData;

    public MessageApi(LoginPostRequest connectedUser
            , ApiContact contact
            , MutableLiveData<List<ApiMessage>> apiMessagesListData) {
        _retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        _webServiceAPI = _retrofit.create(WebServiceAPI.class);
        _connectedUser = connectedUser;
        _contact = contact;
        _apiMessagesListData = apiMessagesListData;
    }

    /**
     * Get all messages.
     */
    public void get() {
        Call<List<ApiMessage>> call = _webServiceAPI
                .GetAllContactMessage(_contact.getId().toString(), _connectedUser.getId());
        call.enqueue(new Callback<List<ApiMessage>>() {
            @Override
            public void onResponse(Call<List<ApiMessage>> call, Response<List<ApiMessage>> response) {
                //_apiMessagesListData.postValue(response.body());

                new Thread(()->{
                    _apiMessagesListData.postValue(response.body());
                }).start();

                new Thread(()->{

                    // clear dao from current conversation.
                    List<Message> messageList = localDatabase.getInstance().messageDao().getMessages(_connectedUser.getId(), _contact.getId());
                    for (Message message : messageList) {
                        localDatabase.getInstance().messageDao().deleteMessage(message);
                    }
                    List<Message> messageList2 = localDatabase.getInstance().messageDao().getMessages(_contact.getId(), _connectedUser.getId());
                    for (Message message : messageList2) {
                        localDatabase.getInstance().messageDao().deleteMessage(message);
                    }

                    // update dao with server response.
                    List<ApiMessage> apiMessageList = response.body();
                    for (ApiMessage message : apiMessageList) {
                        if (message.is_sent() == true) {
                            localDatabase.getInstance().messageDao().insertMessage(new Message(_connectedUser.getId(),
                                    _contact.getId(), message.get_content(), message.get_created(), true));
                        }
                        else {
                            localDatabase.getInstance().messageDao().insertMessage(new Message(_contact.getId(),
                                    _connectedUser.getId(), message.get_content(), message.get_created(), false));
                        }

                    }
                    _apiMessagesListData.postValue(apiMessageList);
                }).start();
            }

            @Override
            public void onFailure(Call<List<ApiMessage>> call, Throwable t) {
            }
        });
    }

    public void transfer(MessagePostRequest messagePostRequest, AppCompatActivity appCompatActivity) {
        Call<Void> call = _webServiceAPI
                .PostTransfer(new ApiTransfer(_connectedUser.getId()
                        , _contact.getId()
                        , messagePostRequest.getContent()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    add(messagePostRequest, appCompatActivity);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void add(MessagePostRequest messagePostRequest, AppCompatActivity appCompatActivity) {
        Call<ApiMessage> call = _webServiceAPI
                .CreateMessage(new MessagePostRequest(messagePostRequest.getContent())
                        , _contact.getId(), _connectedUser.getId());
        call.enqueue(new Callback<ApiMessage>() {
            @Override
            public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                // Insert new message - localdb
                new Thread(() -> {
                    Message newMessage = new Message(_connectedUser.getId(),
                            _contact.getId(), messagePostRequest.getContent(), response.body().get_created(), true);
                    localDatabase.getInstance().messageDao().insertMessage(newMessage);
                }).start();

                // update messages list - api
                new Thread(() -> {
                    List<ApiMessage> temp = _apiMessagesListData.getValue();
                    temp.add(response.body());
                    _apiMessagesListData.postValue(temp);
                }).start();
            }

            @Override
            public void onFailure(Call<ApiMessage> call, Throwable t) {

            }
        });
    }
}
