package com.lasteyestudios.ipoalerts.data.ipo.ipoDetails

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path

interface  IPOCompanyDetails {
    @GET("company/{searchId}")
    suspend fun getIPOCompanyDetails(@Path(value = "searchId",encoded = true) searchId : String) : JsonObject

}


//curl 'https://groww.in/v1/api/stocks_primary_market_data/v1/ipo/company/tarsons-products-ipo' \
//--compressed