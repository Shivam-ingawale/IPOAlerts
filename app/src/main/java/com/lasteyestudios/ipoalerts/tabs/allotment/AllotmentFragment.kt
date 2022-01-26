package com.lasteyestudios.ipoalerts.tabs.allotment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.databinding.FragmentAllotmentBinding
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG

class AllotmentFragment : Fragment() {

    private var _binding: FragmentAllotmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: AllotmentAdapter
    private val allotmentViewModel: AllotmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        allotmentViewModel.loadAllotmentIPOData()
        _binding = FragmentAllotmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter= AllotmentAdapter {id,name->

            findNavController().navigate(AllotmentFragmentDirections.actionListedFragmentToSearchAllotmentFragment(
                companyId = id,
                companyName = name))

        }
//        binding.textView.setOnClickListener {
//
//            findNavController().navigate(R.id.action_ListedFragment_to_watchListFragment2)
//        }
        allotmentViewModel.allotmentIPOs.observe(viewLifecycleOwner) { myResponse ->
            when (myResponse) {
                Response.Error -> {
                    handleRetry()
                }
                Response.Loading -> {
                    binding.retryAllotmentFab.visibility = View.INVISIBLE
//                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {

                    mAdapter.submitList(myResponse.data)
                }
            }
        }
        binding.allotmentRecyclerView.adapter = mAdapter
    }

    private fun handleRetry() {
        binding.retryAllotmentFab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                Log.d(IPO_LOG_TAG, "handleRetry clicked")
                allotmentViewModel.loadAllotmentIPOData()
            }
        }
//        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}