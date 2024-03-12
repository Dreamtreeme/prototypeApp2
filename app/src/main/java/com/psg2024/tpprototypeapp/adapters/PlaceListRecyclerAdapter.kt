package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.databinding.RecyclerItemListFragmentBinding

class PlaceListRecyclerAdapter(val context: Context, val documents: List<Place>) :Adapter<PlaceListRecyclerAdapter.VH>() {
    inner class VH(val binding: RecyclerItemListFragmentBinding) :ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(context)
        val binding = RecyclerItemListFragmentBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
       return documents.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val place: Place =documents[position]

        holder.binding.tvPlaceName.text = place.place_name
        holder.binding.tvAddress.text = if(place.road_address_name=="") place.address_name else place.road_address_name
        holder.binding.tvDistance.text = "${place.distance}m"
    }

}