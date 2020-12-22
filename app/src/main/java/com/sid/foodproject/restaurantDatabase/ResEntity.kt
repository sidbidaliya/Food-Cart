package com.sid.foodproject.restaurantDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class ResEntity(
    @PrimaryKey val res_id : String,
    @ColumnInfo(name = "res_name") val resName : String,
    @ColumnInfo(name = "res_rating") val resRating : String,
    @ColumnInfo(name = "res_cfo") val resCfo : String,
    @ColumnInfo(name = "res_image") val resImage : String
)