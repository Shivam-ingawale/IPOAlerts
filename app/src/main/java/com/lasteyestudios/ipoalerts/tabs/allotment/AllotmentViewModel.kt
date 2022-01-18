package com.lasteyestudios.ipoalerts.tabs.allotment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.data.models.availableallotmentmodel.AvailableAllotmentModel
import com.lasteyestudios.ipoalerts.repository.NetworkRepository
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AllotmentViewModel : ViewModel() {

    private val _allotmentIPOs = MutableLiveData<Response<List<AvailableAllotmentModel?>?>>()
    val allotmentIPOs: LiveData<Response<List<AvailableAllotmentModel?>?>>
        get() = _allotmentIPOs

    private val networkRepository = NetworkRepository.getInstance()

    fun loadAllotmentIPOData() {
        Log.d(IPO_LOG_TAG, "loadHomeIPOData called")

        viewModelScope.launch {
            networkRepository.getAvailableIPOAllotmentsData().catch { exception ->
                Log.d(IPO_LOG_TAG,
                    "error received while updating explore live data" + exception.stackTraceToString())
                _allotmentIPOs.postValue(Response.Error)
            }.collect { exploreResponse ->
                _allotmentIPOs.postValue(exploreResponse)
            }
        }
    }

}