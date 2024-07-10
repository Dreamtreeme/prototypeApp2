package com.psg2024.tpprototypeapp.activities

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.data.KakaoSearchPlaceResponse
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.data.PlaceMeta
import com.psg2024.tpprototypeapp.databinding.ActivityLocationBinding
import com.psg2024.tpprototypeapp.fragments.MapFragment
import com.psg2024.tpprototypeapp.network.RetrofitHelper
import com.psg2024.tpprototypeapp.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationActivity : AppCompatActivity() {
    val binding by lazy { ActivityLocationBinding.inflate(layoutInflater) }

    var searchQuery:String = "서울시청"  //앱 초기 검색어

    var myLocation: Location?=null




    val LocationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }


    var searchPlaceResponse: KakaoSearchPlaceResponse?= null
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
        LocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            myLocation = p0.lastLocation


            LocationProviderClient.removeLocationUpdates(this)//this:locationCallback 객체

            //위치 정보를 얻었으니.. 키워드 장소 검색 작업시작!
            searchPlaces()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val permissionState: Int =
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            //퍼미션을 요청하는 다이얼로그 보이고, 그 결과를 받아오는 작업을 대신해주는 대행사 이용
            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            //위치정보수집이 허가되어 있다면.. 곧바로 위치정보 얻어오는 작업 시작
            requestMyLocation()
        }

        //처음 보여질 프레그먼트를 화면에 붙이기
        supportFragmentManager.beginTransaction().add(R.id.container_fragment2, MapFragment())
            .commit()

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.searchLocation.setOnClickListener {
            searchQuery = binding.searchBar.text.toString()

            if(searchQuery== ""){
                Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }else{
                searchPlaces()
            }
        }
    }//oncreate ---------------------------------------------------------


    fun searchPlaces() {
        G.documents!!.clear()


        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitApiService = retrofit.create(RetrofitService::class.java)
        val call = retrofitApiService.searchPlace(
            searchQuery,)
        call.enqueue(object : Callback<KakaoSearchPlaceResponse> {
            override fun onResponse(
                call: Call<KakaoSearchPlaceResponse>,
                response: Response<KakaoSearchPlaceResponse>
            ) {

                searchPlaceResponse = response.body()
                G.documents?.addAll(searchPlaceResponse?.documents as MutableList<Place> )

                val meta: PlaceMeta? = searchPlaceResponse?.meta
                val documents: MutableList<Place>? = searchPlaceResponse?.documents

                onFunctionExecuted()


            }

            override fun onFailure(call: Call<KakaoSearchPlaceResponse>, t: Throwable) {
                Toast.makeText(this@LocationActivity, "서버 오류가 있습니다.", Toast.LENGTH_SHORT).show()
            }
        })


    }
    fun onFunctionExecuted() {
        // FragmentManager 객체 가져오기
        val fragmentManager = supportFragmentManager

        // FragmentTransaction 객체 생성
        val transaction = fragmentManager.beginTransaction()

        // 기존 Fragment를 새 Fragment로 교체
        transaction.replace(R.id.container_fragment2, MapFragment())

        // 변경 사항 적용
        transaction.commit()
    }
}