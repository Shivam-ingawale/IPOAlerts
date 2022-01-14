package com.lasteyestudios.ipoalerts.repository

import android.util.Log
import com.lasteyestudios.ipoalerts.data.ipo.IPOClient
import com.lasteyestudios.ipoalerts.data.models.AvailableAllotmentModel
import com.lasteyestudios.ipoalerts.data.models.MediaResponse
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.data.models.ipodetailsmodel.IPODetailsModel
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.IPOListingModel
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

    fun getIPOCompanyListings(): Flow<Response<IPOListingModel?>> {
        //done
        return flow {
            emit(Response.Loading)
            val mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyListings() }
            emit(Response.Success(mCompantListing))
            Log.d(IPO_LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
        }
    }
    fun getIPOCompanyDetails(searchId :String): Flow<Response<IPODetailsModel?>> {
        //done
        return flow {
            emit(Response.Loading)
            val mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyDetails(searchId = searchId) }
            emit(Response.Success(mCompantListing))
            Log.d(IPO_LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
        }
    }
    fun getAvailableIPOAllotmentsData(): Flow<Response<List<AvailableAllotmentModel>?>> {
        //done
        Log.d(IPO_LOG_TAG, "network repo -> start")
        return flow {
            emit(Response.Loading)
            val mCompantListing = withContext(Dispatchers.IO) { ipoClient.getAvailableIPOAllotmentsData() }
            emit(Response.Success(mCompantListing))
            Log.d(IPO_LOG_TAG, "getIPOAllotments Network Repo-> Done data -> $mCompantListing" )
        }
    }

    fun getSearchAllotmentsResults(companyId: String, userDoc: String, keyWord: String): Flow<Response<MediaResponse?>> {
        //working
        return flow {
            emit(Response.Loading)
            val mResultAllotment = withContext(Dispatchers.IO) { ipoClient.getSearchAllotmentsResults(companyId, keyWord, userDoc) }
            emit(Response.Success(mResultAllotment))
            Log.d(IPO_LOG_TAG, "getIPOAllotments Network Repo-> Done")
        }
    }


}