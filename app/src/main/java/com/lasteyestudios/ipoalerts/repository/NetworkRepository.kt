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
    private var mCompantListing: IPOListingModel? = null
    private var currentlyGettingExploreResponse: Boolean = false

    fun getIPOCompanyListings(): Flow<Response<List<List<Company?>?>>> {
        //done
        // don't call for explore feed when its already running.
        if (currentlyGettingExploreResponse) return flow { emit(Response.Loading) }
        mCompantListing?.LISTED?.size?.let {
            if (mCompantListing?.LISTED?.size!! > 0) {
                Log.d(
                    IPO_LOG_TAG,
                    "getIPOCompanyListings: already had a value with  items"
                )
                return flow {
                    emit(Response.Success(listOf<List<Company?>?>(mCompantListing?.ACTIVE,
                        mCompantListing?.CLOSED,
                        mCompantListing?.LISTED,
                        mCompantListing?.UPCOMING)))
                }
            }
        }

        return flow {
            emit(Response.Loading)
            var mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyListings() }
            emit(Response.Success(listOf<List<Company?>?>(mCompantListing?.ACTIVE,
                mCompantListing?.CLOSED,
                mCompantListing?.LISTED,
                mCompantListing?.UPCOMING)))
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

    fun getAvailableIPOAllotmentsData(): Flow<Response<List<AvailableAllotmentModel>?>> {
        //done
        Log.d(IPO_LOG_TAG, "network repo -> start")
        return flow {
            emit(Response.Loading)
            val mCompanyListing =
                withContext(Dispatchers.IO) { ipoClient.getAvailableIPOAllotmentsData() }
            emit(Response.Success(mCompanyListing))
            Log.d(IPO_LOG_TAG, "getIPOAllotments Network Repo-> Done data -> $mCompanyListing")
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