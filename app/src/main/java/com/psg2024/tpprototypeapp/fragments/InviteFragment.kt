package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
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
    ): View {
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
        binding.ifBtn.setOnClickListener {
            val invite: MutableMap<String, String> = mutableMapOf()
            invite["초대보낸사람"] = myId!!
            invite["수락여부"]= "대기중"
            invite["초대된 방"]= G.collectionName!!

            G.inviteList.forEach {
                firestore.collection("InviteFriendlist").document(it).set(invite)
            }
            Toast.makeText(requireContext(), "초대가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            //초대가 완료되면 초대목록을 비워준다.
            G.inviteList.clear()
            //SubMainActivity에 있는 바텀네비게이션뷰 지도 버튼누르기
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment3, InviteFragment()).commit()


        }

    }
    //친구목록을 불러오는 함수
    private fun getFriendList() {
        firestore.collection("MyFriends").document(myId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val friends = document.data?.get("friends") as? MutableList<String>
                    G.friendList = friends ?: mutableListOf()
                } else {
                    G.friendList = mutableListOf()
                }
                // 어댑터를 초기화하고 데이터를 반영
                binding.ifRecyclerView.adapter = InviteFriendRecyclerAdapter(requireContext(), G.friendList)
                binding.ifRecyclerView.adapter!!.notifyDataSetChanged()
            }
            .addOnFailureListener {
                G.friendList = mutableListOf()
                // 실패한 경우 빈 리스트로 초기화하고 사용자에게 알림
                Toast.makeText(requireContext(), "친구 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }



}
