package com.lasteyestudios.ipoalerts.repository

import android.util.Log
import com.lasteyestudios.ipoalerts.data.ipo.IPOClient
import com.lasteyestudios.ipoalerts.data.models.MediaResponse
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.utils.LOG_TAG
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

    fun getIPOCompanyListings(): Flow<Response<String?>> {
        //working
        return flow {
            emit(Response.Loading)
            val mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyListings() }
            emit(Response.Success(mCompantListing))
            Log.d(LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
        }
    }
    fun getIPOCompanyListings(searchId :String): Flow<Response<String?>> {
        //working
        return flow {
            emit(Response.Loading)
            val mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOCompanyDetails(searchId = searchId) }
            emit(Response.Success(mCompantListing))
            Log.d(LOG_TAG, "getIPOCompanyListings Network Repo-> Done")
        }
    }
    fun getIPOAllotments(): Flow<Response<String?>> {
        //working
        Log.d(LOG_TAG, "network repo -> start")
        return flow {
            emit(Response.Loading)
            val mCompantListing = withContext(Dispatchers.IO) { ipoClient.getIPOAllotments() }
            emit(Response.Success(mCompantListing))
            Log.d(LOG_TAG, "getIPOAllotments Network Repo-> Done data -> $mCompantListing" )
        }
    }

    fun getSearchAllotmentsResults(companyId: String, userDoc: String, keyWord: String): Flow<Response<MediaResponse?>> {
        //working
        return flow {
            emit(Response.Loading)
            val mResultAllotment = withContext(Dispatchers.IO) { ipoClient.getSearchAllotmentsResults(companyId, keyWord, userDoc) }
            emit(Response.Success(mResultAllotment))
            Log.d(LOG_TAG, "getIPOAllotments Network Repo-> Done")
        }
    }


}