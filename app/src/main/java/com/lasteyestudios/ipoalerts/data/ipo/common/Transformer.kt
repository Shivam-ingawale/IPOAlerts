package com.lasteyestudios.ipoalerts.data.ipo.common

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.lasteyestudios.ipoalerts.data.models.availableallotmentmodel.AvailableAllotmentModel
import com.lasteyestudios.ipoalerts.data.models.ipodetailsmodel.*
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.IPOListingModel
import com.lasteyestudios.ipoalerts.data.models.searchallotmentresultmodel.SearchAllotmentResultModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import com.lasteyestudios.ipoalerts.utils.LAST_TO_LAST_TO_LAST_YEAR
import com.lasteyestudios.ipoalerts.utils.LAST_TO_LAST_YEAR
import com.lasteyestudios.ipoalerts.utils.LAST_YEAR

class Transformer {
    /*
    * UTIL functions
    * */

    private fun getFormattedNumber(count: String): String {
        if (count == "") {
            return ""
        } else {
            val temp = count.toLong()
            if (temp < 1000) return "" + temp
            if (temp < 100000) return "" + temp / 1000 + "k"
            if (temp < 10000000) return "" + temp / 100000 + "Lac"
            return "" + temp / 10000000 + "Cr"
        }
    }

    private fun objToString(temp: Any?): String {
        if (temp.toString() == "null") {
            return ""
        }
        val i = temp.toString().replace("\"", "")
        return (i)
    }

    private fun helperIPOAllotmentsData(data: String, key: String, gap: Int): String {

        val first = data.indexOf(key, gap) + key.length + 1
        val sec = data.indexOf(key, first) - 2
        val temp = data.substring(first, sec)
        return objToString(temp)
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

    private fun listedOnDetailsHelper(temp: JsonElement?): List<String>? {
        if (temp.toString() == "null") {
            return null
        }
        val d = temp?.asJsonArray
        val listMedia = emptyList<String>()
        val list2 = listMedia.toMutableList()

        if (d != null) {
            for (i in 0 until d.size()) {
                val temp1 = objToString(d.get(i))
                list2.add(temp1)
            }
            return list2.toList()
        }
        return null
    }

    private fun ipoAllotmentHelper(d: JsonArray?): List<Company> {
        val listMedia = emptyList<Company>()
        val list2 = listMedia.toMutableList()
        if (d != null) {
            for (i in 0 until d.size()) {
                val jsonObject = d[i]?.asJsonObject
                val jsonElementCompany = jsonObject?.get("company")?.asJsonObject
                val company = Company(
//                    canApply = if (jsonObject?.get("canApply") != null) jsonObject.get("canApply").asBoolean else false,
//                    statusEnable = if (jsonObject?.get("statusEnable") != null) jsonObject.get("statusEnable").asBoolean else false,
                    searchId = objToString(jsonElementCompany?.get("searchId")),
                    additionalTxt = objToString(jsonElementCompany?.get("additionalTxt")),
//                    biddingEndDate = objToString(jsonElementCompany?.get("biddingEndDate")),
                    biddingStartDate = objToString(jsonElementCompany?.get("biddingStartDate")),
//                    documentUrl = objToString(jsonElementCompany?.get("documentUrl")),
                    growwShortName = objToString(jsonElementCompany?.get("growwShortName")),
                    issuePrice = (objToString(jsonElementCompany?.get("issuePrice"))),
                    issueSize = getFormattedNumber(objToString(jsonElementCompany?.get("issueSize"))),
                    listingDate = objToString(jsonElementCompany?.get("listingDate")),
                    listingGains = objToString(jsonElementCompany?.get("listingGains")),
                    listingPrice = objToString(jsonElementCompany?.get("listingPrice")),
                    logoUrl = objToString(jsonElementCompany?.get("logoUrl")),
//                    lotSize = objToString(jsonElementCompany?.get("lotSize")),
                    maxPrice = objToString(jsonElementCompany?.get("maxPrice")),
                    minBidQuantity = objToString(jsonElementCompany?.get("minBidQuantity")),
                    minPrice = objToString(jsonElementCompany?.get("minPrice")),
//                    name = objToString(jsonElementCompany?.get("name")),
                    retailSubscriptionRate = objToString(jsonElementCompany?.get("retailSubscriptionRate")),
                    status = objToString(jsonElementCompany?.get("status")),
//                    symbol = objToString(jsonElementCompany?.get("symbol")),
                    liked = false
                )
                if (company.searchId != "") {
                    list2.add(company)
                }
            }
        }
        return list2.toList()

    }


    /*
    * Specific Transformers
    * */

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

    fun growIPODetailsToDetails(d: JsonObject?): IPODetailsModel? {
        try {
            val aboutCompany = d?.get("aboutCompany")?.asJsonObject
            val listing = if (d?.get("listing")
                    .toString() != "null"
            ) d?.get("listing")?.asJsonObject else null
            val financials =
                if (d?.get("financials")
                        .toString() != "null"
                ) d?.get("financials")?.asJsonArray else null
            val subscriptionRates =
                if (d?.get("subscriptionRates")
                        .toString() != "null"
                ) d?.get("subscriptionRates")?.asJsonArray else null

            return IPODetailsModel(
                symbol = objToString(d?.get("symbol")),
                minPrice = objToString(d?.get("minPrice")),
                minBidQuantity = objToString(d?.get("minBidQuantity")),
                maxPrice = objToString(d?.get("maxPrice")),
                lotSize = objToString(d?.get("lotSize")),
                logoUrl = objToString(d?.get("logoUrl")),
                listingDate = objToString(d?.get("listingDate")),
                issueSize = getFormattedNumber(objToString(d?.get("issueSize"))),
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
                    listedOn = listedOnDetailsHelper(listing?.get("listedOn"))
                ),
                minBidQty = objToString(d?.get("minBidQty")),
                startDate = objToString(d?.get("startDate")),
                subscriptionRates = subscriptionRatesDetailsHelper(subscriptionRates),
                subscriptionUpdatedAt = objToString(d?.get("subscriptionUpdatedAt")),
                faceValue = objToString(d?.get("faceValue"))
            )
        } catch (e: Exception) {
            Log.d(
                IPO_LOG_TAG,
                "transformer growIPODetailsListingToData error -> ${e.stackTraceToString()}"
            )
        }
        return null
    }


    fun getAvailableIPOAllotmentsData(data: String?): List<AvailableAllotmentModel> {
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

            if (data.length < 12) {
                return emptyList()
            }
            var first: Int
            var sec = 0
            val count = ("\\bTable\\b".toRegex().findAll(data).count()) / 2
//            val count = ("Table".count { data.contains(it) }) / 2
            for (i in 0 until count) {
                first = data.indexOf("Table", sec) + 4
                sec = data.indexOf("Table", first) + 4
//                Log.d(IPO_LOG_TAG, "final first -> $first and sec -> $sec and count -> $count and size -> ${data.length}")

                val availableAllotmentModel = AvailableAllotmentModel(
                    company_id = helperIPOAllotmentsData(data, "company_id", first),
                    closing_date = helperIPOAllotmentsData(data, "closing_date", first),
                    COMPANY_SE = helperIPOAllotmentsData(data, "COMPANY_SE", first),
                    companyname = helperIPOAllotmentsData(data, "companyname", first),
                    diff = helperIPOAllotmentsData(data, "diff", first),
                    lead_managers = helperIPOAllotmentsData(data, "lead_managers", first),
                    offer_price = helperIPOAllotmentsData(data, "offer_price", first),
                    REGD_OFF = helperIPOAllotmentsData(data, "REGD_OFF", first),
                    total_shares = getFormattedNumber(helperIPOAllotmentsData(data,
                        "total_shares",
                        first))
                )

//                Log.d(IPO_LOG_TAG, "final id -> ${availableAllotmentModel.company_id} and se -> ${availableAllotmentModel.COMPANY_SE}")
                if (availableAllotmentModel.company_id != "") {
                    list2.add(availableAllotmentModel)
                }
            }
            return list2.toList()
        }
        return emptyList()
    }

    //        private var i = 0
    fun searchAllotmentResults(data: String?): SearchAllotmentResultModel? {
//    for testing
//        i += 1
//        val data = when (i) {
//            3 -> {
//                "<NewDataSet>  <Table>    <higher_priceband>1180</higher_priceband>    <pull>X</pull>    <offer_price>1180</offer_price>    <NAME1>ASHWIN kumar fucki MOORTHY .</NAME1>    <ALLOT>0</ALLOT>    <SHARES>168</SHARES>    <AMTADJ>0</AMTADJ>    <companyname>Sapphire Foods India Limited - IPO</companyname>    <PEMNDG>Retail</PEMNDG>  </Table></NewDataSet>"
//            }
//            2 -> {
//                "<NewDataSet />"
//            }
//            else -> {
//                "<NewDataSet>  <Table>    <higher_priceband>1180</higher_priceband>    <pull>X</pull>    <offer_price>1180</offer_price>    <NAME1>ASHWIN kumar fucki MOORTHY .</NAME1>    <ALLOT>10</ALLOT>    <SHARES>168</SHARES>    <AMTADJ>0</AMTADJ>    <companyname>Sapphire Foods India Limited - IPO</companyname>    <PEMNDG>Retail</PEMNDG>  </Table></NewDataSet>"
//            }
//        }
//            Log.d(IPO_LOG_TAG, "final data i -> $i")

        if (data != null && data.length > 12) {
            val first: Int = data.indexOf("Table") + 4

            val searchAllotmentResultModel = SearchAllotmentResultModel(
                higher_priceband = helperIPOAllotmentsData(data, "higher_priceband", first),
                pull = helperIPOAllotmentsData(data, "pull", first),
                offer_price = helperIPOAllotmentsData(data, "offer_price", first),
                NAME1 = helperIPOAllotmentsData(data, "NAME1", first),
                ALLOT = helperIPOAllotmentsData(data, "ALLOT", first),
                SHARES = helperIPOAllotmentsData(data, "SHARES", first),
                AMTADJ = helperIPOAllotmentsData(data, "AMTADJ", first),
                companyname = helperIPOAllotmentsData(data, "companyname", first),
                PEMNDG = helperIPOAllotmentsData(data, "PEMNDG", first)
            )
            Log.d(IPO_LOG_TAG,
                "final id -> ${searchAllotmentResultModel.companyname} and e -> $searchAllotmentResultModel")
            return searchAllotmentResultModel
        }
        return null
    }
}