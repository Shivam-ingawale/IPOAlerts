package com.lasteyestudios.ipoalerts.tabs.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.repository.NetworkRepository
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    // This is to maintain the state since this navigation system detaches
    // and attaches fragments instead of showing/hiding


    private val networkRepository = NetworkRepository.getInstance()
    private val _currentIPOs = MutableLiveData<Response<List<List<Company?>?>>>()
    val currentIPOs: LiveData<Response<List<List<Company?>?>>> get() = _currentIPOs

    fun loadHomeIPOData() {
        Log.d(IPO_LOG_TAG,"loadHomeIPOData called")

        viewModelScope.launch {
            networkRepository.getIPOCompanyListings().catch { exception ->
                Log.d(IPO_LOG_TAG,"error received while updating explore live data" + exception.stackTraceToString())
                _currentIPOs.postValue(Response.Error)
            }.collect { exploreResponse ->
                _currentIPOs.postValue(exploreResponse)
            }
        }
    }
}