package com.lasteyestudios.ipoalerts.data.ipo.SearchIPOAllotments

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SearchAllotments {
    @Headers(
        "content-type: application/json",
        "charset: UTF-8"
    )
    @POST("SearchOnPan")
    suspend fun getSearchAllotmentsResults(@Body message: APIMessageSearchAllotments): JsonObject
}