package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceFavorBinding
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceListBinding

class PlaceFavorFragment : Fragment() {

    private lateinit var binding : FragmentPlaceFavorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceFavorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}