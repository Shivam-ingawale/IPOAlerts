package com.lasteyestudios.ipoalerts.tabs.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lasteyestudios.ipoalerts.databinding.FragmentCurrentBinding
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG

class CurrentFragment : Fragment() {

    private var _binding: FragmentCurrentBinding? = null
    private val binding get() = _binding!!

    private val currentViewModel :CurrentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hey.setOnClickListener {
            Log.d(IPO_LOG_TAG, "current fragment -> button clicked")
            currentViewModel.loadData()
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}