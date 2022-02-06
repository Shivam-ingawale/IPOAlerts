package com.lasteyestudios.ipoalerts.tabs.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lasteyestudios.ipoalerts.R
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.databinding.FragmentIpoDetailsBinding
import com.lasteyestudios.ipoalerts.tabs.watchlist.WatchListViewModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import java.math.RoundingMode


class DetailsFragment : Fragment() {

    private var _binding: FragmentIpoDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchId: String
    private var liked: Boolean = false
    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val watchListViewModel: WatchListViewModel by activityViewModels()

    private lateinit var growwShortName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        watchListViewModel.loadData()

        _binding = FragmentIpoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchId = args.searchId
        growwShortName = args.growwShortName
        liked = args.liked

        detailsViewModel.loadData(searchId = searchId, growwShortName = growwShortName)

        binding.loadingAnimationCard.visibility = View.VISIBLE


        detailsViewModel.detailsIPOs.observe(viewLifecycleOwner) { myResponse ->
            if (myResponse != null) {
                when (myResponse) {
                    Response.Error -> {
                        handleRetry()
                    }
                    Response.Loading -> {
                        binding.retryAllotmentFab.visibility = View.INVISIBLE
                    }
                    is Response.Success -> {

                        binding.loadingAnimationCard.visibility = View.GONE
                        binding.wishlistHeart.visibility = View.INVISIBLE

                        if (myResponse.data?.growwShortName == null) {
                            handleRetry()
                        } else {
                            myResponse.data.let {
                                if (watchListViewModel.getAllSymbolCompanyWishlist.isNotEmpty()) {

                                    for (i in watchListViewModel.getAllSymbolCompanyWishlist.indices) {
//                                        Log.d(IPO_LOG_TAG, "kon bola + ${watchListViewModel.getAllSymbolCompanyWishlist[i]}")
                                        if (myResponse.data?.symbol == watchListViewModel.getAllSymbolCompanyWishlist[i]
                                        ) {
//                                            Log.d(IPO_LOG_TAG, "kon bola bc")
                                            liked = true
                                            break
                                        } else {
                                            liked = false
                                        }
                                    }
                                } else {
                                    liked = false
                                }

                                if ((it.status != "LISTED" || liked)) {
                                    binding.wishlistHeart.visibility = View.VISIBLE
                                    if (liked) {
                                        binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.ic_watch_list))
                                    } else {
                                        binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.ic_heart))
                                    }
                                    binding.wishlistHeart.setOnClickListener { _ ->
                                        liked = !liked
                                        var gainPrice: String = ""
                                        if (!it.listing.listingPrice.isNullOrBlank() && !it.issuePrice.isNullOrBlank()) {
                                            gainPrice =
                                                (it.listing.listingPrice?.toDouble() - it.issuePrice?.toDouble()).toString()
                                        }

                                        val mCompany = Company(biddingStartDate = it.startDate,
                                            growwShortName = it.companyShortName,
                                            issueSize = it.issueSize,
                                            listingDate = it.listingDate,
                                            logoUrl = it.logoUrl,
                                            maxPrice = it.maxPrice,
                                            minBidQuantity = it.minBidQuantity,
                                            minPrice = it.minPrice,
                                            searchId = searchId,
                                            status = it.status,
                                            liked = liked,
                                            additionalTxt = null,
                                            retailSubscriptionRate = null,
                                            issuePrice = it.issuePrice,
                                            listingGains = gainPrice,
                                            listingPrice = it.listing.listingPrice,
                                            symbol = it.symbol
                                        )
                                        if (liked) {
                                            addWatchlistCompany(mCompany)
                                        } else {
                                            deleteWatchlistCompany(mCompany.symbol.toString())
                                        }
                                        if (liked) {
                                            binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.ic_watch_list))
                                        } else {
                                            binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.ic_heart))
                                        }
                                    }
                                }


                                if (!it.companyShortName.isNullOrBlank()) {
                                    binding.companyName.text = it.companyShortName
                                }
                                if (!it.companyName.isNullOrBlank()) {
                                    binding.companyOfficialName.text = it.companyName
                                }
                                if (!it.logoUrl.isNullOrBlank()) {
                                    Glide.with(binding.logoImage.context).load(it.logoUrl)
                                        .fitCenter()
                                        .into(binding.logoImage)
                                }
                                if (!it.bannerText.isNullOrBlank()) {
                                    binding.detailsBanner.banner.text =
                                        it.bannerText
                                }

                                if (!it.listing.listingPrice.isNullOrBlank()) {
                                    binding.detailsListing.listingPrice.text =
                                        rupeeFormat(it.listing.listingPrice)
                                }
                                if (!it.issuePrice.isNullOrBlank()) {
                                    binding.detailsListing.issuePrice.text =
                                        rupeeFormat(it.issuePrice)
                                }
                                if (!it.listing.listingPrice.isNullOrBlank() && !it.issuePrice.isNullOrBlank()) {
                                    val gainPrice =
                                        it.listing.listingPrice?.toDouble() - it.issuePrice?.toDouble()
                                    val gainPercent =
                                        (gainPrice * 100.0 / it.issuePrice?.toDouble())
//                                    Log.d(IPO_LOG_TAG, "it.listing.listingPrice -> ${it.listing.listingPrice} and it.issuePrice ->${it.issuePrice}")
                                    val final =
                                        rupeeFormat((gainPrice).toString()) + " (" + gainPercent.toBigDecimal()
                                            .setScale(2, RoundingMode.UP).toDouble() + "%)"
                                    binding.detailsListing.listingGains.text = final.toString()

                                }


                                if (!it.startDate.isNullOrBlank()) {
                                    binding.detailsImportantDates.openDate.text = it.startDate
                                }
                                if (!it.endDate.isNullOrBlank()) {

                                    binding.detailsImportantDates.closeDate.text = it.endDate
                                }
                                if (it.listingDate != "" && it.listingDate != null) {
                                    if (it.listingDate.length > 10) {
                                        binding.detailsImportantDates.listingDate.text =
                                            it.listingDate.substring(0, 10)
                                    }
                                }

                                if (it.minPrice != "" || it.minBidQuantity != "") {
                                    val s = rupeeFormat(((it.minPrice?.toFloat()
                                        ?: 1.0f) * (it.minBidQuantity?.toFloat()
                                        ?: 1.0f)).toString())
                                    binding.detailsIpoDetails.minimumPrice.text = s
                                }
                                if (!it.lotSize.isNullOrBlank()) {

                                    binding.detailsIpoDetails.lotSize.text = it.lotSize
                                }
                                if (!it.faceValue.isNullOrBlank()) {
                                    binding.detailsIpoDetails.faceValue.text =
                                        rupeeFormat(it.faceValue.toString())
                                }
                                if (!it.minPrice.isNullOrBlank() && !it.maxPrice.isNullOrBlank()) {
                                    val range =
                                        rupeeFormat(it.minPrice.toString()) + " - " + rupeeFormat(
                                            it.maxPrice.toString())
                                    if (range != " - ") {
                                        binding.detailsIpoDetails.priceRange.text = range
                                    }
                                }
                                if (!it.issueSize.isNullOrBlank()) {

                                    binding.detailsIpoDetails.issueSize.text = it.issueSize
                                }
                                if (!it.documentUrl.isNullOrBlank()) {

                                    binding.detailsIpoDetails.ipoDocs.text =
                                        getString(R.string.ipo_docs)
                                    binding.detailsIpoDetails.ipoDocs.setOnClickListener { _ ->
                                        val url = it.documentUrl
                                        val i = Intent(Intent.ACTION_VIEW)
                                        i.data = Uri.parse(url)
                                        startActivity(i)
                                    }
                                }
                                if (!it.listing.listedOn?.get(1)
                                        .isNullOrBlank() && !it.listing.listedOn?.get(0)
                                        .isNullOrBlank()
                                ) {

                                    val t =
                                        it.listing.listedOn?.get(1)
                                            .toString() + " " + it.listing.listedOn?.get(0)
                                            .toString()
                                    binding.detailsIpoDetails.listedOn.text = t
                                }
                                if (!it.issueType.isNullOrBlank()) {

                                    binding.detailsIpoDetails.issueType.text =
                                        it.issueType.toString()
                                }

                                //subscription rate

                                if (!it.subscriptionRates?.get(0)?.subscriptionRate.isNullOrBlank()) {

                                    binding.detailsSubscriptionRate.retailIndividualInvestor.text =
                                        subscriptionHelper(it.subscriptionRates?.get(0)?.subscriptionRate.toString())
                                }
                                if (!it.subscriptionRates?.get(1)?.subscriptionRate.isNullOrBlank()) {

                                    binding.detailsSubscriptionRate.nonInstitutionalInvestor.text =
                                        subscriptionHelper(it.subscriptionRates?.get(1)?.subscriptionRate.toString())
                                }
                                if (!it.subscriptionRates?.get(2)?.subscriptionRate.isNullOrBlank()) {

                                    binding.detailsSubscriptionRate.qualifiedInstitutionalBuyers.text =
                                        subscriptionHelper(it.subscriptionRates?.get(2)?.subscriptionRate.toString())
                                }

                                //financials

                                if (!it.financials?.get(0)?.yearly?.lastToLastToLastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.revenue2019.text =
                                        rupeeFormat(it.financials?.get(0)?.yearly?.lastToLastToLastYear.toString())
                                }
                                if (!it.financials?.get(0)?.yearly?.lastToLastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.revenue2020.text =
                                        rupeeFormat(it.financials?.get(0)?.yearly?.lastToLastYear.toString())
                                }
                                if (!it.financials?.get(0)?.yearly?.lastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.revenue2021.text =
                                        rupeeFormat(it.financials?.get(0)?.yearly?.lastYear.toString())
                                }

                                if (!it.financials?.get(1)?.yearly?.lastToLastToLastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.totalAssets2019.text =
                                        rupeeFormat(it.financials?.get(1)?.yearly?.lastToLastToLastYear.toString())
                                }
                                if (!it.financials?.get(1)?.yearly?.lastToLastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.totalAssets2020.text =
                                        rupeeFormat(it.financials?.get(1)?.yearly?.lastToLastYear.toString())
                                }
                                if (!it.financials?.get(1)?.yearly?.lastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.totalAssets2021.text =
                                        rupeeFormat(it.financials?.get(1)?.yearly?.lastYear.toString())
                                }


                                if (!it.financials?.get(2)?.yearly?.lastToLastToLastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.profit2019.text =
                                        rupeeFormat(it.financials?.get(2)?.yearly?.lastToLastToLastYear.toString())
                                }
                                if (!it.financials?.get(2)?.yearly?.lastToLastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.profit2020.text =
                                        rupeeFormat(it.financials?.get(2)?.yearly?.lastToLastYear.toString())
                                }
                                if (!it.financials?.get(2)?.yearly?.lastYear.isNullOrBlank()) {

                                    binding.detailsFinancials.profit2021.text =
                                        rupeeFormat(it.financials?.get(2)?.yearly?.lastYear.toString())
                                }


                                //aboutCompany rate

                                if (!it.aboutCompany?.yearFounded.isNullOrBlank()) {

                                    binding.detailsAboutCompany.founded.text =
                                        it.aboutCompany?.yearFounded
                                }
                                if (!it.aboutCompany?.managingDirector.isNullOrBlank()) {

                                    binding.detailsAboutCompany.managingDirector.text =
                                        it.aboutCompany?.managingDirector
                                }
                                if (!it.companyName.isNullOrBlank()) {

                                    binding.detailsAboutCompany.parentOrganisation.text =
                                        it.companyName
                                }
                                if (!it.aboutCompany?.aboutCompany.isNullOrBlank()) {

                                    binding.detailsAboutCompany.aboutCompanyDescription.text =
                                        formattedDescription(it.aboutCompany?.aboutCompany)

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deleteWatchlistCompany(deleteSymbol: String) {
        watchListViewModel.deleteCompanyWishlistBySymbol(deleteSymbol)
    }

    private fun addWatchlistCompany(add: Company) {
        watchListViewModel.insertWatchlistCompanyLocal(CompanyLocalModel(0,
            System.currentTimeMillis() / 1000,
            growwShortName = add.growwShortName.toString(),
            SYMBOL = add.symbol.toString(),
            add))

    }

    private fun subscriptionHelper(value: String): String {
        if (value == "" || value == "null" || value == "0") {
            return ""
        }
        return value + "x"
    }

    private fun rupeeFormat(value: String): String {
        value.replace(",", "")
        if (value == "" || value == "null" || value == "0") {
            return ""
        }
        if (value.contains(".")) {
            val f = value.indexOf(".")
            val dotAfterDigit = value.substring(f, value.length)
            val lastDigit = value[f - 1]
            var result = ""
            val len = f - 1
            var nDigits = 0
            for (i in len - 1 downTo 0) {
                result = value[i].toString() + result
                nDigits++
                if (nDigits % 2 == 0 && i > 0) {
                    result = ",$result"
                }
            }
            val temp = result + lastDigit + dotAfterDigit
            return "₹$temp"

        }

        val lastDigit = value[value.length - 1]
        var result = ""
        val len = value.length - 1
        var nDigits = 0
        for (i in len - 1 downTo 0) {
            result = value[i].toString() + result
            nDigits++
            if (nDigits % 2 == 0 && i > 0) {
                result = ",$result"
            }
        }
        val temp = result + lastDigit
        return "₹$temp"
    }

    private fun objToString(temp: Any?): String? {
        return if (temp != null) {
            val i = temp.toString().replace("\"", "")
            (i)
        } else null
    }

    private fun formattedDescription(temp: Any?): String? {
        val temp2 = objToString(temp)
        return if (temp2 != null) {
            val i = temp2.toString().replace("\\n", "\n")
            (i)
        } else null
    }

    private fun handleRetry() {
        binding.retryAllotmentFab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                Log.d(IPO_LOG_TAG, "handleRetry clicked")
                detailsViewModel.loadData(searchId = searchId, growwShortName = growwShortName)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        detailsViewModel.clearData()
        _binding = null
    }

}