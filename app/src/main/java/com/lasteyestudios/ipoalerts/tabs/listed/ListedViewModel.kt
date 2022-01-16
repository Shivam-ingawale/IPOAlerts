package com.lasteyestudios.ipoalerts.tabs.listed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListedViewModel : ViewModel() {

    private val _currentIPOs = MutableLiveData<List<String>>()
    val currentIPOs: LiveData<List<String>> get() = _currentIPOs

    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

//                _currentIPOs.postValue()
            }
        }
    }

}