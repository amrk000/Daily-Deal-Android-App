package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.UserAccountManager
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel (application: Application): AndroidViewModel(application) {
    private val repository = Repository()
    private val itemsData = MutableLiveData<ArrayList<ItemData>>()

    fun signOut(){
        UserAccountManager.get(getApplication()).signOut()
    }

    fun getUserData(): UserData? {
        return UserAccountManager.get(getApplication()).getUserData()
    }

    fun getTodayItems(){
        viewModelScope.launch {
            try {
                val todayName = LocalDate.now().dayOfWeek.name

                val items = repository.getTodayItems(getApplication(), todayName)
                itemsData.value = items.toCollection(ArrayList())
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun getItemsDataObserver(): MutableLiveData<ArrayList<ItemData>> {
        return itemsData
    }
}