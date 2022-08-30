package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsapp.adapters.ApiContactListAdapter;
import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.interfaces.ListItemClickListener;
import com.example.whatsapp.localdb.Users;
import com.example.whatsapp.localdb.localDatabase;
import com.example.whatsapp.viewmodels.ApiContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements ListItemClickListener {

    private ApiContactViewModel _viewModel;
    private LoginPostRequest _connectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        // Get extras and create LoginPostRequest object to pass the connected user.
        Bundle extras = getIntent().getExtras();

        // Init connected user.
        _connectedUser = new LoginPostRequest(extras.getString("username")
                , extras.getString("password"));

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
        Users user = localDatabase.getInstance().usersDao().getUser(_connectedUser.getId());
        String encodedImage;
        if (user == null)
            encodedImage = localDatabase.getInstance().usersDao().getUser("Default").getPicture();
        else
            encodedImage = localDatabase.getInstance().usersDao().getUser(_connectedUser.getId()).getPicture();
        byte[] imageByteArray = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        userImage.setImageBitmap(image);

        TextView tvContactName = findViewById(R.id.tvContactName);
        tvContactName.setText(_connectedUser.getId());

        // Set button to add new contact screen.
        FloatingActionButton btnToAddContact = findViewById(R.id.btnAddContactScreen);
        btnToAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("username", extras.getString("username"));
            intent.putExtra("password", extras.getString("password"));
            intent.putExtra("nickname", extras.getString("nickname"));
            startActivity(intent);
        });


        // Init _viewModel field.
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ApiContactViewModel(_connectedUser);
            }
        };
        _viewModel = new ViewModelProvider(this, factory).get(ApiContactViewModel.class);
        // RecycleView logic.
        RecyclerView lstApiContacts = findViewById(R.id.lstApiContacts);
        final ApiContactListAdapter adapter = new ApiContactListAdapter(this, this);
        lstApiContacts.setLayoutManager(new LinearLayoutManager(this));
        lstApiContacts.setClickable(true);
        lstApiContacts.setAdapter(adapter);

        // Set observer on the data in the viewModel. when viewModel data will change,
        // the method will activate.
        _viewModel.get().observe(this, apiContacts -> {
            // show user image
            adapter.setContacts(apiContacts);
            //      lstApiContacts.setAdapter(adapter);
        });

        // Go to settings.
        FloatingActionButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onListItemClick(ApiContact apiContact) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username", _connectedUser.getId());
        intent.putExtra("password", _connectedUser.getPassword());
        intent.putExtra("ContactUsername", apiContact.getId());
        intent.putExtra("ContactName", apiContact.getName());
        intent.putExtra("ContactServer", apiContact.getServer());
        startActivity(intent);
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

