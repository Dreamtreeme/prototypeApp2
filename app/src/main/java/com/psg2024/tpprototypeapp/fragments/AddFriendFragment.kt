package com.psg2024.tpprototypeapp.fragments

import android.app.Notification.MessagingStyle.Message
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.activities.AddFriendActivity
import com.psg2024.tpprototypeapp.databinding.FragmentAddFriendBinding


class AddFriendFragment : Fragment() {

    private lateinit var binding : FragmentAddFriendBinding
    private lateinit var addID:String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        binding = FragmentAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.afBtn.setOnClickListener { Addfriend() }

    }

    private fun Addfriend(){
        addID=binding.afTil.editText!!.text.toString()
        when {
            addID.isEmpty() -> {
                Toast.makeText(requireContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            addID == G.userAccount!!.ID -> {
                Toast.makeText(requireContext(), "현재 ID는 입력하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                // 다음 조건문 진행
            }
        }

        val userRef: CollectionReference = Firebase.firestore.collection("idUsers")
        userRef.whereEqualTo("ID", addID).get().addOnSuccessListener {
            if (it.documents.size > 0) {
                val userFriend = Firebase.firestore.collection("friendUsers")
                val user: MutableMap<String, String> = mutableMapOf()
                user["ID"]= G.userAccount!!.ID
                user["requestFriend"]= "true"
                user["FriendId"]= addID
                user["accept"]= "false"


                userFriend.document().set(user).addOnSuccessListener {
                    AlertDialog.Builder(requireContext()).setMessage("친구 추가 요청을 보냈습니다.")
                        .setPositiveButton("확인", {p0, p1 -> }).show()
                }// 친구요청을 보내면서 전역변수와 서버에 친구유무를 저장, 후에 친구 요청했을때
                // 1)푸쉬알람, 2)푸쉬알람을 보고 내부 확인(xml만들어서 리사이클러뷰 만들기)해서 수락여부누르면 서버에 변수 바꾸기
                // 3)친구 요청이 수락되어서 둘다 friend상태가 되면 전역변수에 유무 확인해서 친구목록 띄우기

            }
            else {
                AlertDialog.Builder(requireContext()).setMessage("요청할 ID가 존재하지 않습니다")
                    .setPositiveButton("확인", { p0, p1 -> }).show()
            }
        }

    }









}