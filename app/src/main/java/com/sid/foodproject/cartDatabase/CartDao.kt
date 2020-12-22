package com.sid.foodproject.cartDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun insertCart(cartEntity: CartEntity)

    @Delete
    fun deleteCart(cartEntity: CartEntity)

    @Query("SELECT count(*) FROM cart")
    fun checkItem() : Int

    @Query("DELETE FROM cart")
    fun deleteAll()

    @Query("SELECT * FROM cart")
    fun getAllCart() : List<CartEntity>

    @Query("SELECT * FROM cart WHERE dish_id = :dishId")
    fun getCartById(dishId : String) : CartEntity
}
