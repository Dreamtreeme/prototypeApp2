package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.databinding.RecyclerItemFriendListBinding

class InviteFriendRecyclerAdapter(val context: Context, val documents: MutableList<String>):
    RecyclerView.Adapter<InviteFriendRecyclerAdapter.VH>() {
    inner class VH(val binding: RecyclerItemFriendListBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(context)
        val binding = RecyclerItemFriendListBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val friendID = documents[position]

        holder.binding.tvRequest.text = "${friendID} 님"
        holder.binding.cbRequest.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                G.inviteList.add(friendID)
            } else {
                G.inviteList.remove(friendID)
            }
        }
    }


}