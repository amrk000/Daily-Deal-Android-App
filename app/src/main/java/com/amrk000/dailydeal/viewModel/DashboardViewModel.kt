package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.UserAccountManager
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel (application: Application): AndroidViewModel(application) {
    private val repository = Repository()
    private val itemsData = MutableLiveData<ArrayList<ItemData>>()

    fun getItemsByDay(dayName: String){
        viewModelScope.launch {
            try {
                val items = repository.getTodayItems(getApplication(), dayName)
                itemsData.value = items.toCollection(ArrayList())
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun getAllItems(){
        viewModelScope.launch {
            try {
                val items = repository.getItems(getApplication())
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

    fun getUserData(): UserData? {
        return UserAccountManager.get(getApplication()).getUserData()
    }

    fun getUserPermissions(): Permissions?{
        return UserAccountManager.get(getApplication()).getUserPermissions()
    }

    fun canCreateMeal(): Boolean{
        return UserAccountManager.get(getApplication()).canAddItem()
    }

}