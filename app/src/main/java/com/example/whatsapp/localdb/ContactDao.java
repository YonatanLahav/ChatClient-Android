package com.example.whatsapp.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ContactDao {


    @Query("SELECT * FROM contact WHERE connectedUser =:connectedUser")
    List<Contact> index(String connectedUser);

    @Query("SELECT * FROM contact WHERE connectedUser =:connectedUser AND contactId =:contactId")
    Contact get(String connectedUser, String contactId);

    @Query("UPDATE contact SET last =:last, lastDate =:lastDate WHERE contactId =:contactId AND connectedUser =:connectedUser")
    void update(String connectedUser, String contactId, String last, String lastDate);

    @Insert
    void insert(Contact... contacts);

    @Update                             /// ????
    void update(Contact... contacts);

    @Delete                             /// ????
    void delete(Contact... contacts);
}
