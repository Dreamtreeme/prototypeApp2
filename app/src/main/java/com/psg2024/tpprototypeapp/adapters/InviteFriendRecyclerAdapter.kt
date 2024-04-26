package com.psg2024.tpprototypeapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.FriendListID
import com.psg2024.tpprototypeapp.data.FriendRequestID
import com.psg2024.tpprototypeapp.databinding.RecyclerItemListFragmentBinding

class InviteFriendRecyclerAdapter(val context: Context, val documents : List<FriendListID>):
    RecyclerView.Adapter<InviteFriendRecyclerAdapter.VH>() {
    inner class VH(val binding: RecyclerItemListFragmentBinding) : RecyclerView.ViewHolder(binding.root)
    //firebase로 친구목록을 불러와서 리사이클러뷰로 보여주는 어댑터
    //firebase 객체 만들기
    val myId = G.userAccount?.ID
    val firestore = Firebase.firestore

    //친구목록을 불러오는 함수
    fun getFriendList() {
        firestore.collection("MyFriends").document(myId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val friends = document.data?.get("friends") as List<String>
                    //친구목록을 리사이클러뷰로 보여주기
                    showFriendList(friends)
                }
            }
    }



}