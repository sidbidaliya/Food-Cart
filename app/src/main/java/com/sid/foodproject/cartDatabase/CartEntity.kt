package com.sid.foodproject.cartDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val dish_id : String,
    @ColumnInfo(name = "dish_name") val dishName : String,
    @ColumnInfo(name = "dish_cfo") val dishCfo : String
)