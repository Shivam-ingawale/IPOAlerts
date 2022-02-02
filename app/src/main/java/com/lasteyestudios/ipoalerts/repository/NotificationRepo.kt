package com.lasteyestudios.ipoalerts.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.ListenableWorker
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.tabs.watchlist.WatchListViewModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class NotificationRepo {
    private val networkRepository = NetworkRepository.getInstance()
    private val localDbRepository: LocalDbRepository
    private val watchListViewModel = WatchListViewModel(Application())

    private val _notifications = MutableLiveData<List<List<String?>?>?>(listOf())
    val notifications: LiveData<List<List<String?>?>?> get() = _notifications

    companion object {
        private var INSTANCE: NotificationRepo? = null
        fun getInstance(): NotificationRepo {
            if (INSTANCE == null) {
                INSTANCE = NotificationRepo()
            }
            return INSTANCE!!
        }
    }


    init {
        localDbRepository = watchListViewModel.getLocalDbRepository()
    }


    private fun getCompanyLocalFromGrowwShortName(growwShortName: String): CompanyLocalModel {
        return localDbRepository.getCompanyLocalFromGrowwShortName(growwShortName)
    }

    suspend fun getIPOCompanyListingsForNotifications() {
        Log.d(IPO_LOG_TAG, "getIPOCompanyListingsForNotifications start")

        val getAllSymbolCompanyWishlist: List<String> =
            localDbRepository.getAllSymbolCompanyWishlist()
        networkRepository.getIPOCompanyListings().catch { exception ->
            Log.d(IPO_LOG_TAG,
                "error received in GetUpdatesWorker" + exception.stackTraceToString())
            ListenableWorker.Result.failure()
        }.collect { myResponse ->
            when (myResponse) {
                is Response.Success -> {
                    var new: MutableList<List<String>> = mutableListOf()
                    for (j in myResponse.data.indices) {
                        if (myResponse.data?.get(j) != null) {
                            for (k in myResponse.data?.get(j)?.indices!!) {
                                for (i in getAllSymbolCompanyWishlist.indices) {
                                    if (myResponse.data?.get(j)
                                            ?.get(k)?.symbol == getAllSymbolCompanyWishlist[i]
                                    ) {
                                        var myLocalCompany =
                                            getCompanyLocalFromGrowwShortName(
                                                getAllSymbolCompanyWishlist[i]).company

                                        //testing
//                                        myLocalCompany = Company(biddingStartDate = null,growwShortName = null,issueSize = null,listingDate = null,logoUrl = null,maxPrice = null,minBidQuantity = null,minPrice = null,searchId = null, status = null, liked = false,additionalTxt = null,retailSubscriptionRate = null,issuePrice = null,listingGains = null,listingPrice = null)


                                        val updatedCompany = myResponse.data?.get(j)?.get(k)!!

//                                        Log.d(IPO_LOG_TAG, "updatedCompany -> $updatedCompany")
//                                        Log.d(IPO_LOG_TAG, "myLocalCompany -> $myLocalCompany")
                                        val t = emptyList<String>()
                                        val curr: MutableList<String> = t.toMutableList()


//                                        Log.d(IPO_LOG_TAG,"element " + updatedCompany.status + " to " + myLocalCompany.status + " not equal "+(updatedCompany.status != myLocalCompany.status))
                                        if (updatedCompany.status != myLocalCompany.status) {
                                            curr.add(updatedCompany.growwShortName + " Status Changed to " + updatedCompany.status)
//                                            Log.d(IPO_LOG_TAG, "curr ->${curr.toString()}")
                                        }
                                        if (updatedCompany.additionalTxt != myLocalCompany.additionalTxt) {
                                            curr.add(updatedCompany.growwShortName + " " + updatedCompany.additionalTxt)
                                        }
                                        if (updatedCompany.biddingStartDate != myLocalCompany.biddingStartDate) {
                                            curr.add(updatedCompany.growwShortName + " Bidding Start Date is " + updatedCompany.biddingStartDate)
                                        }
                                        if (updatedCompany.listingDate != myLocalCompany.listingDate) {
                                            curr.add(updatedCompany.growwShortName + " Listing  Date is " + updatedCompany.listingDate)
                                        }
                                        if (updatedCompany.listingGains != myLocalCompany.listingGains) {
                                            curr.add(updatedCompany.growwShortName + " Listing  Date is " + updatedCompany.listingGains)
                                        }
                                        Log.d(IPO_LOG_TAG, "curr -> $curr")
                                        curr.let { new.add(it) }

                                        localDbRepository.deleteCompanyWishlistBySymbol(
                                            myLocalCompany.growwShortName.toString())
                                        updatedCompany.liked = true
                                        localDbRepository.insertCompanyWishlist(
                                            CompanyLocalModel(0,
                                                System.currentTimeMillis() / 1000,
                                                updatedCompany.growwShortName!!,
                                                updatedCompany.symbol.toString(),
                                                updatedCompany))
                                        Log.d(IPO_LOG_TAG,
                                            "updatedCompany ->$updatedCompany and myLocalCompany ->$myLocalCompany")

                                        break
                                    }
                                }
                            }
                        }
                    }
//                    testing
//                    new = listOf(listOf("sdfsdf","Sdfsdfsd","121s2df2")).toMutableList()
                    Log.d(IPO_LOG_TAG, "value of new ->${new.toList()}")

                    _notifications.postValue(new.toList())

                }
                else -> {
                    ListenableWorker.Result.failure()
                }
            }
        }
        Log.d(IPO_LOG_TAG, "getIPOCompanyListingsForNotifications end")
    }
}
