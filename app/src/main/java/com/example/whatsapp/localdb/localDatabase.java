package com.example.whatsapp.localdb;

import android.content.Context;
import com.example.whatsapp.MyApplication;
import androidx.room.Room;

public class localDatabase {
    //private static Context _context;
    private static AppDB _db = null;

    private localDatabase() {
    }
    public static AppDB getInstance() {
        if (_db == null) {
            _db = Room.databaseBuilder(MyApplication.context,
                            AppDB.class
                            , "AndroidDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return _db;
    }
}
