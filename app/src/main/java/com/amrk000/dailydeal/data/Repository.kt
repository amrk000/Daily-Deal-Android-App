package com.amrk000.dailydeal.data

import android.content.Context
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.Order
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.Model.UserOrderData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.data.local.AppDatabase
import com.google.gson.Gson

class Repository() {

    //Local Data:
    fun initDatabaseData(context: Context){
        //load default permissions
        val permissionsJson = context.resources.openRawResource(R.raw.default_permissions)
            .bufferedReader().use {
                it.readText()
            }
        val permissions =  Gson().fromJson(permissionsJson, Array<Permissions>::class.java).toList()
        AppDatabase.get(context).dao().addDefaultPermissions(permissions)

        //load default users
        val usersJson = context.resources.openRawResource(R.raw.default_users)
            .bufferedReader().use {
                it.readText()
            }
        val users =  Gson().fromJson(usersJson, Array<UserData>::class.java).toList()
        AppDatabase.get(context).dao().addDefaultUsers(users)
    }

    //Roles
    suspend fun addRolePermissions(context: Context, permissions: Permissions): Long{
        return AppDatabase.get(context).dao().addRolePermissions(permissions)
    }

    suspend fun updateRolePermissions(context: Context, permissions: Permissions): Int{
        return AppDatabase.get(context).dao().updateRolePermissions(permissions)
    }

    suspend fun getRolePermissions(context: Context, role: String): Permissions{
        return AppDatabase.get(context).dao().getRolePermissions(role)
    }

    //Users
    suspend fun signUpUser(context: Context, user: UserData): Long{
        return AppDatabase.get(context).dao().signUpUser(user)
    }

    suspend fun signInUser(context: Context, email: String, password: String): UserData?{
        return AppDatabase.get(context).dao().signInUser(email, password)
    }

    suspend fun updateUser(context: Context, user: UserData): Int{
        return AppDatabase.get(context).dao().updateUser(user)
    }

    suspend fun getUserById(context: Context, id: Long): UserData?{
        return AppDatabase.get(context).dao().getUserById(id)
    }

    //Items
    suspend fun addItem(context: Context, item: ItemData): Long{
        return AppDatabase.get(context).dao().addItem(item)
    }

    suspend fun updateItem(context: Context, item: ItemData): Int{
        return AppDatabase.get(context).dao().updateItem(item)
    }

    suspend fun deleteItem(context: Context, item: ItemData): Int{
        return AppDatabase.get(context).dao().deleteItem(item)
    }

    suspend fun getItem(context: Context, itemID: Long): List<ItemData>{
        return AppDatabase.get(context).dao().getItem(itemID)
    }

    suspend fun getItems(context: Context): List<ItemData>{
        return AppDatabase.get(context).dao().getAllItems()
    }

    suspend fun getTodayItems(context: Context, dayName: String): List<ItemData>{
        return AppDatabase.get(context).dao().getTodayItems(dayName)
    }

    //orders
    suspend fun makeOrder(context: Context, order: Order): Long{
        return AppDatabase.get(context).dao().makeOrder(order)
    }

    suspend fun getUserOrders(context: Context, userId: Long): List<UserOrderData>{
        return AppDatabase.get(context).dao().getUserOrders(userId)
    }

    suspend fun getUserLatestOrderDate(context: Context, userId: Long): String{
        return AppDatabase.get(context).dao().getUserLatestOrderDate(userId)
    }
}