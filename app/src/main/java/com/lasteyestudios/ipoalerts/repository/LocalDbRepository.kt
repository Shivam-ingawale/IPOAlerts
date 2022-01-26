package com.lasteyestudios.ipoalerts.repository

import androidx.lifecycle.LiveData
import com.lasteyestudios.ipoalerts.data.local.CompanyWishlistDao
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel

class LocalDbRepository(private val companyWishlistDao: CompanyWishlistDao) {

    fun getAllCompanyWishlist() : LiveData<List<CompanyLocalModel>> =
        companyWishlistDao.getAllCompanyWishlist()


    fun getAllGrowShortCompanyWishlist() : List<String> =
        companyWishlistDao.getAllGrowShortCompanyWishlist()

    fun getCompanyLocalFromGrowwShortName(growwShortName: String) : CompanyLocalModel =
        companyWishlistDao.getCompanyLocalFromGrowwShortName(growwShortName)

    fun insertCompanyWishlist(companyLocalModel: CompanyLocalModel) =
        companyWishlistDao.insertCompanyWishlist(companyLocalModel)

//    fun updateMediaDownload(growwShortName: String, company: Company) =
//        companyWishlistDao.updateMediaDownloadStatus(mediaId, status)

    fun deleteCompanyWishlistByGrowwShortName(growwShortName: String) =
        companyWishlistDao.deleteCompanyWishlistByGrowwShortName(growwShortName)
}