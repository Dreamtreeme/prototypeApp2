package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.FriendRequestID
import com.psg2024.tpprototypeapp.databinding.RecyclerItemFriendRequestBinding
import com.psg2024.tpprototypeapp.databinding.RecyclerItemListFragmentBinding

class RequestFriendAdapter(val context: Context, val documents : List<FriendRequestID>) : Adapter<RequestFriendAdapter.VH> (){

    inner class VH(val binding: RecyclerItemFriendRequestBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(context)
        val binding = RecyclerItemFriendRequestBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val friendRequestList:FriendRequestID=documents[position]

        holder.binding.tvRequest.text = friendRequestList.ID

        holder.binding.ivYes.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("friendUsers").document(G.docmentsID!!)
                .update("accept", "true")
            Toast.makeText(context, "친구추가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}