package com.pragma.mymusicapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pragma.mymusicapp.data.entities.Song
import com.pragma.mymusicapp.other.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch(e: Exception) {
            emptyList()
        }
    }
}