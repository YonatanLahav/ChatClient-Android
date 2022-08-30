package com.example.whatsapp.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {

    /**
     * Get all messages table.
     */
    @Query("SELECT * FROM message")
    List<Message> index();

    /**
     * Get messages of connectedUser with contactID.
     */
    @Query("SELECT * FROM message WHERE connectedUser =:connectedUser AND contactId =:contactId")
    List<Message> getMessages(String connectedUser, String contactId);

    @Query("SELECT * FROM message WHERE connectedUser =:connectedUser AND contactId =:contactId OR connectedUser =:contactId AND contactId =:connectedUser")
    List<Message> getConversation(String connectedUser, String contactId);

    /**
     * Create new message.
     */
    @Insert
    void insertMessage(Message... messages);

    @Update
    void updateMessage(Message... messages);       // ?????

    @Delete                                        // ?????
    void deleteMessage(Message... messages);
}
