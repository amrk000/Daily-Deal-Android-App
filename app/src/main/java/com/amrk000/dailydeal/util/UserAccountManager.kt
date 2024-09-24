package com.amrk000.dailydeal.util

import android.content.Context
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.data.Repository
import kotlinx.coroutines.runBlocking

class UserAccountManager private constructor(private val context: Context) {
    private val repository: Repository = Repository()
    private var user: UserData? = null
    private var permissions: Permissions? = null

    companion object {
        private lateinit var instance: UserAccountManager

        fun get(context: Context): UserAccountManager {
            if (!this::instance.isInitialized){
                instance = UserAccountManager(context)

                runBlocking {
                    val userId = SharedPrefManager.get(context).userId
                    if (userId > -1) {
                        instance.user = instance.repository.getUserById(context, userId)
                        if(instance.user!=null) instance.permissions = instance.repository.getRolePermissions(context, instance.user!!.role)
                    }
                }

            }
            return instance
        }
    }

    fun getUserData(): UserData?{
        return user
    }

    fun isSignedIn(): Boolean{
        return SharedPrefManager.get(context).userSignedIn
    }

    fun signIn(userData: UserData){
        user = userData
        SharedPrefManager.get(context).userSignedIn = true
        SharedPrefManager.get(context).userId = userData.id

        runBlocking {
            if(instance.user!=null) instance.permissions = instance.repository.getRolePermissions(context, instance.user!!.role)
        }
    }

    fun signOut(){
        user = null
        SharedPrefManager.get(context).userSignedIn = false
        SharedPrefManager.get(context).userId = -1
    }


    fun getUserPermissions(): Permissions?{
        return permissions
    }

    fun canAccessAdminDashboard():Boolean{
        return permissions?.adminDashboard ?: false
    }

    fun canOrderItems():Boolean{
        return permissions?.orderItem ?: false
    }

    fun canAddItem():Boolean{
        return permissions?.addItem ?: false
    }

    fun canEditItem():Boolean{
        return permissions?.editItem ?: false
    }

    fun canDeleteItem():Boolean{
        return permissions?.deleteItem ?: false
    }

}