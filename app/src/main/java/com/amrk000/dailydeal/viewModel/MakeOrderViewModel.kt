package com.amrk000.dailydeal.viewModel

import DateTimeHelper
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.Order
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.UserAccountManager
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime

class MakeOrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository()
    private val makeOrderLiveData = MutableLiveData<Boolean>()
    private val orderedTodayLiveData = MutableLiveData<Boolean>()

    fun getUserData(): UserData? {
        return UserAccountManager.get(getApplication()).getUserData()
    }

    fun makeOrder(itemId: Long) {
        viewModelScope.launch {
            try {
                val order = Order(
                    userId = getUserData()?.id!!,
                    itemId = itemId,
                    date = Instant.now().toString() //UTC Time
                )

                val result = repository.makeOrder(getApplication(), order)
                makeOrderLiveData.value = result > 0
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun checkIfUserOrderedToday() {
        viewModelScope.launch {
            try {
                val dateTimeUTC = repository.getUserLatestOrderDate(getApplication(), getUserData()?.id!!)

                if (dateTimeUTC.isEmpty()) orderedTodayLiveData.value = false
                else {
                    val orderLocalDateTime = LocalDateTime.parse(DateTimeHelper.utcToLocal(dateTimeUTC))
                    val currentLocalDateTime = LocalDateTime.now()

                    orderedTodayLiveData.value = currentLocalDateTime.toLocalDate().compareTo(orderLocalDateTime.toLocalDate()) == 0
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun orderedTodayCheckObserver(): LiveData<Boolean> {
        return orderedTodayLiveData
    }

    fun getMakeOrderObserver(): LiveData<Boolean> {
        return makeOrderLiveData
    }
}