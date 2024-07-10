package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.Rank
import com.psg2024.tpprototypeapp.databinding.RecyclerItemListFragmentBinding

class RankListRecyclerAdapter(val context: Context, val documents: MutableList<Rank>) :
    RecyclerView.Adapter<RankListRecyclerAdapter.VH>(){
        inner class VH(val binding: RecyclerItemListFragmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(context)
        val binding = RecyclerItemListFragmentBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
       return documents.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val rank : Rank =documents[position]
        val arrivalTime = rank.arrivalTime.toLong()
        val appointTime=G.collectionName!!.split(",")[2].toLong()
        var yourArrivalTime=(appointTime-arrivalTime).toInt()
        if(yourArrivalTime<0) {
            yourArrivalTime*=-1
            val hour = Math.floor(((yourArrivalTime / 3600000).toDouble())).toInt()
            val minute = Math.floor(((yourArrivalTime % 3600000) / 60000).toDouble()).toInt()
            holder.binding.tvWhen.text = "약속시간 보다 ${hour}시간 ${minute}분 늦게 도착하셨습니다. "
        }else if(yourArrivalTime>0){
            val hour = Math.floor(((yourArrivalTime / 3600000).toDouble())).toInt()
            val minute = Math.floor(((yourArrivalTime % 3600000) / 60000).toDouble()).toInt()
            holder.binding.tvWhen.text = "도착시간 보다 ${hour}시간 ${minute}분 빨리 도착하셨습니다. "}


        holder.binding.tvRank.text = rank.rank

        holder.binding.tvId.text = rank.id

    }

}