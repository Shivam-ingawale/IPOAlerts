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

    companion object {
        private var INSTANCE: NetworkRepository? = null
        fun getInstance(): NetworkRepository {
            if (INSTANCE == null) {
                INSTANCE = NetworkRepository()
            }
            return INSTANCE!!
        }
    }

    private var ipoClient: IPOClient = IPOClient.getInstance()
    private var myIpoListing: IPOListingModel? = null
    private var currentlyGettingExploreResponse: Boolean = false

    private var availableAllotments: List<AvailableAllotmentModel?>? = null
    private var currentlyGettingAvailableAllotments: Boolean = false

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
            var mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyListings() }
            emit(Response.Success(listOf<List<Company?>?>(mCompantListing?.ACTIVE,
                mCompantListing?.UPCOMING,
                mCompantListing?.LISTED,
                mCompantListing?.CLOSED)))
            Log.d(IPO_LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
        }
    }

    fun getIPOCompanyDetails(searchId: String): Flow<Response<IPODetailsModel?>> {
        //done
        return flow {
            emit(Response.Loading)
            val mCompantListing =
                withContext(Dispatchers.IO) { ipoClient.getIPOCompanyDetails(searchId = searchId) }
            emit(Response.Success(mCompantListing))
            Log.d(IPO_LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
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
        return flow {
            emit(Response.Loading)
            val mResultAllotment = withContext(Dispatchers.IO) {
                ipoClient.getSearchAllotmentsResults(companyId,
                    keyWord,
                    userDoc)
            }
            emit(Response.Success(mResultAllotment))
            Log.d(
                IPO_LOG_TAG,
                "getSearchAllotmentsResults Network Repo-> Done data -> $mResultAllotment}"
            )
        }
    }


}