package com.lasteyestudios.ipoalerts.repository

import android.util.Log
import com.lasteyestudios.ipoalerts.data.ipo.IPOClient
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.data.models.availableallotmentmodel.AvailableAllotmentModel
import com.lasteyestudios.ipoalerts.data.models.ipodetailsmodel.IPODetailsModel
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.IPOListingModel
import com.lasteyestudios.ipoalerts.data.models.searchallotmentresultmodel.SearchAllotmentResultModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NetworkRepository {


    private var ipoClient: IPOClient = IPOClient.getInstance()
    private var myIpoListing: IPOListingModel? = null
    private var currentlyGettingExploreResponse: Boolean = false

    private var availableAllotments: List<AvailableAllotmentModel?>? = null
    private var currentlyGettingAvailableAllotments: Boolean = false


    private var iPOCompanyDetails: IPODetailsModel? = null
    private var currentlyGettingIPOCompanyDetails: Boolean = false


    private var currentlyGettingSearchAllotmentsResults: Boolean = false


    companion object {
        private var INSTANCE: NetworkRepository? = null
        fun getInstance(): NetworkRepository {
            if (INSTANCE == null) {
                INSTANCE = NetworkRepository()
            }
            return INSTANCE!!
        }
    }

    fun getIPOCompanyListings(): Flow<Response<List<List<Company?>?>>> {
        //done
        // don't call for explore feed when its already running.
        if (currentlyGettingExploreResponse) return flow { emit(Response.Loading) }
        myIpoListing?.LISTED?.size?.let {
            if (myIpoListing?.LISTED?.size!! > 0) {
                Log.d(
                    IPO_LOG_TAG,
                    "getIPOCompanyListings: already had a value with  items"
                )
                return flow {
                    emit(Response.Success(listOf<List<Company?>?>(myIpoListing?.ACTIVE,
                        myIpoListing?.UPCOMING,
                        myIpoListing?.LISTED,
                        myIpoListing?.CLOSED)))
                }
            }
        }
        return flow {
            emit(Response.Loading)
            myIpoListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyListings() }
            emit(Response.Success(listOf<List<Company?>?>(myIpoListing?.ACTIVE,
                myIpoListing?.UPCOMING,
                myIpoListing?.LISTED,
                myIpoListing?.CLOSED)))
            Log.d(IPO_LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
        }
    }

    fun getIPOCompanyDetails(
        searchId: String,
        growwShortName: String,
    ): Flow<Response<IPODetailsModel?>> {

        if (currentlyGettingIPOCompanyDetails) return flow { emit(Response.Loading) }
        //done
        if (iPOCompanyDetails != null && iPOCompanyDetails!!.growwShortName == growwShortName) {
            Log.d(
                IPO_LOG_TAG,
                "iPOCompanyDetails: already had a value with  items"
            )
            return flow {
                emit(Response.Success(iPOCompanyDetails))
            }
        }
        return flow {
            emit(Response.Loading)
            iPOCompanyDetails =
                withContext(Dispatchers.IO) { ipoClient.getIPOCompanyDetails(searchId = searchId) }
            emit(Response.Success(iPOCompanyDetails))
            Log.d(IPO_LOG_TAG, "getIPOCompanyListings Network Repo-> Done $iPOCompanyDetails")
        }
    }

    fun getAvailableIPOAllotmentsData(): Flow<Response<List<AvailableAllotmentModel?>?>> {
        //done
        // don't call for explore feed when its already running.
        if (currentlyGettingAvailableAllotments) return flow { emit(Response.Loading) }

        if (availableAllotments != null) {
            Log.d(
                IPO_LOG_TAG,
                "availableAllotments: already had a value with  items"
            )
            return flow {
                emit(Response.Success(availableAllotments))
            }
        }

        Log.d(IPO_LOG_TAG, "network repo -> start")
        return flow {
            emit(Response.Loading)
            availableAllotments =
                withContext(Dispatchers.IO) { ipoClient.getAvailableIPOAllotmentsData() }
            emit(Response.Success(availableAllotments))
            Log.d(IPO_LOG_TAG, "getIPOAllotments Network Repo-> Done data -> $availableAllotments")
        }
    }

    fun getSearchAllotmentsResults(
        companyId: String,
        userDoc: String,
        keyWord: String,
    ): Flow<Response<SearchAllotmentResultModel?>> {
        //done
        if (currentlyGettingSearchAllotmentsResults) return flow { emit(Response.Loading) }

        return flow {
            emit(Response.Loading)
            var searchAllotmentsResults = withContext(Dispatchers.IO) {
                ipoClient.getSearchAllotmentsResults(companyId,
                    keyWord,
                    userDoc)
            }
            emit(Response.Success(searchAllotmentsResults))
            Log.d(
                IPO_LOG_TAG,
                "getSearchAllotmentsResults Network Repo-> Done data -> $searchAllotmentsResults}"
            )
        }
    }


}