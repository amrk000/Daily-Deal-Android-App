package com.amrk000.dailydeal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.Order
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.Model.UserOrderData


@Dao
interface AppDao {
    //Preload database with default data
    @Insert(entity = Permissions::class, onConflict = OnConflictStrategy.REPLACE)
    fun addDefaultPermissions(permissions: List<Permissions>)

    @Insert(entity = UserData::class, onConflict = OnConflictStrategy.IGNORE)
    fun addDefaultUsers(users: List<UserData>)

    //Roles
    @Insert(entity = Permissions::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRolePermissions(permissions: Permissions): Long

    @Update(entity = Permissions::class)
    suspend fun updateRolePermissions(permissions: Permissions): Int

    @Query("SELECT * FROM permissions WHERE role = :role")
    suspend fun getRolePermissions(role: String): Permissions

    //Users
    @Insert(entity = UserData::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun signUpUser(user: UserData): Long

    @Query("SELECT * FROM users WHERE email = :email AND password =:password")
    suspend fun signInUser(email: String, password: String): UserData?

    @Update(entity = UserData::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateUser(user: UserData): Int

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): UserData?

    //Items
    @Insert(entity = ItemData::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: ItemData): Long

    @Update(entity = ItemData::class)
    suspend fun updateItem(item: ItemData): Int

    @Delete(entity = ItemData::class)
    suspend fun deleteItem(item: ItemData): Int

    @Query("SELECT * FROM items WHERE id = :itemID")
    suspend fun getItem(itemID: Long): List<ItemData>

    @Query("SELECT * FROM items ORDER BY id DESC")
    suspend fun getAllItems(): List<ItemData>

    @Query("SELECT * FROM items WHERE day = :dayName ORDER BY originalPrice - discountPrice DESC")
    suspend fun getTodayItems(dayName: String): List<ItemData>

    //orders
    @Insert(entity = Order::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun makeOrder(order: Order): Long

    @Query("SELECT items.id, items.name, items.image, items.restaurant, items.day, orders.date FROM orders INNER Join items on orders.itemId = items.id WHERE userId = :userId ORDER BY DATETIME(date, 'UTC') DESC")
    suspend fun getUserOrders(userId: Long): List<UserOrderData>

    @Query("SELECT date FROM orders WHERE userId = :userId ORDER BY DATETIME(date, 'UTC') DESC LIMIT 1")
    suspend fun getUserLatestOrderDate(userId: Long): String
}