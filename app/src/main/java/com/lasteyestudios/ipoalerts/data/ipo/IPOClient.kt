package com.lasteyestudios.ipoalerts.data.ipo

import android.util.Log
import com.google.gson.JsonArray
import com.lasteyestudios.ipoalerts.data.ipo.SearchIPOAllotments.APIMessageSearchAllotments
import com.lasteyestudios.ipoalerts.data.ipo.SearchIPOAllotments.SearchAllotments
import com.lasteyestudios.ipoalerts.data.ipo.common.RetrofitHelper
import com.lasteyestudios.ipoalerts.data.ipo.common.Transformer
import com.lasteyestudios.ipoalerts.data.ipo.ipoAllotments.IPOAllotments
import com.lasteyestudios.ipoalerts.data.ipo.ipoCompanyListing.IPOCompanyListings
import com.lasteyestudios.ipoalerts.data.ipo.ipoDetails.IPOCompanyDetails
import com.lasteyestudios.ipoalerts.data.models.MediaResponse
import com.lasteyestudios.ipoalerts.utils.LOG_TAG


class IPOClient {

    private val urlEndPointAllotments: String = "https://linkintime.co.in/MIPO/IPO.aspx/"
    private val urlEndPointIPOCompanyListings = "https://groww.in/v1/api/stocks_ipo/v2/listing/"
    private val urlEndPointIPOCompanyDetails = "https://groww.in/v1/api/stocks_primary_market_data/v1/ipo/"

    private val transformer = Transformer()

    companion object {
        private var INSTANCE: IPOClient? = null
        fun getInstance(): IPOClient {
            if (INSTANCE == null) {
                INSTANCE = IPOClient()
            }
            return INSTANCE!!
        }
    }

    suspend fun getIPOAllotments(): String? {

        Log.d(LOG_TAG, "client -> start")

        val ipoAllotmentsApi = RetrofitHelper(urlEndPointAllotments).getInstance()
            .create(IPOAllotments::class.java)
        try {
            val res = ipoAllotmentsApi.getIPOAllotments()
            Log.d(LOG_TAG, "getIPOAllotments res -> ${res.toString()}")
            return res.toString()
//            return transformer.genericFeedData(res["payload"].asJsonObject["d"] as JsonArray)
        } catch (e: Exception) {
            Log.d("TAG", "error while getIPOAllotments -> " + e.message)
        }
        return null

    }

//    suspend fun getExploreFeed(): com.lasteyestudios.ipoalerts.data.models.MediaResponse? {
//
//        val exploreFeedAPI = RetrofitHelper(urlEndPointIPOCompanyListing).getInstance()
//            .create(ExploreFeedApi::class.java)
//        val lang = Lang("hindi")
//        val exploreFeedDataClass = APIExploreFeed(
//            message = lang,
//            userId = userId,
//            passCode = passCode,
//            client = "web"
//        )
//        val res = exploreFeedAPI.getMedia(exploreFeedDataClass)
//        try {
//            return transformer.genericFeedData(res["payload"].asJsonObject["d"] as JsonArray)
//        } catch (e: Exception) {
//            Log.d("tag", "error: $e")
//        }
//        return null
//    }

    suspend fun getSearchAllotmentsResults(companyId: String, keyWord: String, userDoc: String): MediaResponse? {
        val searchAllotments = RetrofitHelper(urlEndPointAllotments).getInstance()
            .create(SearchAllotments::class.java)
        val message = APIMessageSearchAllotments(
            clientid = companyId,
            key_word = keyWord,
            PAN = userDoc
        )
        val res = searchAllotments.getSearchAllotmentsResults(
            message = message
        )
        try {
            return transformer.profileFeedData(res["payload"].asJsonObject["d"] as JsonArray)
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return null
    }

    suspend fun getIPOCompanyListings(): String? {
        val ipoCompanyListings =
            RetrofitHelper(urlEndPointIPOCompanyListings).getInstance()
                .create(IPOCompanyListings::class.java)

        val res = ipoCompanyListings.getIPOCompanyListings()
        try {

            return transformer.growIPOListingToData(res["ipoCompanyListingOrderMap"].asJsonObject)
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return null
    }

    suspend fun getIPOCompanyDetails(searchId:String): String? {
        val ipoCompanyListings =
            RetrofitHelper(urlEndPointIPOCompanyDetails).getInstance()
                .create(IPOCompanyDetails::class.java)

        val res = ipoCompanyListings.getIPOCompanyDetails(searchId = searchId)
        try {
            return transformer.genericFeedData(res["payload"].asJsonObject["data"].asJsonObject["postCards"] as JsonArray).toString()
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return null
    }



}