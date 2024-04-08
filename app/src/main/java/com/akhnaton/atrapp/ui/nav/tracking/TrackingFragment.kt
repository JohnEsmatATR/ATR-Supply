package com.akhnaton.atrapp.ui.nav.tracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.FragmentTrackingBinding

class TrackingFragment : Fragment() {
    lateinit var binding: FragmentTrackingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater)

        init()
        onClick()

        return binding.root
    }

    private fun init() {

    }

    private fun onClick() {

    }
}