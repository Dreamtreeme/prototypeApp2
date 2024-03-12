package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceListBinding
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceMapBinding

class PlaceMapFragment : Fragment() {

    private val binding : FragmentPlaceMapBinding by lazy { FragmentPlaceMapBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}