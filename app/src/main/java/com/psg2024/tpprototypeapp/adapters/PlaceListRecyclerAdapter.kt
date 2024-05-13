package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.Rank
import com.psg2024.tpprototypeapp.databinding.RecyclerItemListFragmentBinding

class RankListRecyclerAdapter(val context: Context, val documents: MutableList<Rank>) : Adapter<RankListRecyclerAdapter.VH>(){
    open inner class VH(val binding: RecyclerItemListFragmentBinding) :ViewHolder(binding.root)

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
            //밀리초 -> 시간+"시간"+(나머지시간 분으로변환)+"분" 으로변환 단 시간이 1 이하면 0시간으로 표시, 남은 분이 1분 이하면 0분으로 표시
            //양수로 변환
            yourArrivalTime*=-1
            val hour = Math.floor(((yourArrivalTime / 3600000).toDouble())).toInt()
            val minute = Math.floor(((yourArrivalTime % 3600000) / 60000).toDouble()).toInt()
            holder.binding.tvWhen.text = "도착시간 보다 ${hour}시간 ${minute}분 빨리 도착하셨습니다. "
        }else if(yourArrivalTime>0){
            val hour = Math.floor(((yourArrivalTime / 3600000).toDouble())).toInt()
            val minute = Math.floor(((yourArrivalTime % 3600000) / 60000).toDouble()).toInt()
            holder.binding.tvWhen.text = "도착시간 보다 ${hour}시간 ${minute}분 늦게 도착하셨습니다. "}


        holder.binding.tvRank.text = rank.rank

        holder.binding.tvId.text = rank.id

    }

}