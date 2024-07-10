package com.psg2024.tpprototypeapp.fragments

import android.app.Notification.MessagingStyle.Message
import android.os.Bundle
import android.util.Log
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
    ): View {
        binding = FragmentAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.afBtn.setOnClickListener { addFriend() }

    }

    private fun addFriend() {
        val addID = binding.afTil.editText?.text.toString()

        when {
            addID.isEmpty() -> {
                Toast.makeText(requireContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            addID == G.userAccount?.ID -> {
                Toast.makeText(requireContext(), "현재 ID는 입력하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                // Firestore에서 현재 사용자의 친구 목록을 조회
                Firebase.firestore.collection("MyFriends")
                    .document(G.userAccount!!.ID)
                    .get()
                    .addOnSuccessListener { document ->
                        val friendsList = document.data?.get("friends") as? List<String> ?: listOf()
                        if (addID in friendsList) {
                            Toast.makeText(requireContext(), "이미 친구입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Firestore에서 해당 ID를 가진 사용자 조회
                            checkUserIdExists(addID)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("AddFriend", "친구 목록 조회 실패", e)
                    }
            }
        }
    }

    private fun checkUserIdExists(addID: String) {
        Firebase.firestore.collection("idUsers")
            .whereEqualTo("ID", addID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    // 친구 추가 요청 생성
                    createFriendRequest(G.userAccount!!.ID, addID)
                } else {
                    AlertDialog.Builder(requireContext())
                        .setMessage("요청할 ID가 존재하지 않습니다")
                        .setPositiveButton("확인", null)
                        .show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("AddFriend", "사용자 ID 조회 실패", e)
            }
    }

    private fun createFriendRequest(myId: String, friendId: String) {
        val userFriend = Firebase.firestore.collection("friendUsers")
        val request = hashMapOf(
            "ID" to myId,
            "FriendId" to friendId,
            "accept" to "false"
        )

        userFriend.add(request)
            .addOnSuccessListener {
                AlertDialog.Builder(requireContext())
                    .setMessage("친구 추가 요청을 보냈습니다.")
                    .setPositiveButton("확인", null)
                    .show()
            }
            .addOnFailureListener { e ->
                Log.e("AddFriend", "친구 요청 전송 실패", e)
            }
    }



}