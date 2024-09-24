package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.data.Repository
import kotlinx.coroutines.launch

class SignUpViewModel (application: Application): AndroidViewModel(application)  {
    private val repository = Repository()
    private val userLiveData = MutableLiveData<UserData?>()

    fun signUpUser(userData: UserData){
        viewModelScope.launch {
            try {
                val result = repository.signUpUser(getApplication(), userData)
                if(result > 0) userLiveData.value = userData
                else userLiveData.value = null
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun getSignUpUserObserver(): MutableLiveData<UserData?> {
        return userLiveData
    }
}