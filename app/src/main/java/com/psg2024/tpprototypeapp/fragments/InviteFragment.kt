package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.adapters.InviteFriendRecyclerAdapter
import com.psg2024.tpprototypeapp.databinding.FragmentInviteFriendBinding

class InviteFragment : Fragment() {
    val myId = G.userAccount?.ID
    val firestore = Firebase.firestore



    private lateinit var binding : FragmentInviteFriendBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentInviteFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFriendList()
        if(G.friendList.size>0){
            binding.ifRecyclerView.adapter = InviteFriendRecyclerAdapter(requireContext(), G.friendList)
            binding.ifRecyclerView.adapter!!.notifyDataSetChanged()
        }

    }
    //친구목록을 불러오는 함수
    fun getFriendList() {
        firestore.collection("MyFriends").document(myId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    G.friendList= document.data?.get("friends") as MutableList<String>

                }
            }
        }


}
