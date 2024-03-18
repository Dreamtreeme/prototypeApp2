package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
                user[G.userAccount!!.ID]= "friend"
                user[addID]="unfriend"

            }
            else {
                AlertDialog.Builder(requireContext()).setMessage("요청할 ID가 존재하지 않습니다")
                    .setPositiveButton("확인", { p0, p1 -> }).show()
            }
        }

    }




}