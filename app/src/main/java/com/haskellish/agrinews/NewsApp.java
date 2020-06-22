package com.haskellish.agrinews;

import android.app.Application;

import androidx.room.Room;

import com.haskellish.agrinews.db.NewsDB;

public class NewsApp extends Application {

    public static NewsApp instance;

    private NewsDB database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NewsDB.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static NewsApp getInstance() {
        return instance;
    }

    public NewsDB getDatabase() {
        return database;
    }
}
