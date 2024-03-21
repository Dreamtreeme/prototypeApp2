package com.psg2024.tpprototypeapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
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
    val binding by lazy{ActivitySubMainBinding.inflate(layoutInflater)}
    // [ Google Fused Location API 사용 : 라이브러리 play-sevices-location ]

    val LocationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    var myLocation: Location?=null



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
            locationCallback,
            Looper.getMainLooper()
        )
    }
    //위치정보 갱신때마다 발동하는 콜백 객체
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            myLocation = p0.lastLocation

            //위치 탐색이 종료되었으니 내 위치정보 업데이트를 이제 그만..
            LocationProviderClient.removeLocationUpdates(this)//this:locationCallback 객체


        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //처음 보여질 프레그먼트를 화면에 붙이기
        supportFragmentManager.beginTransaction().add(R.id.container_fragment3, SubMapFragment())
            .commit()

        //bnv의 선택에 따라 Fragment를 동적으로 교체.

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bnv_map -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, SubMapFragment()).commit()

                R.id.menu_bnv_addfriend -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, InviteFragment()).commit()

                R.id.menu_bnv_list -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, ListFragment()).commit()

            }
            true// OnItemSelectedListener의 추상메소드는 리턴값을 가지고 있음. SAM변환을 하면 return키워드를 사용하면 안됨.
        }//binding.bnv.setOnItemSelectedListener

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

        var db = Firebase.firestore.collection(G.collectionName!!)
        var userInformation:MutableMap<String, String> = mutableMapOf()
        userInformation["ID"] = G.userAccount!!.ID
        userInformation["Lat"] = G.pos[0]
        userInformation["Long"] = G.pos[1]
        db.document().set(userInformation).addOnSuccessListener {
            Toast.makeText(this, "위치가 등록되었습니다", Toast.LENGTH_SHORT).show()
        }

        db.document().addSnapshotListener {it, e ->

        }

    }// oncreate----------------------------------------------------
    private val timer = Timer()

    override fun onStart() {
        super.onStart()
        timer.schedule(object : TimerTask() {
            override fun run() {
                requestMyLocation()
            }
        }, 0, 60000)
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    //퍼미션 요청 및 결과를 받아오는 작업을 대행하는 대행사를 등록
    val permissionResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) requestMyLocation()
            else Toast.makeText(this, "내 위치정보를 제공하지 않아서 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT)
                .show()
        }

}