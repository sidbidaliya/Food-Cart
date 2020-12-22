package com.sid.foodproject.restaurantDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertRes(resEntity: ResEntity)

    @Delete
    fun deleteRes(resEntity: ResEntity)

    @Query("SELECT count(*) FROM restaurants")
    fun checkRes() : Int

    @Query("SELECT * FROM restaurants")
    fun getAllRes() : List<ResEntity>

    @Query("SELECT * FROM restaurants WHERE res_id = :resId")
    fun getResById(resId : String) : ResEntity

    @Query("DELETE FROM restaurants")
    fun deleteAll()

}