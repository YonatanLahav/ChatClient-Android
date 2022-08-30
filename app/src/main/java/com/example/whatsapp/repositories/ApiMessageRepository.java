package com.example.whatsapp.repositories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.api.MessageApi;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.ApiMessage;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.entities.MessagePostRequest;
import com.example.whatsapp.localdb.Contact;
import com.example.whatsapp.localdb.Message;
import com.example.whatsapp.localdb.MessageDao;
import com.example.whatsapp.localdb.localDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiMessageRepository {
    //    private ApiMessageDao dao;
    private MessageApi _api;
    private ApiMessagesListData _apiMessagesListData;
    private LoginPostRequest _connectedUser;
    private ApiContact _contact;
    private MessageDao _messageDao;

    public ApiMessageRepository(LoginPostRequest connectedUser, ApiContact contact) {
        _messageDao = localDatabase.getInstance().messageDao();
        _connectedUser = connectedUser;
        _contact = contact;
        _apiMessagesListData = new ApiMessagesListData();
        _api = new MessageApi(_connectedUser, _contact, _apiMessagesListData);
    }

    private class ApiMessagesListData extends MutableLiveData<List<ApiMessage>> {
        public ApiMessagesListData() {
            super();
            new Thread(()->{
                List<Message> messageList = _messageDao.getConversation(_connectedUser.getId(), _contact.getId());
                List<ApiMessage> apiMessageList = new LinkedList<>();
                for (Message message : messageList) {
                    apiMessageList.add( new ApiMessage(message.getContent(), message.getCreated(), message.isSent()));
                }
                _apiMessagesListData.postValue(apiMessageList);

            }).start();
            List<ApiMessage> apiMessages = new LinkedList<>();
             setValue(apiMessages);
        }

        @Override
        protected void onActive() {
            super.onActive();
            _api.get();

        }
    }

    public LiveData<List<ApiMessage>> getAll() {
        return _apiMessagesListData;
    }

    public void add(MessagePostRequest messagePostRequest, AppCompatActivity appCompatActivity) {
        _api.transfer(messagePostRequest, appCompatActivity);
    }
}
