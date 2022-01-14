package com.lasteyestudios.ipoalerts.data.ipo.ipoAllotments

import com.google.gson.JsonObject
import retrofit2.http.Headers
import retrofit2.http.POST

interface IPOAllotments {
    @Headers(
        "content-length: 0",
        "content-type: application/json",
        "charset:utf-8"
    )
    @POST("GetDetails")
    suspend fun getIPOAllotments() : JsonObject
}
