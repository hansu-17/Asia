package com.miskaa.asia.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountriesDao {

    @Insert
    void insertCountry(Country... country);

    @Delete
    void deleteCountry(Country country);

    @Query("SELECT * FROM countries")
    List<Country> getAllCrew();

    @Query("DELETE FROM countries")
    void deleteAll();

}
