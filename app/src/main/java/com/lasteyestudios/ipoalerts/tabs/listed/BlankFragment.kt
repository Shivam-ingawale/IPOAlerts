package com.lasteyestudios.ipoalerts.tabs.listed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lasteyestudios.ipoalerts.R
import com.lasteyestudios.ipoalerts.databinding.FragmentBlankBinding

class BlankFragment : Fragment() {

        private var _binding: FragmentBlankBinding? = null
        private val binding get() = _binding!!


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            _binding = FragmentBlankBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

}