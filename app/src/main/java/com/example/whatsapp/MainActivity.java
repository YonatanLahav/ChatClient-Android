package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.whatsapp.localdb.Users;
import com.example.whatsapp.localdb.localDatabase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToLoginScreen = findViewById(R.id.btnToLoginScreen);
        Button btnToRegisterScreen = findViewById(R.id.btnToRegisterScreen);
        Button btnToContacts = findViewById(R.id.btnToContacts);
        Button btnToChatScreen = findViewById(R.id.btnToChatScreen);
        Button btnToAddContactScreen = findViewById(R.id.btnToAddContactScreen);

        btnToLoginScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        btnToRegisterScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        btnToContacts.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
        });
        btnToChatScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });
        btnToAddContactScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            startActivity(intent);
        });
    }
}