package com.example.android.moviesapp.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movies>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE movieId = :id")
    LiveData<Movies> loadMovie(String id);

    @Insert
    void insertMovie(Movies movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movies movies);

    @Delete
    void deleteMovie(Movies movies);
}
