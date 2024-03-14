package com.psg2024.tpprototypeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        //툴바의 업버튼 클릭시 돌아가기
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.duplicateCheck.setOnClickListener { clickDuplicateCheck() }
        binding.btnSignup.setOnClickListener { clickSignUp() }
    }


    private fun clickDuplicateCheck() {
        var ID = binding.inputLayoutEmail.editText!!.text.toString()
        //firebase 연결
        auth = Firebase.auth
        if(auth.currentUser.toString()==ID)
    }

    private fun clickSignUp() {
        //Firebase Firestore DB에 사용자 정보를 저장

        var ID = binding.inputLayoutEmail.editText!!.text.toString()
        var password = binding.inputLayoutPassword.editText!!.text.toString()
        var passwordConform = binding.inputLayoutPasswordConform.editText!!.text.toString()

        //유효성 검사 - 패스워드와 패스워드 확인이 맞는지 검사
        if(password != passwordConform){
            AlertDialog.Builder(this).setMessage("패스워드가 다릅니다. 다시 확인하여 입력해주세요.").create().show()
            binding.inputLayoutPasswordConform.editText!!.selectAll()
            return
        }

        // Firebase Firestore DB에 저장하기 -프로젝트 연동부터.

        // "emailUser"이름의 컬렉션 명 참조객체부터 소환
        val userRef: CollectionReference = Firebase.firestore.collection("idUsers")

        // 중복된 이메일은 저장되면 안되기에..
        userRef.whereEqualTo("ID" , ID).get().addOnSuccessListener {
            //혹시 같은 email값을 가진 docment가 여러개 일수도 있어서..
            if(it.documents.size>0){ // 개수가 0개 이상이면.. 같은 email이 있다는 것임.
                AlertDialog.Builder(this).setMessage("중복된 이메일이 있습니다. 다시 확인하여 입력해주시기 바람니다.").create().show()
                binding.inputLayoutEmail.editText!!.requestFocus()
                binding.inputLayoutEmail.editText!!.selectAll()
            }else{//중복된 이메일이 없다면 저장..
                // 저장할 값(이메일, 비밀번호)을 MutableMap으로 묶어주기
                val user: MutableMap<String, String> = mutableMapOf()
                user["email"] = ID
                user["password"]= password

                userRef.document().set(user).addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setMessage("축하합니다 \n회원가입이 완료되었습니다")
                        .setPositiveButton("확인", { p0, p1 -> finish() })
                        .create().show()
                }
            }
        }



    }
}