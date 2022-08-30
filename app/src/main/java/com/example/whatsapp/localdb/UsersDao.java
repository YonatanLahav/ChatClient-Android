package com.example.whatsapp.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsersDao {
    /**
     * Get all users table.
     */
    @Query("SELECT * FROM users")
    List<Users> index();

    /**
     * Get specific user by id.
     */
    @Query("SELECT * FROM users WHERE userId =:userId")
    Users getUser(String userId);

    /**
     * Create new user (after registration).
     */
    @Insert
    void insertUser(Users... users);

    @Update                           /// ?????
    void update(Users... users);

    @Delete                           /// ?????
    void delete(Users... users);
}
