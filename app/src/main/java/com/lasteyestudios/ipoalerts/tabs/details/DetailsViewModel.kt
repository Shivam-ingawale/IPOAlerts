package com.lasteyestudios.ipoalerts.tabs.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.data.models.ipodetailsmodel.IPODetailsModel
import com.lasteyestudios.ipoalerts.repository.NetworkRepository
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel : ViewModel() {

    private val networkRepository = NetworkRepository.getInstance()

    private val _detailsIPOs = MutableLiveData<Response<IPODetailsModel?>?>()
    val detailsIPOs: LiveData<Response<IPODetailsModel?>?> get() = _detailsIPOs

    fun loadData(searchId: String, growwShortName:String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                Log.d(IPO_LOG_TAG, "DetailsViewModel -> start")

                viewModelScope.launch {
                    networkRepository.getIPOCompanyDetails(searchId = searchId,
                        growwShortName = growwShortName).catch { exception ->
                        Log.d(IPO_LOG_TAG,
                            "error received while updating details ipo live data" + exception.stackTraceToString())
                        _detailsIPOs.postValue(Response.Error)
                    }.collect { exploreResponse ->
                        _detailsIPOs.postValue(exploreResponse)

                    }

                }
            }


        }
//                _currentIPOs.postValue()
    }


    fun clearData() {
        _detailsIPOs.postValue(null)
    }
}
