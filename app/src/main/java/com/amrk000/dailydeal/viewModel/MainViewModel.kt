package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.SharedPrefManager
import com.amrk000.dailydeal.util.UserAccountManager
import com.google.gson.Gson

class MainViewModel (application: Application): AndroidViewModel(application) {
    private val repository = Repository()

    fun isUserSignedIn(): Boolean{
        return UserAccountManager.get(getApplication()).isSignedIn()
    }

    fun canAccessAdminDashboard(): Boolean{
        return UserAccountManager.get(getApplication()).canAccessAdminDashboard()
    }

    fun initDatabaseData(){
        repository.initDatabaseData(getApplication())
    }
}