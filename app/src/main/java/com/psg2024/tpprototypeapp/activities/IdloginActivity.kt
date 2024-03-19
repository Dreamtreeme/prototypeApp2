package com.psg2024.tpprototypeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.UserAccount

import com.psg2024.tpprototypeapp.databinding.ActivityIdloginBinding

class IdloginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityIdloginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnSignin.setOnClickListener { clickSignIn() }
    }

    private fun clickSignIn() {
        var ID = binding.inputLayoutID.editText!!.text.toString()
        var password = binding.inputLayoutPassword.editText!!.text.toString()

        val userRef: CollectionReference = Firebase.firestore.collection("idUsers")
        userRef.whereEqualTo("ID", ID).whereEqualTo("password", password).get().addOnSuccessListener {
            if(it.documents.size > 0){//이메일 비밀번호 검색 경과가 1개 이상이므로 찾았다는것임//로그인에 성공
                //다른 화면에서도 회원정보를 사용할 수도 있어서 전역변수 처럼 ... G클래스에 저장
                val id:String = it.documents[0].id //랜덤하게 만들어진 document 명을 id로 활용!!
                G.userAccount = UserAccount(id,ID)
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("SignupActivity", "Fetching FCM registration token failed", task.exception)
                        return@addOnCompleteListener
                    }
                    G.token = task.result
                    val msg = G.token!!
                    Log.d("SignupActivity", msg)
                    val user: MutableMap<String, Any> = mutableMapOf()
                    if (G.token != null && G.userAccount?.ID != null) {
                        user["${G.userAccount!!.ID} TOKEN"] = G.token!!
                        userRef.document(G.userAccount!!.id).update(user).addOnSuccessListener {
                            AlertDialog.Builder(this).setMessage("로그인이 성공했습니다.")
                                .setPositiveButton("확인") { p0, p1 -> login() }.show()
                        }
                    }
                }// 회원가입시 FCM토큰 발급

                //로그인 성공했으니..메인화면으로 이동


            }else{
                //이멜일과 비밀번호에 해당하는 document가 없는것. 로그인 실패
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