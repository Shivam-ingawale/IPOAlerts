package com.lasteyestudios.ipoalerts.tabs.watchlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lasteyestudios.ipoalerts.data.local.CompanyWishlistDao
import com.lasteyestudios.ipoalerts.data.local.CompanyWishlistDatabase
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.repository.LocalDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchListViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentIPOs = MutableLiveData<List<String>>(listOf())
    val currentIPOs: LiveData<List<String>> get() = _currentIPOs

    /*
    * DATABASE FUNCTIONS
    * */
    private val localDbRepository: LocalDbRepository

    val getAllCompanyWishlist: LiveData<List<CompanyLocalModel>>
    private var mDownloadsList: MutableList<CompanyLocalModel> = mutableListOf()

    var getAllGrowShortCompanyWishlist: List<String> = emptyList()

    init {
        val companyWishlistDao: CompanyWishlistDao =
            CompanyWishlistDatabase.getDatabase(application).companyWishlistDao()
        localDbRepository = LocalDbRepository(companyWishlistDao)
        getAllCompanyWishlist = localDbRepository.getAllCompanyWishlist()

        viewModelScope.launch(Dispatchers.IO) {
            getAllGrowShortCompanyWishlist = localDbRepository.getAllGrowShortCompanyWishlist()
        }
    }
    fun getLocalDbRepository(): LocalDbRepository {
        return localDbRepository
    }

    internal fun getListCompanyFromCompanyLocal(it: List<CompanyLocalModel>): List<Company> {
        val listMedia = emptyList<Company>()
        val list2 = listMedia.toMutableList()
        it.forEach {
            list2.add(it.company)
        }
        return list2.toList()
    }



    fun insertWatchlistCompanyLocal(company: CompanyLocalModel) {
        viewModelScope.launch(Dispatchers.IO) {
            localDbRepository.insertCompanyWishlist(company)
        }
    }

//    private fun updateMediaDownload(mediaId: String, status: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            localDbRepository.updateMediaDownload(mediaId, status)
//        }
//    }

    fun deleteCompanyWishlistByGrowwShortName(growwShortName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localDbRepository.deleteCompanyWishlistByGrowwShortName(growwShortName)
        }
    }


    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllGrowShortCompanyWishlist = localDbRepository.getAllGrowShortCompanyWishlist()
        }
//                _currentIPOs.postValue()
    }
}

