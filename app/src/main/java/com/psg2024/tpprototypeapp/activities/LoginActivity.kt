package com.psg2024.tpprototypeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psg2024.tpprototypeapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {


    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //회원가입 버튼 클릭
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        //이메일 로그인 버튼 클릭
        binding.btnLogin.setOnClickListener {

            startActivity(Intent(this, IdloginActivity::class.java))
        }

        //간편로그인 버튼들 클릭
//        binding.btnLoginKakao.setOnClickListener { clickKakao() }
//        binding.btnLoginGoogle.setOnClickListener { clickGoogle() }
//        binding.btnLoginNaver.setOnClickListener { clickNaver() }


    }











//    private fun clickKakao() {
//        val callback:(OAuthToken?, Throwable?)->Unit = { token, error ->
//            if(error != null) {
//                Toast.makeText(this, "카카오로그인 실패", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "카카오로그인 성공", Toast.LENGTH_SHORT).show()
//
//                UserApiClient.instance.me { user, error ->
//                    if(user!=null){
//                        val id:String = user.id.toString()
//                        val nickname:String = user.kakaoAccount?.profile?.nickname ?: ""
//                        Toast.makeText(this, "$id\n$nickname", Toast.LENGTH_SHORT).show()
//                        G.userAccount = UserAccount(id, nickname)
//
//                        startActivity(Intent(this, MainActivity2::class.java))
//                        finish()
//                    }
//                }
//            }
//        }
//        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
//                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
//        }else{
//                UserApiClient.instance.loginWithKakaoAccount(this,callback = callback)
//        }
//    }
//
//    private fun clickGoogle() {
//        //로그인 옵션객체 생성 - Builder - 이메일 요청..
//        val signInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
//
//        //구글 로그인을 하는 화면 액티비티를 실행하는 Intent 객체로 로그인 구현
//        val intent:Intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
//        resultLauncher.launch(intent)
//    }
//
//    // 구글 로그인화면 결과를 받아주는 대행사 등록
//    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        //로그인 결과를 가져온 인텐트 소환
//        val intent:Intent? = it.data
//        //인텐트로 부터 구글 계정 정보를 가져오는 작업자 객체를 소환
//        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
//
//        //작업자로부터 계정 받기
//        val account:GoogleSignInAccount = task.result
//        val id:String = account.id.toString()
//        val email:String= account.email ?: ""
//
//        Toast.makeText(this, "$id\n$email", Toast.LENGTH_SHORT).show()
//        G.userAccount= UserAccount(id, email)
//
//        //main 화면으로 이동
//        startActivity(Intent(this, MainActivity2::class.java))
//        finish()
//    }
//
//    private fun clickNaver() {
//
//        // 네아로SDK 초기화
//        NaverIdLoginSDK.initialize(this, "piMKSj9m88BRBUgvmsfN", "c8G0RW3Qi7", "프로토타입앱")
//
//        // 로그인 요청
//        NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback{
//            override fun onError(errorCode: Int, message: String) {
//                Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(httpStatus: Int, message: String) {
//                Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onSuccess() {
//                Toast.makeText(this@LoginActivity, "로그인성공", Toast.LENGTH_SHORT).show()
//
//                //사용자 정보 받아오기.. -- REST API 받아야 함.
//                //로그인에 성공하면. REST API로 요청할 수 있는 토큰을 발급받음.
//                val accessToken:String? = NaverIdLoginSDK.getAccessToken()
//
//                //Retrofit 작업을 통해 사용자 정보 가져오기
//                val retrofit= RetrofitHelper.getRetrofitInstance("https://openapi.naver.com")
//                val retrofitService = retrofit.create(RetrofitService::class.java)
//                val call= retrofitService.getNidUserInfo("Gearer ${accessToken}")
//                call.enqueue(object : Callback<String>{
//                    override fun onResponse(call: Call<String>, response: Response<String>) {
//                        val s= response.body()
//                        AlertDialog.Builder(this@LoginActivity).setMessage(s).create().show()
//
//                        startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
//                        finish()
//                    }
//
//                    override fun onFailure(call: Call<String>, t: Throwable) {
//                        Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//            }
//        })
//
//    }


}