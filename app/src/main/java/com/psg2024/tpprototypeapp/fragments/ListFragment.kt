package com.psg2024.tpprototypeapp.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.adapters.RankListRecyclerAdapter
import com.psg2024.tpprototypeapp.data.Rank
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceListBinding

class ListFragment : Fragment() {
    val rankList:MutableList<Rank> = mutableListOf()

    private lateinit var binding : FragmentPlaceListBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding= FragmentPlaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRankList()


    }


    private fun getRankList() {
        val db = Firebase.firestore
        db.collection(G.collectionName!!+"ranks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val rank = Rank(
                        document.data["id"].toString(),
                        document.data["rank"].toString(),
                        document.data["arrivalTime"].toString()
                    )
                    rankList.add(rank)
                }
                Toast.makeText(requireContext(), "성공불러옴", Toast.LENGTH_SHORT).show()
                Log.d("ListFragment", "onViewCreated: ${rankList.size}")
                binding.recyclerView.adapter = RankListRecyclerAdapter(requireContext(), rankList)
                binding.recyclerView.adapter!!.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage(exception.message)
                    .setPositiveButton("OK", null)
                    .show()
            }
    }




}