package com.pragma.mymusicapp.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pragma.mymusicapp.data.entities.FavoriteSong


@Database(entities = [FavoriteSong::class], version = 1, exportSchema = false)
abstract class FavoriteSongsDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        @Volatile
        var INSTANCE: FavoriteSongsDatabase? = null

        fun getDatabasenIstance(mContext: Context): FavoriteSongsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabaseInstance(mContext).also {
                    INSTANCE = it
                }
            }


        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, FavoriteSongsDatabase::class.java, "favorite_song_database")
                .fallbackToDestructiveMigration()
                .build()

    }

}