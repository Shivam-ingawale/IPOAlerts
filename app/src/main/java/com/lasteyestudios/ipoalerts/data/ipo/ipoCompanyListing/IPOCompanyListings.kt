package com.lasteyestudios.ipoalerts.data.ipo.ipoCompanyListing

import com.google.gson.JsonObject
import retrofit2.http.GET

interface IPOCompanyListings {
    @GET("user")
    suspend fun getIPOCompanyListings() : JsonObject
}