package com.example.nghiamvvm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    /* Method to insert the movies fetched from api
     * to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieEntity>): LongArray

    /* Method to fetch the movies stored locally */
    @Query("SELECT * FROM `MovieEntity`")
    fun getMoviesByPage(): List<MovieEntity>
}