package com.lasteyestudios.ipoalerts.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel

@Dao
interface CompanyWishlistDao {

    @Query("SELECT * FROM company_wishlist_table ORDER BY timeStamp DESC")
    fun getAllCompanyWishlist() : LiveData<List<CompanyLocalModel>>


    @Query("SELECT Symbol FROM company_wishlist_table ORDER BY timeStamp DESC")
    fun getAllSymbolCompanyWishlist() : List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCompanyWishlist(companyLocalModel: CompanyLocalModel)

    @Query("SELECT * FROM company_wishlist_table WHERE growwShortName = :growwShortName")
    fun getCompanyLocalFromGrowwShortName(growwShortName: String) : CompanyLocalModel



//    @Query("UPDATE company_wishlist_table SET company =:company WHERE growwShortName=:growwShortName")
//    fun updateCompanyWishlist(growwShortName: String, company: Company)

    @Query("DELETE FROM company_wishlist_table WHERE symbol = :Symbol")
    fun deleteCompanyWishlistBySymbol(Symbol: String)
}