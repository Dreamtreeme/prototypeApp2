package com.psg2024.tpprototypeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.data.UserAccount
import com.psg2024.tpprototypeapp.databinding.ActivityEmailloginBinding

class EmailloginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEmailloginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnSignin.setOnClickListener { clickSignIn() }
    }

    private fun clickSignIn() {
        var email = binding.inputLayoutEmail.editText!!.text.toString()
        var password = binding.inputLayoutPassword.editText!!.text.toString()

        val userRef: CollectionReference = Firebase.firestore.collection("emailUsers")
        userRef.whereEqualTo("email", email).whereEqualTo("password", password).get().addOnSuccessListener {
            if(it.documents.size > 0){//이메일 비밀번호 검색 경과가 1개 이상이므로 찾았다는것임//로그인에 성공
                //다른 화면에서도 회원정보를 사용할 수도 있어서 전역변수 처럼 ... G클래스에 저장
                val id:String = it.documents[0].id //랜덤하게 만들어진 document 명을 id로 활용!!
                G.userAccount = UserAccount(id,email)

                //로그인 성공했으니..메인화면으로 이동
                val intent:Intent=Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }else{
                //이멜일과 비밀번호에 해당하는 document가 없는것. 로그인 실패
                AlertDialog.Builder(this).setMessage("이메일과 비밀번호를 다시 확인해주세요").create().show()
                binding.inputLayoutEmail.editText!!.requestFocus()
                binding.inputLayoutEmail.editText!!.selectAll()
            }
        }
    }
}