package com.lasteyestudios.ipoalerts.tabs.current.ipocategory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.databinding.FragmentIpoCategoryBinding
import com.lasteyestudios.ipoalerts.tabs.common.SharedViewModel
import com.lasteyestudios.ipoalerts.tabs.current.ItemRecyclerAdapter
import com.lasteyestudios.ipoalerts.tabs.watchlist.WatchListViewModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG


class IpoCategory : Fragment() {

    private var _binding: FragmentIpoCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val watchListViewModel: WatchListViewModel by activityViewModels()

    private lateinit var ipoCategory: String
    private val args: IpoCategoryArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        watchListViewModel.loadData()

        _binding = FragmentIpoCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ipoCategory = args.ipoCategory

        val value = listOf("Active", "Upcoming", "Listed", "Closed")
        val index  =  value.indexOf(ipoCategory)

//(mCompantListing?.ACTIVE,
//                        mCompantListing?.UPCOMING,
//                        mCompantListing?.LISTED,
//                        mCompantListing?.CLOSED)


        itemRecyclerAdapter = ItemRecyclerAdapter(requireContext(), { searchId, growwShortName ->
            findNavController().navigate(IpoCategoryDirections.actionIpoCategoryToDetailsFragment2(
                growwShortName = growwShortName,
                searchId = searchId))
        }, { deleteGrowwShortName ->
            watchListViewModel.deleteCompanyWishlistByGrowwShortName(deleteGrowwShortName)
        }, { add ->
            watchListViewModel.insertWatchlistCompanyLocal(CompanyLocalModel(0,
                0,
                add.growwShortName.toString(),
                add))
        })

        binding.categoryText.text = ipoCategory


        sharedViewModel.currentIPOs.observe(viewLifecycleOwner) { myResponse ->
            when (myResponse) {
                Response.Error -> {
                    handleRetry()
                }
                Response.Loading -> {

//                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    binding.retryFab.visibility = View.INVISIBLE
//                    Log.d(IPO_LOG_TAG,
//                        "watchListViewModel.getAllGrowShortCompanyWishlist()" + watchListViewModel.getAllGrowShortCompanyWishlist)
                    for (j in myResponse.data.indices) {
                        if (myResponse.data?.get(j) != null) {
                            for (k in myResponse.data?.get(j)?.indices!!) {
                                for (i in watchListViewModel.getAllGrowShortCompanyWishlist.indices) {
                                    if (myResponse.data?.get(j)
                                            ?.get(k)?.growwShortName == watchListViewModel.getAllGrowShortCompanyWishlist[i]
                                    ) {
                                        myResponse.data?.get(j)?.get(k)?.liked = true
                                        break
                                    } else {
                                        myResponse.data?.get(j)?.get(k)?.liked = false
                                    }
                                }
                            }
                        }
                    }
                    itemRecyclerAdapter.submitList(myResponse.data[index])
                }
            }
        }


        binding.ipoCategoryRecycler.adapter = itemRecyclerAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleRetry() {
        binding.retryFab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                Log.d(IPO_LOG_TAG, "handleRetry clicked")
                sharedViewModel.loadHomeIPOData()
            }
        }
//        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }
}