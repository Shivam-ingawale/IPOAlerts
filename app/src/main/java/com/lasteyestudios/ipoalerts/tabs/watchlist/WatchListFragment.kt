package com.lasteyestudios.ipoalerts.tabs.watchlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.databinding.FragmentWatchListBinding
import com.lasteyestudios.ipoalerts.tabs.current.ItemRecyclerAdapter


class WatchListFragment : Fragment() {

    private var _binding: FragmentWatchListBinding? = null
    private val binding get() = _binding!!


    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private val watchListViewModel: WatchListViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        // Inflate the layout for this fragment
        _binding = FragmentWatchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemRecyclerAdapter = ItemRecyclerAdapter(requireContext(), { searchId, growwShortName ->
            findNavController().navigate(WatchListFragmentDirections.actionWatchListFragmentToDetailsFragment3(
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


        watchListViewModel.getAllCompanyWishlist.observe(viewLifecycleOwner) {
            itemRecyclerAdapter.submitList(watchListViewModel.getListCompanyFromCompanyLocal(it))
        }
        binding.watchlistRecycler.adapter = itemRecyclerAdapter





//        binding.text.setOnClickListener {

//            findNavController().navigate(R.id.action_watchListFragment_to_listedFragment)
//        }

//        binding.text.setOnClickListener {
//
//            notificationGroup("hello","you bitch!!!!!")
//            notificationGroup("hello5454","you bit1ch!!!!!")
//
//        }
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}