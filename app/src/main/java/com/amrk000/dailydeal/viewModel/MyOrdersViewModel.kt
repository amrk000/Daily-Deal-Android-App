package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.Model.UserOrderData
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.UserAccountManager
import kotlinx.coroutines.launch
import java.time.LocalDate

class MyOrdersViewModel (application: Application): AndroidViewModel(application) {
    private val repository = Repository()
    private val ordersHistoryLiveData = MutableLiveData<ArrayList<UserOrderData>>()

    fun getUserData(): UserData? {
        return UserAccountManager.get(getApplication()).getUserData()
    }

    fun getUserOrdersHistory(){
        viewModelScope.launch {
            try {
                val items = repository.getUserOrders(getApplication(), getUserData()?.id!!)
                ordersHistoryLiveData.value = items.toCollection(ArrayList())
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun getUserOrdersHistoryObserver(): MutableLiveData<ArrayList<UserOrderData>> {
        return ordersHistoryLiveData
    }

}