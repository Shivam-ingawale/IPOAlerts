package com.lasteyestudios.ipoalerts.tabs.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lasteyestudios.ipoalerts.databinding.FragmentIpoDetailsBinding
import com.lasteyestudios.ipoalerts.tabs.common.SharedViewModel
import com.lasteyestudios.ipoalerts.utils.DETAILFRAGMENTSEARCHID

class DetailsFragment : Fragment() {

    private var _binding: FragmentIpoDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private lateinit var searchId: String
    private val detailsViewModel: DetailsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIpoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchId = sharedViewModel.indexStateBundle.getString(DETAILFRAGMENTSEARCHID).toString()
        detailsViewModel.loadData(searchId)
        detailsViewModel.detailsIPOs.observe(viewLifecycleOwner, {

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}