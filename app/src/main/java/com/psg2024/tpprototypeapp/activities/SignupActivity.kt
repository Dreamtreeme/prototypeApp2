package com.psg2024.tpprototypeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.duplicateCheck.setOnClickListener { clickDuplicateCheck() }
        binding.btnSignup.setOnClickListener { clickSignUp() }
    }

    private var ischeck=false
    private fun clickDuplicateCheck() {
        val ID = binding.inputLayoutID.editText!!.text.toString()
        if(ID.isEmpty()){
            AlertDialog.Builder(this)
                .setMessage("ID를 입력해주세요")
                .setPositiveButton("확인", { _, _ ->  })
                .create().show()
            return
        }

        val userRef: CollectionReference = Firebase.firestore.collection("idUsers")
        userRef.whereEqualTo("ID" , ID).get().addOnSuccessListener {
            if (it.documents.size > 0) {
                AlertDialog.Builder(this).setMessage("중복된 ID가 있습니다. 다시 확인하여 입력해주시기 바람니다.").create()
                    .show()
                binding.inputLayoutID.editText!!.requestFocus()
                binding.inputLayoutID.editText!!.selectAll()
                return@addOnSuccessListener
            }

            AlertDialog.Builder(this)
                .setMessage("유효한 ID입니다.")
                .setPositiveButton("확인", { _, _ ->  })
                .create().show()
            ischeck = true
        }
    }

    private fun clickSignUp() {
        if(!ischeck) {
            AlertDialog.Builder(this)
                .setMessage("ID 중복검사를 확인해주세요.")
                .setPositiveButton("확인", { _, _ ->  })
                .create().show()
            return
        }

        val ID = binding.inputLayoutID.editText!!.text.toString()
        val password = binding.inputLayoutPassword.editText!!.text.toString()
        val passwordConform = binding.inputLayoutPasswordConform.editText!!.text.toString()
        if(password.isEmpty()){
            AlertDialog.Builder(this)
                .setMessage("password를 입력해주세요")
                .setPositiveButton("확인", { _, _ ->  })
                .create().show()
            return
        }

        if (password != passwordConform) {
            AlertDialog.Builder(this).setMessage("패스워드가 다릅니다. 다시 확인하여 입력해주세요.").create()
                .show()
            binding.inputLayoutPasswordConform.editText!!.selectAll()
            return
        }

        val userRef: CollectionReference = Firebase.firestore.collection("idUsers")
        val user: MutableMap<String, String> = mutableMapOf()
        user["ID"] = ID
        user["password"] = password

        userRef.document().set(user).addOnSuccessListener {
            AlertDialog.Builder(this)
                .setMessage("축하합니다 \n회원가입이 완료되었습니다")
                .setPositiveButton("확인", { _, _ -> finish() })
                .create().show()

        }
    }
}