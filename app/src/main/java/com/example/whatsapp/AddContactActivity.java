package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.whatsapp.entities.ApiContact;
import com.example.whatsapp.entities.ContactsPostRequest;
import com.example.whatsapp.entities.LoginPostRequest;
import com.example.whatsapp.viewmodels.ApiContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddContactActivity extends AppCompatActivity {

    private ApiContactViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Set error message after failed.
        Bundle extras = getIntent().getExtras();
        TextView tvErr = findViewById(R.id.tvErr);
        if (extras != null)
            tvErr.setText(extras.getString("Invalid"));
        else
            tvErr.setText("");
        // Get extras and create LoginPostRequest object to pass the connected user.
        LoginPostRequest connectedUser = new LoginPostRequest(extras.getString("username"),
                extras.getString("password"));
        // Init viewModel field.
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ApiContactViewModel(connectedUser);
            }
        };
        viewModel = new ViewModelProvider(this, factory).get(ApiContactViewModel.class);

        Button btnAdd = findViewById(R.id.btnAddContact);
        btnAdd.setOnClickListener(view -> {
            EditText etAddContactUsername = findViewById(R.id.etAddContactUsername);
            EditText etAddContacNickname = findViewById(R.id.etAddContacNickname);
            EditText etAddContactServer = findViewById(R.id.etAddContactServer);
            viewModel.add(new ContactsPostRequest(etAddContactUsername.getText().toString()
                            , etAddContacNickname.getText().toString()
                            , etAddContactServer.getText().toString())
                    , this);
        });

        // Go to settings.
        FloatingActionButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}