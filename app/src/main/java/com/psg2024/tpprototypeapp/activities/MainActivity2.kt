package com.psg2024.tpprototypeapp.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.data.Test
import com.psg2024.tpprototypeapp.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {

    val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }


    val permissionResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) requestMyLocation()
            else Toast.makeText(this, "내 위치정보를 제공하지 않아서 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT)
                .show()
        }
    private fun requestMyLocation() {
        //요청 객체 생성
        val request: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000).build()
        //실시간 위치정보 갱신 요청- 퍼미션 코드 체크가 있어야만 함
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(G.collectionName == null){
            G.collectionName = getSharedPreferences(this, "collectionName", "").toString()
            if (G.collectionName != "" && G.collectionName!!.split(",")[0]==G.userAccount!!.ID){
                AlertDialog.Builder(this).setMessage("이전에 생성한 방이 있습니다. 이어서 하시겠습니까?").setNegativeButton("취소"){_,_->
                    //firestore 정보 삭제
                    Firebase.firestore.collection(G.collectionName!!).document(G.userAccount!!.ID).delete()
                    //sharedPreferences 정보 삭제
                    deleteSharedPreferences(this, "collectionName")
                }.setPositiveButton("확인"){_,_->
                    finish()
                    startActivity(Intent(this, SubMainActivity::class.java))
                }.show()

            }
        }

        val permissionState: Int =
            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            //퍼미션을 요청하는 다이얼로그 보이고, 그 결과를 받아오는 작업을 대신해주는 대행사 이용
            permissionResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        clickBtn()
        confirmInvite()

        binding.toolbar.setNavigationOnClickListener { exitbutton() }
        binding.mkroom.setOnClickListener { startActivity(Intent(this, MakeRoomActivity::class.java)) }
        binding.addfriend.setOnClickListener { startActivity(Intent(this, AddFriendActivity::class.java)) }
    }

    private fun exitbutton() {
        AlertDialog.Builder(this).setMessage("앱을 종료하시겠습니까?").setNegativeButton("취소",{p0,p1 -> }).setPositiveButton("확인", {p0,p1 -> finish()}).show()
    }

    private fun clickBtn() {

        // Android 13 버전부터 알림 사용 시 사용자 허가 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionResult = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
                return
            }
        }
    }//clickbtn

    private fun confirmInvite() {
        val db = Firebase.firestore
        db.collection("InviteFriendlist").document(G.userAccount!!.ID).get().addOnSuccessListener {
            if(it.exists()){
                //초대 받은 목록이 있으면
                val inviteroom = it.data?.get("초대된 방") as String
                //초대된 방 문자열에서 문자 추출
                val inviteList = inviteroom.split(",")
                //초대한사람
                val inviter = inviteList[0]
                //초대된 방 목적지
                val place = inviteList[1]

                AlertDialog.Builder(this)
                    .setTitle("초대정보 ")
                    .setMessage("${inviter}님으로부터 ${place}가 목적지인 방으로 초대받았습니다. 수락하시겠습니까?")
                    .setPositiveButton("확인"){_,_->
                        db.collection("InviteFriendlist").document(G.userAccount!!.ID).delete()
                        G.collectionName = inviteroom
                        db.collection(inviteroom).document(inviter).get().addOnSuccessListener {
                            G.pos.add(it.get("LoLat").toString())
                            G.pos.add(it.get("LoLong").toString())
                            val userInformation: MutableMap<String, Any> = mutableMapOf()
                            userInformation["ID"] = G.userAccount!!.ID
                            userInformation["Lat"] =0
                            userInformation["Long"] =0
                            userInformation["LoLat"] = G.pos[0]
                            userInformation["LoLong"] = G.pos[1]
                            db.collection(G.collectionName!!).document(G.userAccount!!.ID).set(userInformation).addOnSuccessListener {
                                Toast.makeText(this, "위치가 등록되었습니다", Toast.LENGTH_SHORT).show()
                                finish()
                                startActivity(Intent(this, SubMainActivity::class.java))
                            }
                        }
                    }.setNegativeButton("취소"){_,_->
                        db.document(G.userAccount!!.ID).delete()
                    }.show()

            }
        }
    }
    // 값 불러오기
    fun getSharedPreferences(context: Context, key: String, defaultValue: Any): Any {
        val sharedPreferences = context.getSharedPreferences("YOUR_APP_NAME", Context.MODE_PRIVATE)
        return when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) ?: ""
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            else -> throw IllegalArgumentException("Unsupported default value type: ${defaultValue.javaClass}")
        }
    }
    fun deleteSharedPreferences(context: Context, key: String) {
        val sharedPreferences = context.getSharedPreferences("YOUR_APP_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply() // 비동기적으로 삭제
        // editor.commit() // 동기적으로 삭제 (더 느릴 수 있지만, 데이터 손실 가능성이 낮음)
    }

}