package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsapp.adapters.ApiContactListAdapter;
import com.example.whatsapp.adapters.ApiMessageListAdapter;
import com.example.whatsapp.api.RegisterApi;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.entities.MessagePostRequest;
import com.example.whatsapp.entities.User;
import com.example.whatsapp.localdb.Users;
import com.example.whatsapp.localdb.localDatabase;
import com.example.whatsapp.viewmodels.ApiContactViewModel;
import com.example.whatsapp.viewmodels.ApiMessageViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ChatActivity extends AppCompatActivity {

    private ApiMessageViewModel viewModel;
    private LoginPostRequest _connectedUser;
    private ApiContact _contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Init connected user field.
        Bundle extras = getIntent().getExtras();
        _connectedUser = new LoginPostRequest(extras.getString("username")
                , extras.getString("password"));

        // Init contact field.
        _contact = new ApiContact(extras.getString("ContactUsername")
                , extras.getString("ContactName")
                , extras.getString("ContactServer")
                , ""
                , "");

        // check orientation.
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, ContactsAndChatActivity.class);
            intent.putExtra("username", _connectedUser.getId());
            intent.putExtra("password", _connectedUser.getPassword());
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
        }

        // show user image
        ImageView userImage = findViewById(R.id.ivContactAvatar);
        Users user = localDatabase.getInstance().usersDao().getUser(_contact.getId());
        String encodedImage;
        if (user == null)
            encodedImage = localDatabase.getInstance().usersDao().getUser("Default").getPicture();
        else
            encodedImage = localDatabase.getInstance().usersDao().getUser(_contact.getId()).getPicture();
        byte[] imageByteArray = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        userImage.setImageBitmap(image);

        // Init top bar (contact card).
        TextView tvContactName = findViewById(R.id.tvContactName);
        tvContactName.setText(_contact.getName());

        // Init _viewModel field.
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ApiMessageViewModel(_connectedUser, _contact);
            }
        };
        viewModel = new ViewModelProvider(this, factory).get(ApiMessageViewModel.class);

        // RecycleView logic.
        RecyclerView lstApiMessages = findViewById(R.id.lstApiMessages);
        final ApiMessageListAdapter adapter = new ApiMessageListAdapter(this, getIntent().getExtras().get("ContactName").toString());
        lstApiMessages.setLayoutManager(new LinearLayoutManager(this));
        lstApiMessages.setAdapter(adapter);

        // Set observer on the data in the viewModel. when viewModel data will change,
        // the method will activate.
        viewModel.get().observe(this, apiMessages -> {
            adapter.setMessages(apiMessages);
            lstApiMessages.scrollToPosition(viewModel.get().getValue().size() - 1);
        });

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            EditText etNewMessage = findViewById(R.id.etNewMessage);
            MessagePostRequest messagePostRequest =
                    new MessagePostRequest(etNewMessage.getText().toString());
            viewModel.add(messagePostRequest, this);
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etNewMessage.getText().clear();
            } catch (Exception e) {
            }
            lstApiMessages.scrollToPosition(viewModel.get().getValue().size() - 1);
        });

        // Go to settings.
        FloatingActionButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, ContactsAndChatActivity.class);
            intent.putExtra("username", _connectedUser.getId());
            intent.putExtra("password", _connectedUser.getPassword());
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}