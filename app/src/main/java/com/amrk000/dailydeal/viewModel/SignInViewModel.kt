package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.UserAccountManager
import kotlinx.coroutines.launch

class SignInViewModel (application: Application): AndroidViewModel(application)  {
    private val repository = Repository()
    private val userLiveData = MutableLiveData<UserData?>()

    fun canUserAccessAdminDashboard(): Boolean{
        return UserAccountManager.get(getApplication()).canAccessAdminDashboard()
    }

    fun signInUser(userEmail: String, userPassword: String){
        viewModelScope.launch {
            try {
                val user = repository.signInUser(getApplication(), userEmail, userPassword)
                if(user!=null)  UserAccountManager.get(getApplication()).signIn(user)
                userLiveData.value = user
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun getSignInUserObserver(): MutableLiveData<UserData?> {
        return userLiveData
    }
}