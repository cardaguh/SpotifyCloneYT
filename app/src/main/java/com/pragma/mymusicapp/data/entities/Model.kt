package com.pragma.mymusicapp.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite_song")
data class FavoriteSong constructor(
    @field:SerializedName("id") @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @field:SerializedName("name") @ColumnInfo(name = "name") var name: String?,
    @field:SerializedName("photo") @ColumnInfo(name = "photo") var photo: String?
) : Parcelable