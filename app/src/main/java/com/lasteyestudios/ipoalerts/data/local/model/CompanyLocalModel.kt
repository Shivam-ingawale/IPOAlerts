package com.lasteyestudios.ipoalerts.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company

interface RecyclerItem

@Entity(tableName = "company_wishlist_table")
data class CompanyLocalModel(
    @PrimaryKey(autoGenerate = true) val ID: Int,
    var timeStamp: Long?,
    @ColumnInfo(name = "ShortName")
    var growwShortName:String,
    @ColumnInfo(name = "SYMBOLS")
    var SYMBOL:String,
    @Embedded
    val company: Company
) : RecyclerItem