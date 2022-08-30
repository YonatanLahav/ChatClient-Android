package com.example.whatsapp.repositories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.api.ContactsApi;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.ContactsPostRequest;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.localdb.*;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

public class ApiContactRepository {

    //    private ApiContactDao dao;
    private ContactsApi _api;
    private ApiContactsListData _apiContactsListData;
    private LoginPostRequest _connectedUser;
    private ContactDao _contactDao;


    public ApiContactRepository(LoginPostRequest connectedUser) {
        _contactDao = localDatabase.getInstance().contactDao();
        _connectedUser = connectedUser;
        _apiContactsListData = new ApiContactsListData();
        _api = new ContactsApi(_connectedUser, _apiContactsListData);
    }


    private class ApiContactsListData extends MutableLiveData<List<ApiContact>> {
        public ApiContactsListData() {
            super();

            new Thread(()->{
                List<Contact> contactList = _contactDao.index(_connectedUser.getId());
                List<ApiContact> apiContactList = new LinkedList<>();
                for (Contact contact : contactList) {
                    apiContactList.add( new ApiContact(contact.getContactId(), contact.getContactName(),
                            contact.getContactServer(), contact.getLast(), contact.getLastDate()));
                }
                postValue(apiContactList);
            }).start();
        }

        @Override
        protected void onActive() {
            super.onActive();
            _api.get();
        }
    }

    /**
     * Get all the connected user contacts.
     */
    public LiveData<List<ApiContact>> getAll() {
        return _apiContactsListData;
    }

    /**
     * Add new contact. the _api.invitation method excute both add and invitation api calls.
     */
    public void add(ContactsPostRequest contactsPostRequest, AppCompatActivity appCompatActivity) {
        _api.invitation(contactsPostRequest, appCompatActivity);
    }
}
