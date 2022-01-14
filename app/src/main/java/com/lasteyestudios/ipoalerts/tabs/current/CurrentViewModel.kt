package com.lasteyestudios.ipoalerts.tabs.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.repository.NetworkRepository
import com.lasteyestudios.ipoalerts.utils.LOG_TAG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CurrentViewModel : ViewModel() {

    private val networkRepository = NetworkRepository.getInstance()

    private val _currentIPOs = MutableLiveData<List<String>>(listOf())
    val currentIPOs: LiveData<List<String>> get() = _currentIPOs

    fun loadData() {
        Log.d(LOG_TAG, "CurrentViewModel -> start")
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                  _currentIPOs.postValue()
//            }
//        }
        viewModelScope.launch {
            networkRepository.getIPOCompanyListings("lic-ipo").collect(){response->
                when (response) {
                    Response.Loading -> Log.d(LOG_TAG, "load: profile Loading")
                    Response.Error -> Log.d(LOG_TAG, "load: profile Error")
                    is Response.Success -> {
                        if (response.data != null) {
                            val data = response.data
                            Log.d(LOG_TAG, "load: profile Success")
                            _currentIPOs.postValue(listOf(data.toString()))
                        }
                    }
                }
            }
        }


    }
}