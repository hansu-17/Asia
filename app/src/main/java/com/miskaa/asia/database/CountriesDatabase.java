package com.miskaa.asia.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miskaa.asia.CountriesRecyclerAdapter;

@Database(entities = {Country.class}, version = 1)
public abstract class CountriesDatabase extends RoomDatabase {
    public abstract CountriesDao getCountryDao();

    private static CountriesDatabase instance;

    public static CountriesDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CountriesDatabase.class, "asia-db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
