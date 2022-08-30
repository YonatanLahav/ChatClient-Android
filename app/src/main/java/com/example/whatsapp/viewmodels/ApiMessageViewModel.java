package com.example.whatsapp.viewmodels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.ApiMessage;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.entities.MessagePostRequest;
import com.example.whatsapp.repositories.ApiContactRepository;
import com.example.whatsapp.repositories.ApiMessageRepository;

import java.util.List;

public class ApiMessageViewModel extends ViewModel {
    // Repository for messages.
    private final ApiMessageRepository _apiMessageRepository;
    // LiveData with all the contact's messages.
    private final LiveData<List<ApiMessage>> _apiMessages;
    // Current connected user.
    private LoginPostRequest _connectedUser;
    // Current contact.
    private ApiContact _contact;

    public ApiMessageViewModel(LoginPostRequest connectedUser, ApiContact contact) {
        _connectedUser = connectedUser;
        _contact = contact;
        _apiMessageRepository = new ApiMessageRepository(_connectedUser, _contact);
        _apiMessages = _apiMessageRepository.getAll();

    }

    public LiveData<List<ApiMessage>> get() {
        return _apiMessages;
    }

    public void add(MessagePostRequest messagePostRequest, AppCompatActivity appCompatActivity) {
        _apiMessageRepository.add(messagePostRequest, appCompatActivity);
    }
//
//    public void delete(ApiMessage apiMessage) {
//        _apiMessageRepository.delete(apiMessage);
//    }
//
//    public void reload() {
//        _apiMessageRepository.reload();
//    }

}
