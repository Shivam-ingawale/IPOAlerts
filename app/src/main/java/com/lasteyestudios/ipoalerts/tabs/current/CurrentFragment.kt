package com.lasteyestudios.ipoalerts.tabs.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.databinding.FragmentIpoBinding
import com.lasteyestudios.ipoalerts.tabs.common.SharedViewModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG

class CurrentFragment : Fragment() {

    private var _binding: FragmentIpoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: BlockRecyclerAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val currentViewModel: CurrentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentIpoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = BlockRecyclerAdapter(requireContext(),{ searchId, growwShortName->
            findNavController().navigate(directions = CurrentFragmentDirections.actionCurrentFragmentToDetailsFragment2(
                searchId = searchId, growwShortName = growwShortName))
        }, {

        })

        sharedViewModel.currentIPOs.observe(viewLifecycleOwner, { myResponse ->
            when (myResponse) {
                Response.Error -> {
                    handleRetry()
                }
                Response.Loading -> {
                    binding.retryFab.visibility = View.INVISIBLE
//                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {

                    mAdapter.submitList(myResponse.data)
                }
            }
        })

        binding.mainRecyclerView.adapter = mAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun handleRetry() {
        binding.retryFab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                Log.d(IPO_LOG_TAG,  "handleRetry clicked")
                sharedViewModel.loadHomeIPOData()
            }
        }
//        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }
}