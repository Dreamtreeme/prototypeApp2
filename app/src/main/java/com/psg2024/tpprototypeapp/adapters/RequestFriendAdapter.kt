package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.G.Companion.FriendRequestList
import com.psg2024.tpprototypeapp.data.FriendRequestID
import com.psg2024.tpprototypeapp.data.UserFriend
import com.psg2024.tpprototypeapp.databinding.RecyclerItemFriendRequestBinding
import com.psg2024.tpprototypeapp.databinding.RecyclerItemListFragmentBinding

class RequestFriendAdapter(val context: Context, val documents : MutableList<FriendRequestID>) : Adapter<RequestFriendAdapter.VH>() {
    private val firestore = Firebase.firestore

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
        val friendRequestID = documents[position]

        holder.binding.tvRequest.text = "${friendRequestID.ID} 님이 친구요청을 신청 하셨습니다\n수락하시겠습니까?"

        holder.binding.ivYes.setOnClickListener {
            addFriend(G.userAccount!!.ID, friendRequestID.ID)
            deleteDocument()
            documents.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.binding.ivNo.setOnClickListener {
            deleteDocument()
            documents.removeAt(position)
            notifyItemRemoved(position)
            Toast.makeText(context, "친구요청을 거절하셨습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addFriend(myId: String, friendId: String) {
        val myFriends = firestore.collection("MyFriends").document(myId)
        myFriends.update("friends", FieldValue.arrayUnion(friendId))
            .addOnSuccessListener {
                Toast.makeText(context, "친구 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "친구 추가 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        val friendFriends = firestore.collection("MyFriends").document(friendId)
        friendFriends.update("friends", FieldValue.arrayUnion(myId))
            .addOnSuccessListener {
                Toast.makeText(context, "상대방 친구목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "상대방 친구 추가 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteDocument() {
        FirebaseFirestore.getInstance()
            .collection("friendUsers").document(G.docmentsID!!)
            .delete()
    }
}