package com.amrk000.dailydeal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.data.Repository
import com.amrk000.dailydeal.util.UserAccountManager
import kotlinx.coroutines.launch

class MealEditorViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = Repository()
    private val createLiveData = MutableLiveData<Boolean>()
    private val updateLiveData = MutableLiveData<Boolean>()
    private val deleteLiveData = MutableLiveData<Boolean>()

    fun createMeal(itemData: ItemData) {
        viewModelScope.launch {
            try {
                val result = repository.addItem(getApplication(), itemData)
                createLiveData.value = result > 0
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun updateMeal(itemData: ItemData) {
        viewModelScope.launch {
            try {
                val result = repository.updateItem(getApplication(), itemData)
                updateLiveData.value = result > 0
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun deleteMeal(itemData: ItemData) {
        viewModelScope.launch {
            try {
                val result = repository.deleteItem(getApplication(), itemData)
                deleteLiveData.value = result > 0
            }
            catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    fun getCreateObserver(): LiveData<Boolean> {
        return createLiveData
    }

    fun getUpdateObserver(): LiveData<Boolean> {
        return updateLiveData
    }

    fun getDeleteObserver(): LiveData<Boolean> {
        return deleteLiveData
    }

    fun canEditMeal(): Boolean{
        return UserAccountManager.get(getApplication()).canEditItem()
    }

    fun canDeleteMeal(): Boolean{
        return UserAccountManager.get(getApplication()).canDeleteItem()
    }

}