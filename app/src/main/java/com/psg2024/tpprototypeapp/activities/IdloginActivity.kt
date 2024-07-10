package com.psg2024.tpprototypeapp.activities

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.UserAccount

import com.psg2024.tpprototypeapp.databinding.ActivityIdloginBinding

class IdloginActivity : AppCompatActivity() {
    lateinit var db: SQLiteDatabase

    private val binding by lazy { ActivityIdloginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnSignin.setOnClickListener {
            clickSignIn()

        }
    }

    private fun clickSignIn() {
        val ID = binding.inputLayoutID.editText!!.text.toString()
        val password = binding.inputLayoutPassword.editText!!.text.toString()

        val userRef: CollectionReference = Firebase.firestore.collection("idUsers")
        userRef.whereEqualTo("ID", ID).whereEqualTo("password", password).get()
            .addOnSuccessListener {
                if (it.documents.size > 0) {
                    val id: String = it.documents[0].id
                    G.userAccount = UserAccount(id, ID)
                    AlertDialog.Builder(this).setMessage("로그인이 성공했습니다.")
                        .setPositiveButton("확인") { p0, p1 -> login() }.show()
                } else {
                    AlertDialog.Builder(this).setMessage("ID와 비밀번호를 다시 확인해주세요").create().show()
                    binding.inputLayoutID.editText!!.requestFocus()
                    binding.inputLayoutID.editText!!.selectAll()
                }


            }
    }

    fun login() {
            val intent: Intent = Intent(this, MainActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }





}



