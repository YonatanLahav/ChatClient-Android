package com.example.whatsapp.localdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class, Message.class, Users.class}, version = 4)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract UsersDao usersDao();
    public abstract MessageDao messageDao();
}
