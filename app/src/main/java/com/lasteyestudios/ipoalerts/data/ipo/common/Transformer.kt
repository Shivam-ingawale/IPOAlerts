package com.lasteyestudios.ipoalerts.data.ipo.common

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lasteyestudios.ipoalerts.data.models.AvailableAllotmentModel
import com.lasteyestudios.ipoalerts.data.models.ipodetailsmodel.*
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.IPOListingModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import com.lasteyestudios.ipoalerts.utils.LAST_TO_LAST_TO_LAST_YEAR
import com.lasteyestudios.ipoalerts.utils.LAST_TO_LAST_YEAR
import com.lasteyestudios.ipoalerts.utils.LAST_YEAR

class Transformer {
    /*
    * UTIL functions
    * */

    private fun objToString(temp: Any?): String {
        if (temp == null) {
            return ""
        }
        val i = temp.toString().replace("\"", "")
        return (i)
    }

    /*
    * Specific Transformers
//    * */
//    fun genericFeedData(d: JsonArray): String? {
//        val listMedia = emptyList<com.lasteyestudios.ipoalerts.data.models.Media>()
//        val list2 = listMedia.toMutableList()
//        try {
//            for (i in 0 until d.size()) {
//                val jsonElement = d[i] as JsonObject
//                val widthString = objToString(jsonElement["w"])
//                val heightString = objToString(jsonElement["h"])
//                val authorJson = jsonElement["ath"] as JsonObject
//                val basePlatformNew =
//                    com.lasteyestudios.ipoalerts.data.models.BasePlatform(
//                        name = "moj",
//                        mediaTypes = objToString(jsonElement["t"])
//                    )
//                val audioJson = jsonElement["audioMeta"] as JsonObject
//                val media = com.lasteyestudios.ipoalerts.data.models.Media(
//                    id = objToString(jsonElement["a"]),
//                    basePlatform = basePlatformNew,
//                    contentType = objToString(jsonElement["t"]),
//                    videos = listOf(
//                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
//                            url = objToString(jsonElement["attributedVideoUrl"]),
//                            height = heightString,
//                            width = widthString
//                        )
//                    ),
//                    audio = com.lasteyestudios.ipoalerts.data.models.MediaAudio(
//                        id = objToString(audioJson["audioId"]),
//                        artist = objToString(audioJson["audioText"]),
//                        artworkUrl = objToString(audioJson["thumbUrl"]),
//                        title = objToString(audioJson["audioName"]),
//                        media = objToString(audioJson["resourceUrl"]),
//                        metadata = mapOf("compressedThumbUrl" to objToString(audioJson["compressedThumbUrl"]))
//                    ),
//                    images = listOf(
//                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
//                            url = objToString(jsonElement["thumb"]),
//                            height = heightString,
//                            width = widthString
//                        )
//                    ),
//                    metrics = com.lasteyestudios.ipoalerts.data.models.MediaMetrics(
//                        likes = objToInt(jsonElement["lc"]),
//                        comments = objToInt(jsonElement["c2"]),
//                        share = objToInt(jsonElement["usc"]),
//                        views = null
//                    ),
//                    description = objToString(jsonElement["c"]),
//                    user = com.lasteyestudios.ipoalerts.data.models.User(
//                        id = objToString(authorJson["a"]),
//                        name = objToString(authorJson["n"]),
//                        username = objToString(authorJson["h"]),
//                        basePlatform = basePlatformNew,
//                        metrics = com.lasteyestudios.ipoalerts.data.models.UserMetrics(
//                            likes = objToString(authorJson["likeCount"]), followers = null,
//                            media = basePlatformNew.mediaTypes,
//                            following = null
//                        ),
//                        profile = com.lasteyestudios.ipoalerts.data.models.UserProfile(
//                            verified = null,
//                            pics = listOf(
//                                mapOf("PhotoUrl" to objToString(authorJson["pu"])),
//                                mapOf("ThumbUrl" to objToString(authorJson["tu"])),
//                                mapOf("coverPic" to objToString(authorJson["coverPic"]))
//                            ),
//                            category = null,
//                            description = objToString(authorJson["s"]),
//                            externalUrl = null
//                        ),
//                        metadata = null
//                    )
//                )
//                if (media.id != null) {
//                    list2.add(media)
//                }
//            }
//        } catch (e: Exception) {
//            Log.d("tag", "error: $e")
//        }
//        return MediaResponse(list2.toList(), null).toString()
//    }
//
//    fun profileFeedData(d: JsonArray): MediaResponse? {
//        val listMedia = emptyList<com.lasteyestudios.ipoalerts.data.models.Media>()
//        val list2 = listMedia.toMutableList()
//        try {
//            for (i in 0 until d.size()) {
//                val jsonElement = d[i] as JsonObject
//
//                val widthString = objToString(jsonElement["w"])
//                val heightString = objToString(jsonElement["h"])
//                val authJson = jsonElement["ath"] as JsonObject
//                val basePlatformNew =
//                    com.lasteyestudios.ipoalerts.data.models.BasePlatform(
//                        name = "moj",
//                        mediaTypes = objToString(jsonElement["t"])
//                    )
//                val audioJson = jsonElement["audioMeta"] as JsonObject
//
//
//                val media = com.lasteyestudios.ipoalerts.data.models.Media(
//                    id = objToString(jsonElement["a"]),
//                    basePlatform = basePlatformNew,
//                    contentType = objToString(jsonElement["t"]),
//                    videos = listOf(
//                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
//                            url = objToString(jsonElement["compressedVideoUrl"])!!,
//                            height = heightString,
//                            width = widthString
//                        )
//                    ),
//                    audio = com.lasteyestudios.ipoalerts.data.models.MediaAudio(
//                        id = objToString(audioJson["audioId"]),
//                        artist = objToString(audioJson["audioText"]),
//                        artworkUrl = objToString(audioJson["thumbUrl"]),
//                        title = objToString(audioJson["audioName"]),
//                        media = objToString(audioJson["resourceUrl"]),
//                        metadata = mapOf("compressedThumbUrl" to objToString(audioJson["compressedThumbUrl"]))
//                    ),
//                    images = listOf(
//                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
//                            url = objToString(jsonElement["thumb"]),
//                            height = heightString,
//                            width = widthString
//                        )
//                    ),
//                    metrics = com.lasteyestudios.ipoalerts.data.models.MediaMetrics(
//                        likes = objToInt(jsonElement["lc"]),
//                        comments = objToInt(jsonElement["c2"]),
//                        share = objToInt(jsonElement["usc"]),
//                        views = null
//                    ),
//                    description = objToString(jsonElement["c"]),
//                    user = com.lasteyestudios.ipoalerts.data.models.User(
//                        id = objToString(authJson["a"]),
//                        name = objToString(authJson["n"]),
//                        username = objToString(authJson["h"]),
//                        basePlatform = basePlatformNew,
//                        metrics = com.lasteyestudios.ipoalerts.data.models.UserMetrics(
//                            likes = objToString(authJson["likeCount"]),
//                            followers = null,
//                            media = basePlatformNew.mediaTypes,
//                            following = null
//                        ),
//                        profile = com.lasteyestudios.ipoalerts.data.models.UserProfile(
//                            verified = null,
//                            pics = listOf(
//                                mapOf("PhotoUrl" to objToString(authJson["pu"])),
//                                mapOf("ThumbUrl" to objToString(authJson["tu"])),
//                                mapOf("coverPic" to objToString(authJson["coverPic"]))
//                            ),
//                            category = null,
//                            description = objToString(authJson["s"]),
//                            externalUrl = null
//                        ),
//                        metadata = null
//                    )
//                )
//                if (media.id != null) {
//                    list2.add(media)
//                }
//            }
//        } catch (e: Exception) {
//            Log.d("tag", "error: $e")
//        }
//        return MediaResponse(list2.toList(), null)
//    }

//    fun urlMediaData(jsonObject: JsonObject): MediaResponse? {
//        val listMedia = emptyList<com.lasteyestudios.ipoalerts.data.models.Media>()
//        val list2 = listMedia.toMutableList()
//        val item: JsonObject = jsonObject["item"].asJsonObject
//        try {
//
//            val widthString = objToString(item["w"])
//            val heightString = objToString(item["h"])
//            val authJson = item["ath"] as JsonObject
//            val basePlatformNew =
//                com.lasteyestudios.ipoalerts.data.models.BasePlatform(
//                    name = "moj",
//                    mediaTypes = objToString(item["t"])
//                )
//            val audioJson = item["audioMeta"] as JsonObject
//            val media = com.lasteyestudios.ipoalerts.data.models.Media(
//                id = objToString(jsonObject["videoId"]),
//                basePlatform = basePlatformNew,
//                contentType = objToString(item["t"]),
//                videos = listOf(
//                    com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
//                        url = objToString(item["compressedVideoUrl"]),
//                        height = heightString,
//                        width = widthString
//                    )
//                ),
//                audio = com.lasteyestudios.ipoalerts.data.models.MediaAudio(
//                    id = objToString(audioJson["audioId"]),
//                    artist = objToString(audioJson["audioText"]),
//                    artworkUrl = objToString(audioJson["thumbUrl"]),
//                    title = objToString(audioJson["audioName"]),
//                    media = objToString(audioJson["resourceUrl"]),
//                    metadata = mapOf("compressedThumbUrl" to objToString(audioJson["compressedThumbUrl"]))
//                ),
//                images = listOf(
//                    com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
//                        url = objToString(item["thumb"]),
//                        height = heightString,
//                        width = widthString
//                    )
//                ),
//                metrics = com.lasteyestudios.ipoalerts.data.models.MediaMetrics(
//                    likes = objToInt(item["lc"]),
//                    comments = objToInt(item["c2"]),
//                    share = objToInt(item["usc"]),
//                    views = null
//                ),
//                description = objToString(item["c"]),
//                user = com.lasteyestudios.ipoalerts.data.models.User(
//                    id = objToString(authJson["a"]),
//                    name = objToString(authJson["n"]),
//                    username = objToString(authJson["h"]),
//                    basePlatform = basePlatformNew,
//                    metrics = com.lasteyestudios.ipoalerts.data.models.UserMetrics(
//                        likes = objToString(authJson["likeCount"]), followers = null,
//                        media = basePlatformNew.mediaTypes,
//                        following = null
//                    ),
//                    profile = com.lasteyestudios.ipoalerts.data.models.UserProfile(
//                        verified = null,
//                        pics = listOf(
//                            mapOf("PhotoUrl" to objToString(authJson["pu"])),
//                            mapOf("ThumbUrl" to objToString(authJson["tu"])),
//                            mapOf("coverPic" to objToString(authJson["coverPic"]))
//                        ),
//                        category = null,
//                        description = objToString(authJson["s"]),
//                        externalUrl = null
//                    ),
//                    metadata = null
//                )
//            )
//            if (media.id != null) {
//                list2.add(media)
//            }
//        } catch (e: Exception) {
//            Log.d("tag", "error: $e")
//        }
//        return MediaResponse(list2.toList(), null)
//    }

    private fun ipoAllotmentHelper(d: JsonArray?): List<Company> {
        val listMedia = emptyList<Company>()
        val list2 = listMedia.toMutableList()
        if (d != null) {
            for (i in 0 until d.size()) {
                val jsonObject = d[i]?.asJsonObject
                val jsonElementCompany = jsonObject?.get("company")?.asJsonObject
                val company = Company(
                    canApply = if (jsonObject?.get("canApply") != null) jsonObject.get("canApply").asBoolean else false,
                    statusEnable = if (jsonObject?.get("statusEnable") != null) jsonObject.get("statusEnable").asBoolean else false,
                    searchId = objToString(jsonElementCompany?.get("searchId")),
                    additionalTxt = objToString(jsonElementCompany?.get("additionalTxt")),
                    biddingEndDate = objToString(jsonElementCompany?.get("biddingEndDate")),
                    biddingStartDate = objToString(jsonElementCompany?.get("biddingStartDate")),
                    documentUrl = objToString(jsonElementCompany?.get("documentUrl")),
                    growwShortName = objToString(jsonElementCompany?.get("growwShortName")),
                    issuePrice = objToString(jsonElementCompany?.get("issuePrice")),
                    issueSize = objToString(jsonElementCompany?.get("issueSize")),
                    listingDate = objToString(jsonElementCompany?.get("listingDate")),
                    listingGains = objToString(jsonElementCompany?.get("listingGains")),
                    listingPrice = objToString(jsonElementCompany?.get("listingPrice")),
                    logoUrl = objToString(jsonElementCompany?.get("logoUrl")),
                    lotSize = objToString(jsonElementCompany?.get("lotSize")),
                    maxPrice = objToString(jsonElementCompany?.get("maxPrice")),
                    minBidQuantity = objToString(jsonElementCompany?.get("minBidQuantity")),
                    minPrice = objToString(jsonElementCompany?.get("minPrice")),
                    name = objToString(jsonElementCompany?.get("name")),
                    retailSubscriptionRate = objToString(jsonElementCompany?.get("retailSubscriptionRate")),
                    status = objToString(jsonElementCompany?.get("status")),
                    symbol = objToString(jsonElementCompany?.get("symbol"))
                )
                if (company.searchId != "") {
                    list2.add(company)
                }
            }
        }
        return list2.toList()

    }

    fun growIPOListingToData(res: JsonObject?): IPOListingModel? {
        try {

            val active = res?.get("ACTIVE")?.asJsonArray
            val closed = res?.get("CLOSED")?.asJsonArray
            val upcoming = res?.get("UPCOMING")?.asJsonArray
            val listed = res?.get("LISTED")?.asJsonArray
            Log.d(IPO_LOG_TAG, "upcoming $upcoming")

            return IPOListingModel(
                ACTIVE = ipoAllotmentHelper(active),
                CLOSED = ipoAllotmentHelper(closed),
                LISTED = ipoAllotmentHelper(listed),
                UPCOMING = ipoAllotmentHelper(upcoming)

            )
        } catch (e: Exception) {
            Log.d(
                IPO_LOG_TAG,
                "transformer growIPOListingToData error -> ${e.stackTraceToString()}"
            )
        }
        return null

    }

    private fun financialsDetailsHelper(financials: JsonArray?): List<Financial>? {
        val listMedia = emptyList<Financial>()
        val list2 = listMedia.toMutableList()

        if (financials != null) {
            for (i in 0 until financials.size()) {
                val temp = financials[i].asJsonObject
                val year = temp["yearly"].asJsonObject
                val res = Financial(
                    title = objToString(temp["title"]),
                    yearly = Yearly(
                        lastToLastToLastYear = objToString(year[LAST_TO_LAST_TO_LAST_YEAR]),
                        lastToLastYear = objToString(year[LAST_TO_LAST_YEAR]),
                        lastYear = objToString(year[LAST_YEAR])
                    )
                )
                if (res.title != "") {
                    list2.add(res)
                }
            }
            return list2.toList()
        }
        return null
    }

    private fun subscriptionRatesDetailsHelper(data: JsonArray?): List<SubscriptionRate>? {
        val listMedia = emptyList<SubscriptionRate>()
        val list2 = listMedia.toMutableList()

        if (data != null) {
            for (i in 0 until data.size()) {
                val temp = data[i].asJsonObject
                val res = SubscriptionRate(
                    category = objToString(temp["category"]),
                    categoryName = objToString(temp["categoryName"]),
                    subscriptionRate = objToString(temp["subscriptionRate"])
                )
                if (res.category != "") {
                    list2.add(res)
                }
            }
            return list2.toList()
        }
        return null
    }

    private fun listedOnDetailsHelper(d: JsonArray?): List<String>? {
        val listMedia = emptyList<String>()
        val list2 = listMedia.toMutableList()

        if (d != null) {
            for (i in 0 until d.size()) {
                val temp = objToString(d[i])
                list2.add(temp)
            }
            return list2.toList()
        }
        return null
    }

    fun growIPODetailsToDetails(d: JsonObject?): IPODetailsModel? {
        try {
            val aboutCompany = d?.get("aboutCompany")?.asJsonObject
            val listing = d?.get("listing")?.asJsonObject
            val financials = d?.get("financials")?.asJsonArray
            val subscriptionRates = d?.get("subscriptionRates")?.asJsonArray

            return IPODetailsModel(
                symbol = objToString(d?.get("symbol")),
                minPrice = objToString(d?.get("minPrice")),
                minBidQuantity = objToString(d?.get("minBidQuantity")),
                maxPrice = objToString(d?.get("maxPrice")),
                lotSize = objToString(d?.get("lotSize")),
                logoUrl = objToString(d?.get("logoUrl")),
                listingDate = objToString(d?.get("listingDate")),
                issueSize = objToString(d?.get("issueSize")),
                issuePrice = objToString(d?.get("issuePrice")),
                growwShortName = objToString(d?.get("growwShortName")),
                documentUrl = objToString(d?.get("documentUrl")),
                status = objToString(d?.get("status")),
                aboutCompany = if (aboutCompany != null) AboutCompany(
                    aboutCompany = objToString(aboutCompany["aboutCompany"]),
                    managingDirector = objToString(aboutCompany["managingDirector"]),
                    yearFounded = objToString(aboutCompany["yearFounded"])
                ) else null,
                applicationDetails = objToString(d?.get("applicationDetails")),
                bannerText = objToString(d?.get("bannerText")),
                companyName = objToString(d?.get("companyName")),
                companyShortName = objToString(d?.get("companyShortName")),
                endDate = objToString(d?.get("endDate")),
                financials = financialsDetailsHelper(financials),
                issueType = objToString(d?.get("issueType")),
                listing = Listing(
                    listingPrice = objToString(listing?.get("listingPrice")),
                    listedOn = listedOnDetailsHelper(listing?.get("listedOn")?.asJsonArray)
                ),
                minBidQty = objToString(d?.get("minBidQty")),
                startDate = objToString(d?.get("startDate")),
                subscriptionRates = subscriptionRatesDetailsHelper(subscriptionRates),
                subscriptionUpdatedAt = objToString(d?.get("subscriptionUpdatedAt"))
            )
        } catch (e: Exception) {
            Log.d(
                IPO_LOG_TAG,
                "transformer growIPOListingToData error -> ${e.stackTraceToString()}"
            )
        }
        return null
    }

    private fun helperAvailableIPOAllotmentsData(data: String, key: String, gap: Int): String {

        val first = data.indexOf(key, gap) + key.length + 1
        val sec = data.indexOf(key, first) - 2
        val temp = data.substring(first, sec)
        return objToString(temp)
    }

    fun getAvailableIPOAllotmentsData(data: String?): List<AvailableAllotmentModel>? {
        if (!data.isNullOrEmpty()) {
            val listMedia = emptyList<AvailableAllotmentModel>()
            val list2 = listMedia.toMutableList()

//            data.replace("\\u003c", "",true)
//            data.replace("\\u003e", "",true)
            data.replace("\\r", "", true)
            data.replace("\\n", "", true)
//            data.replace("/", "")
//            data.replace("<", "")
//            data.replace(">", "")

//            Log.d(IPO_LOG_TAG, "final data -> $data")

//            if (data=="NewDataSet")
//                return null
            var first = 0
            var sec = 0
            val count = ("\\bTable\\b".toRegex().findAll(data).count()) / 2
//            val count = ("Table".count { data.contains(it) }) / 2
            for (i in 0 until count) {
                first = data.indexOf("Table", sec) + 4
                sec = data.indexOf("Table", first) + 4
//                Log.d(IPO_LOG_TAG, "final first -> $first and sec -> $sec and count -> $count and size -> ${data.length}")

                val availableAllotmentModel = AvailableAllotmentModel(
                    company_id = helperAvailableIPOAllotmentsData(data, "company_id", first),
                    closing_date = helperAvailableIPOAllotmentsData(data, "closing_date", first),
                    COMPANY_SE = helperAvailableIPOAllotmentsData(data, "COMPANY_SE", first),
                    companyname = helperAvailableIPOAllotmentsData(data, "companyname", first),
                    diff = helperAvailableIPOAllotmentsData(data, "diff", first),
                    lead_managers = helperAvailableIPOAllotmentsData(data, "lead_managers", first),
                    offer_price = helperAvailableIPOAllotmentsData(data, "offer_price", first),
                    REGD_OFF = helperAvailableIPOAllotmentsData(data, "REGD_OFF", first),
                    total_shares = helperAvailableIPOAllotmentsData(data, "total_shares", first)
                )

//                Log.d(IPO_LOG_TAG, "final id -> ${availableAllotmentModel.company_id} and se -> ${availableAllotmentModel.COMPANY_SE}")
                if (availableAllotmentModel.company_id != "") {
                    list2.add(availableAllotmentModel)
                }
            }
            return list2.toList()
        }
        return null
    }
}