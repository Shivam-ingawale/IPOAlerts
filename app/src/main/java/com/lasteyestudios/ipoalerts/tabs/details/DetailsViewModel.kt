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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel : ViewModel() {

    private val networkRepository = NetworkRepository.getInstance()

    private val _detailsIPOs = MutableLiveData<IPODetailsModel?>()
    val detailsIPOs: LiveData<IPODetailsModel?> get() = _detailsIPOs

    fun loadData(searchId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                Log.d(IPO_LOG_TAG, "DetailsViewModel -> start")

                viewModelScope.launch {
                    networkRepository.getIPOCompanyDetails(searchId).collect { response ->
                        when (response) {
                            Response.Loading -> Log.d(IPO_LOG_TAG, "load: profile Loading")
                            Response.Error -> Log.d(IPO_LOG_TAG, "load: profile Error")
                            is Response.Success -> {
                                if (response.data != null) {

                                    Log.d(IPO_LOG_TAG,
                                        "load: profile Success with data -> ${response.data}")

//                            for (i  in 0 until  data.UPCOMING.size){
//                                Log.d(IPO_LOG_TAG, "data got ${data.UPCOMING[i].logoUrl}.")
//                            }

                                    _detailsIPOs.postValue(response.data)
                                }
                            }
                        }
                    }
                }


            }
//                _currentIPOs.postValue()
        }
    }
}
