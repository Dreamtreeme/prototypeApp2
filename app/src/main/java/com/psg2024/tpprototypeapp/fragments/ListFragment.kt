package com.psg2024.tpprototypeapp.fragments

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.activities.SubMainActivity
import com.psg2024.tpprototypeapp.adapters.RankListRecyclerAdapter
import com.psg2024.tpprototypeapp.data.Rank
import com.psg2024.tpprototypeapp.databinding.FragmentListBinding
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceListBinding

class ListFragment : Fragment() {


    private lateinit var binding : FragmentPlaceListBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentPlaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rankList:MutableList<Rank> = mutableListOf()
        val db = Firebase.firestore.collection("rank")
        db.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val rank = Rank(
                    rank = document.data["rank"].toString(),
                    id =document.data["id"].toString(),
                    arrivalTime = document.data["arrivalTime"].toString()
                )
                rankList.add(rank)
            }
            binding.recyclerView.adapter = RankListRecyclerAdapter(requireContext(), rankList)
        }



    }




}