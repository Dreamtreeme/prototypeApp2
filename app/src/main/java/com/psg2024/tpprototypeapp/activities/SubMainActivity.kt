package com.psg2024.tpprototypeapp.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivitySubMainBinding
import com.psg2024.tpprototypeapp.fragments.InviteFragment
import com.psg2024.tpprototypeapp.fragments.ListFragment
import com.psg2024.tpprototypeapp.fragments.MapFragment
import com.psg2024.tpprototypeapp.fragments.SubMapFragment
import java.util.Timer
import java.util.TimerTask

class SubMainActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    fun runEvery60Seconds() {
        runnable = Runnable {
            // Fragment 재생성 코드
            runOnUiThread {
                requestMyLocation()
                if(myLocation != null){
                    supportFragmentManager.beginTransaction().replace(R.id.container_fragment3, SubMapFragment()).commit()
                    updateLocationInFirestore()}
            }
            // 60초 후에 다시 runnable 실행
            handler.postDelayed(runnable!!, 60000)
        }
        handler.postDelayed(runnable!!, 60000) // 60초 후에 runnable 실행

    }


    val binding by lazy { ActivitySubMainBinding.inflate(layoutInflater) }
    // [ Google Fused Location API 사용 : 라이브러리 play-sevices-location ]

    val LocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }
    var myLocation: Location? = null
    val db = Firebase.firestore.collection(G.collectionName!!)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            AlertDialog.Builder(this).setMessage("방을 나가시겠습니까?").setNegativeButton("취소"){_,_->}.setPositiveButton("확인"){_,_->
                //sharedPreferences 정보 삭제
                deleteSharedPreferences(this, "collectionName")
                //방을 나가면 앱 완전히 꺼지게하고 그 전에 firestore에서 위치정보 삭제

                db.document(G.userAccount!!.ID).delete()
                finishAffinity()}

        }



        //처음 보여질 프레그먼트를 화면에 붙이기
        supportFragmentManager.beginTransaction().add(R.id.container_fragment3, SubMapFragment())
            .commit()

        //bnv의 선택에 따라 Fragment를 동적으로 교체.

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bnv_map ->{
                    requestMyLocation()
                    if(myLocation != null){
                        updateLocationInFirestore()
                    supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, SubMapFragment()).commit()}}

                R.id.menu_bnv_addfriend -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, InviteFragment()).commit()

                R.id.menu_bnv_list -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, ListFragment()).commit()

            }
            true// OnItemSelectedListener의 추상메소드는 리턴값을 가지고 있음. SAM변환을 하면 return키워드를 사용하면 안됨.
        }//binding.bnv.setOnItemSelectedListener



        //퍼미션 요청 및 결과를 받아오는 작업을 대행하는 대행사를 등록
        val permissionResultLauncher: ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) requestMyLocation()
                else Toast.makeText(this, "내 위치정보를 제공하지 않아서 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT)
                    .show()
            }

        // 위치정보 제공에 대한 퍼미션 체크
        val permissionState: Int =
            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            //퍼미션을 요청하는 다이얼로그 보이고, 그 결과를 받아오는 작업을 대신해주는 대행사 이용
            permissionResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            //위치정보수집이 허가되어 있다면.. 곧바로 위치정보 얻어오는 작업 시작
            requestMyLocation()
            Toast.makeText(this, "위치정보 얻어옴", Toast.LENGTH_SHORT).show()
        }

        if(myLocation != null){
        updateLocationInFirestore()}

        runEvery60Seconds()

    }// oncreate----------------------------------------------------

    private fun updateLocationInFirestore() {
        db.document(G.userAccount!!.ID).update("Lat", myLocation!!.latitude, "Long", myLocation!!.longitude)
    }



    //현재 위치를 얻어오는 작업요청 코드가 있는 기능메소드
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
        LocationProviderClient.requestLocationUpdates(
            request,
            locationListener(),
            Looper.getMainLooper()
        )



    }

    fun deleteSharedPreferences(context: Context, key: String) {
        val sharedPreferences = context.getSharedPreferences("YOUR_APP_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply() // 비동기적으로 삭제
        // editor.commit() // 동기적으로 삭제 (더 느릴 수 있지만, 데이터 손실 가능성이 낮음)

    }

    //위치정보가 갱신되면 호출되는 콜백 리스너 객체




    private fun locationListener(): LocationListener = object: LocationListener {
        override fun onLocationChanged(location: Location) {

            val result: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)//3칸짜리 빈 배열 준비 --이 배열안에 계산결과를 넣어줌
            myLocation = location
            Location.distanceBetween(myLocation!!.latitude,myLocation!!.longitude , G.pos[0].toDouble(), G.pos[1].toDouble(), result)
            //위치 갱신이 끝났으니 종료
            LocationProviderClient.removeLocationUpdates(this)
            //result[0] 에 두 지점의 거리를 m미터 단위로 계산하여 가지고 있음.
            if(result[0]<50) {//두 지점의 거리가 50m이내
                if(wasEnter==false){
                    //목적지에 도착하면 알림창 띄우기
                    //알림창의 ok버튼을 누르면 서버에 있는 위치정보를 삭제, 그 후 서버에 id, 도착시간, 등수를 저장
                    AlertDialog.Builder(this@SubMainActivity).setMessage("목적지에 도착하셨습니다!").setPositiveButton("OK",null).create().show()
                    // Firestore 인스턴스 가져오기
                    val db = Firebase.firestore

                            // 등수 정보를 저장할 컬렉션 참조 얻기
                    val ranksCollection = db.collection(G.collectionName+"ranks")

                        // 문서를 생성하고 등수, 아이디, 도착시간 정보 저장
                    ranksCollection.get().addOnSuccessListener { snapshot ->

                        if (snapshot!!.size() == 0) {
                            val rank =1
                            val id = G.userAccount!!.ID
                            val arrivalTime = System.currentTimeMillis()
                            val userinfo = mutableMapOf(
                                "rank" to rank,
                                "id" to id,
                                "arrivalTime" to arrivalTime
                            )
                            ranksCollection.document(G.userAccount!!.ID).set(userinfo)
                        }else if(snapshot.size()>=1){
                            val rank = snapshot.size()+1
                            val id = G.userAccount!!.ID
                            val arrivalTime = System.currentTimeMillis()
                            val userinfo = mutableMapOf(
                                "rank" to rank,
                                "id" to id,
                                "arrivalTime" to arrivalTime
                            )
                            ranksCollection.document(G.userAccount!!.ID).set(userinfo)
                        }
                    }

                    wasEnter=true
                }


            }else{
                wasEnter = false
            }

        }


    }
    var wasEnter = false






}

