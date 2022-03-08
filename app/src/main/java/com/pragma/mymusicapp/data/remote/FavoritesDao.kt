package com.pragma.mymusicapp.data.remote

import android.icu.text.Replaceable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.pragma.mymusicapp.data.entities.FavoriteSong
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface FavoritesDao {
    @Query("SELECT * from favorite_song")
    suspend fun getAllRecords():MutableList<FavoriteSong>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(insert: FavoriteSong?)

    @Query("DELETE FROM favorite_song")
    fun deleteAll()

    @Query("SELECT * from favorite_song where id=:id")
    suspend fun isRecordExists(id: String?): FavoriteSong?


    @Delete
    fun deleteSingleRecord(searchContactDataModel: FavoriteSong?): Completable

    @Query("DELETE FROM favorite_song where id=:id")
    suspend fun deleteSingleRecord(id: String?)

}