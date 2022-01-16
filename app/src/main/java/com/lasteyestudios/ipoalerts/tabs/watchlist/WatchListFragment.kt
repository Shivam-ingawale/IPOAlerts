package com.lasteyestudios.ipoalerts.tabs.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lasteyestudios.ipoalerts.R
import com.lasteyestudios.ipoalerts.databinding.FragmentWatchListBinding

class WatchListFragment : Fragment() {

    private var _binding: FragmentWatchListBinding? = null
    private val binding get() = _binding!!

    private val watchListViewModel: WatchListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWatchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text.setOnClickListener {

            findNavController().navigate(R.id.action_watchListFragment_to_listedFragment)
        }
//        watchListViewModel.loadData()
//        viewLifecycleOwner.lifecycleScope.launch {
//
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}