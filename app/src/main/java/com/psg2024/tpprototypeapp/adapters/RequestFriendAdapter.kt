package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.G.Companion.FriendRequestList
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
        var friendRequestID=documents[position]

        holder.binding.tvRequest.text = "${friendRequestID.ID} 님이 친구요청을 신청 하셨습니다\n수락하시겠습니까?"

        holder.binding.ivYes.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("friendUsers").document(G.docmentsID!!)
                .update("accept", "true")


            val userFriend = Firebase.firestore.collection("MyFriend")
            val user: MutableMap<String, String> = mutableMapOf()
            user["ID"]= G.userAccount!!.ID
            user["FID"]= friendRequestID.ID


            userFriend.document().set(user).addOnSuccessListener {
                AlertDialog.Builder(context).setMessage("친구추가가 완료되었습니다")
                    .setPositiveButton("확인", {p0, p1 ->deleteDocument() }).show()
            }
            documents.toMutableList().remove(friendRequestID)
            FriendRequestList!!.remove(friendRequestID)
            notifyItemRemoved(position)

        }

        holder.binding.ivNo.setOnClickListener {
            documents.toMutableList().remove(friendRequestID)
            FriendRequestList!!.remove(friendRequestID)
            AlertDialog.Builder(context).setMessage("친구요청을 거절하셨습니다")
                .setPositiveButton("확인", {p0, p1 ->deleteDocument() }).show()
            notifyItemRemoved(position)

        }
    }
    fun deleteDocument() {
        FirebaseFirestore.getInstance()
            .collection("friendUsers").document(G.docmentsID!!).delete()
    }
}