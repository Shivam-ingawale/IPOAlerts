package com.lasteyestudios.ipoalerts.data.models.growipolisting

data class GrowIPOListing(
    val ACTIVE: List<Company>,
    val CLOSED: List<Company>,
    val LISTED: List<Company>,
    val UPCOMING: List<Company>
)