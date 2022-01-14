package com.lasteyestudios.ipoalerts.data.ipo.common

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lasteyestudios.ipoalerts.data.models.MediaResponse

class Transformer {
    /*
    * UTIL functions
    * */
    private fun objToInt(temp:Any?) :Int?{
        var i : Int? = null
        try{
            if(temp!=null) {
                i = temp.toString()?.replace(("[^\\d.]").toRegex(), "").toInt()
            }
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return (i)
    }
    private fun objToString(temp:Any?) :String?{
        val i = temp.toString().replace("\"", "")
        return (i)
    }

    /*
    * Specific Transformers
    * */
    fun genericFeedData(d: JsonArray): String? {
        val listMedia = emptyList<com.lasteyestudios.ipoalerts.data.models.Media>()
        val list2 = listMedia.toMutableList()
        try {
            for (i in 0 until d.size()) {
                val jsonElement = d[i] as JsonObject
                val widthString = objToString(jsonElement["w"])
                val heightString = objToString(jsonElement["h"])
                val authorJson = jsonElement["ath"] as JsonObject
                val basePlatformNew =
                    com.lasteyestudios.ipoalerts.data.models.BasePlatform(
                        name = "moj",
                        mediaTypes = objToString(jsonElement["t"])
                    )
                val audioJson = jsonElement["audioMeta"] as JsonObject
                val media = com.lasteyestudios.ipoalerts.data.models.Media(
                    id = objToString(jsonElement["a"]),
                    basePlatform = basePlatformNew,
                    contentType = objToString(jsonElement["t"]),
                    videos = listOf(
                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
                            url = objToString(jsonElement["attributedVideoUrl"]),
                            height = heightString,
                            width = widthString
                        )
                    ),
                    audio = com.lasteyestudios.ipoalerts.data.models.MediaAudio(
                        id = objToString(audioJson["audioId"]),
                        artist = objToString(audioJson["audioText"]),
                        artworkUrl = objToString(audioJson["thumbUrl"]),
                        title = objToString(audioJson["audioName"]),
                        media = objToString(audioJson["resourceUrl"]),
                        metadata = mapOf("compressedThumbUrl" to objToString(audioJson["compressedThumbUrl"]))
                    ),
                    images = listOf(
                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
                            url = objToString(jsonElement["thumb"]),
                            height = heightString,
                            width = widthString
                        )
                    ),
                    metrics = com.lasteyestudios.ipoalerts.data.models.MediaMetrics(
                        likes = objToInt(jsonElement["lc"]),
                        comments = objToInt(jsonElement["c2"]),
                        share = objToInt(jsonElement["usc"]),
                        views = null
                    ),
                    description = objToString(jsonElement["c"]),
                    user = com.lasteyestudios.ipoalerts.data.models.User(
                        id = objToString(authorJson["a"]),
                        name = objToString(authorJson["n"]),
                        username = objToString(authorJson["h"]),
                        basePlatform = basePlatformNew,
                        metrics = com.lasteyestudios.ipoalerts.data.models.UserMetrics(
                            likes = objToString(authorJson["likeCount"]), followers = null,
                            media = basePlatformNew.mediaTypes,
                            following = null
                        ),
                        profile = com.lasteyestudios.ipoalerts.data.models.UserProfile(
                            verified = null,
                            pics = listOf(
                                mapOf("PhotoUrl" to objToString(authorJson["pu"])),
                                mapOf("ThumbUrl" to objToString(authorJson["tu"])),
                                mapOf("coverPic" to objToString(authorJson["coverPic"]))
                            ),
                            category = null,
                            description = objToString(authorJson["s"]),
                            externalUrl = null
                        ),
                        metadata = null
                    )
                )
                if(media.id!=null) {
                    list2.add(media)
                }
            }
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return MediaResponse(list2.toList(), null).toString()
    }

    fun profileFeedData(d: JsonArray): MediaResponse? {
        val listMedia = emptyList<com.lasteyestudios.ipoalerts.data.models.Media>()
        val list2 = listMedia.toMutableList()
        try {
            for (i in 0 until d.size()) {
                val jsonElement = d[i] as JsonObject

                val widthString = objToString(jsonElement["w"])
                val heightString = objToString(jsonElement["h"])
                val authJson = jsonElement["ath"] as JsonObject
                val basePlatformNew =
                    com.lasteyestudios.ipoalerts.data.models.BasePlatform(
                        name = "moj",
                        mediaTypes = objToString(jsonElement["t"])
                    )
                val audioJson = jsonElement["audioMeta"] as JsonObject


                val media = com.lasteyestudios.ipoalerts.data.models.Media(
                    id = objToString(jsonElement["a"]),
                    basePlatform = basePlatformNew,
                    contentType = objToString(jsonElement["t"]),
                    videos = listOf(
                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
                            url = objToString(jsonElement["compressedVideoUrl"])!!,
                            height = heightString,
                            width = widthString
                        )
                    ),
                    audio = com.lasteyestudios.ipoalerts.data.models.MediaAudio(
                        id = objToString(audioJson["audioId"]),
                        artist = objToString(audioJson["audioText"]),
                        artworkUrl = objToString(audioJson["thumbUrl"]),
                        title = objToString(audioJson["audioName"]),
                        media = objToString(audioJson["resourceUrl"]),
                        metadata = mapOf("compressedThumbUrl" to objToString(audioJson["compressedThumbUrl"]))
                    ),
                    images = listOf(
                        com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
                            url = objToString(jsonElement["thumb"]),
                            height = heightString,
                            width = widthString
                        )
                    ),
                    metrics = com.lasteyestudios.ipoalerts.data.models.MediaMetrics(
                        likes = objToInt(jsonElement["lc"]),
                        comments = objToInt(jsonElement["c2"]),
                        share = objToInt(jsonElement["usc"]),
                        views = null
                    ),
                    description = objToString(jsonElement["c"]),
                    user = com.lasteyestudios.ipoalerts.data.models.User(
                        id = objToString(authJson["a"]),
                        name = objToString(authJson["n"]),
                        username = objToString(authJson["h"]),
                        basePlatform = basePlatformNew,
                        metrics = com.lasteyestudios.ipoalerts.data.models.UserMetrics(
                            likes = objToString(authJson["likeCount"]),
                            followers = null,
                            media = basePlatformNew.mediaTypes,
                            following = null
                        ),
                        profile = com.lasteyestudios.ipoalerts.data.models.UserProfile(
                            verified = null,
                            pics = listOf(
                                mapOf("PhotoUrl" to objToString(authJson["pu"])),
                                mapOf("ThumbUrl" to objToString(authJson["tu"])),
                                mapOf("coverPic" to objToString(authJson["coverPic"]))
                            ),
                            category = null,
                            description = objToString(authJson["s"]),
                            externalUrl = null
                        ),
                        metadata = null
                    )
                )
                if(media.id!=null) {
                    list2.add(media)
                }
            }
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return MediaResponse(list2.toList(), null)
    }

    fun urlMediaData(jsonObject: JsonObject): MediaResponse? {
        val listMedia = emptyList<com.lasteyestudios.ipoalerts.data.models.Media>()
        val list2 = listMedia.toMutableList()
        val item : JsonObject = jsonObject["item"].asJsonObject
        try {

            val widthString = objToString(item["w"])
            val heightString = objToString(item["h"])
            val authJson = item["ath"] as JsonObject
            val basePlatformNew =
                com.lasteyestudios.ipoalerts.data.models.BasePlatform(
                    name = "moj",
                    mediaTypes = objToString(item["t"])
                )
            val audioJson = item["audioMeta"] as JsonObject
            val media = com.lasteyestudios.ipoalerts.data.models.Media(
                id = objToString(jsonObject["videoId"]),
                basePlatform = basePlatformNew,
                contentType = objToString(item["t"]),
                videos = listOf(
                    com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
                        url = objToString(item["compressedVideoUrl"]),
                        height = heightString,
                        width = widthString
                    )
                ),
                audio = com.lasteyestudios.ipoalerts.data.models.MediaAudio(
                    id = objToString(audioJson["audioId"]),
                    artist = objToString(audioJson["audioText"]),
                    artworkUrl = objToString(audioJson["thumbUrl"]),
                    title = objToString(audioJson["audioName"]),
                    media = objToString(audioJson["resourceUrl"]),
                    metadata = mapOf("compressedThumbUrl" to objToString(audioJson["compressedThumbUrl"]))
                ),
                images = listOf(
                    com.lasteyestudios.ipoalerts.data.models.MediaCandidate(
                        url = objToString(item["thumb"]),
                        height = heightString,
                        width = widthString
                    )
                ),
                metrics = com.lasteyestudios.ipoalerts.data.models.MediaMetrics(
                    likes = objToInt(item["lc"]),
                    comments = objToInt(item["c2"]),
                    share = objToInt(item["usc"]),
                    views = null
                ),
                description = objToString(item["c"]),
                user = com.lasteyestudios.ipoalerts.data.models.User(
                    id = objToString(authJson["a"]),
                    name = objToString(authJson["n"]),
                    username = objToString(authJson["h"]),
                    basePlatform = basePlatformNew,
                    metrics = com.lasteyestudios.ipoalerts.data.models.UserMetrics(
                        likes = objToString(authJson["likeCount"]), followers = null,
                        media = basePlatformNew.mediaTypes,
                        following = null
                    ),
                    profile = com.lasteyestudios.ipoalerts.data.models.UserProfile(
                        verified = null,
                        pics = listOf(
                            mapOf("PhotoUrl" to objToString(authJson["pu"])),
                            mapOf("ThumbUrl" to objToString(authJson["tu"])),
                            mapOf("coverPic" to objToString(authJson["coverPic"]))
                        ),
                        category = null,
                        description = objToString(authJson["s"]),
                        externalUrl = null
                    ),
                    metadata = null
                )
            )
            if(media.id!=null) {
                list2.add(media)
            }
        } catch (e: Exception) {
            Log.d("tag", "error: $e")
        }
        return MediaResponse(list2.toList(), null)
    }

    fun growIPOListingToData(d: JsonObject?): String? {
        TODO("Not yet implemented")
        return null
    }
}