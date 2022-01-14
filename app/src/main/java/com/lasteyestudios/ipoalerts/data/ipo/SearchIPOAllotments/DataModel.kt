package com.lasteyestudios.ipoalerts.data.ipo.SearchIPOAllotments

import com.google.gson.annotations.SerializedName

data class APIMessageSearchAllotments(
    @SerializedName("clientid") val clientid: String,
    @SerializedName("PAN") val PAN: String,
    @SerializedName("key_word") val key_word: String,
)
