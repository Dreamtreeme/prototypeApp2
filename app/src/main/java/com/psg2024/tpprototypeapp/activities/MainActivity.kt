package com.psg2024.tpprototypeapp.activities

import android.Manifest
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.psg2024.tpprototypeapp.G

import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.data.KakaoSearchPlaceResponse
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.data.PlaceMeta
import com.psg2024.tpprototypeapp.databinding.ActivityMainBinding
import com.psg2024.tpprototypeapp.fragments.PlaceFavorFragment
import com.psg2024.tpprototypeapp.fragments.PlaceListFragment
import com.psg2024.tpprototypeapp.fragments.PlaceMapFragment
import com.psg2024.tpprototypeapp.network.RetrofitHelper
import com.psg2024.tpprototypeapp.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //카카오 검색에 필요한 요청 데이터: query (검색어), x(경도-longitude)y(위도-latitude)
    //1. 검색장소명
    var searchQuery:String = "화장실"  //앱 초기 검색어 - 내 주변 개방화장실
    //2. 현재 내 위치 정보 객체 (위도, 경도 정보를 멤버로 보유)
    var myLocation:Location?=null

    //.




    // [ Google Fused Location API 사용 : 라이브러리 play-sevices-location ]

    val LocationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    // kakao search api 응답결과 객체 참조변수
    var searchPlaceResponse: KakaoSearchPlaceResponse?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clickBtn()

        //처음 보여질 프레그먼트를 화면에 붙이기
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, PlaceListFragment())
            .commit()

        //bnv의 선택에 따라 Fragment를 동적으로 교체.

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bnv_list -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, PlaceListFragment()).commit()

                R.id.menu_bnv_map -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, PlaceMapFragment()).commit()



            }

            true// OnItemSelectedListener의 추상메소드는 리턴값을 가지고 있음. SAM변환을 하면 return키워드를 사용하면 안됨.
        }//onitemselectedListener

        // bnv의 아이템 선택 리플효과의 범위를 제한하지 않기 위해..배경영역을 없애기
        binding.bnv.background = null

        //소프트 키보드의 검색버튼을 클릭하였을 때.
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            searchQuery = binding.etSearch.text.toString()
            //키워드로 장소 검색 요청
            pgIndex1=1
            G.documents!!.clear()
            searchPlaces()
            binding.etSearch.text.clear()
            binding.etSearch.clearFocus()


            //액션버튼이 클릭되었을때 여기서 모든 액션을 소비하지 않았다는 뜻..으로 false를 뜻함
            false
        }

        // 특정 키워드 단축 choice 버튼들에 리스너 처리하는 코드를 별도의 메소드에
        setChoiceButtonsListener()

        // 위치정보 제공에 대한 퍼미션 체크
        val permissionState: Int =
            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            //퍼미션을 요청하는 다이얼로그 보이고, 그 결과를 받아오는 작업을 대신해주는 대행사 이용
            permissionResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            //위치정보수집이 허가되어 있다면.. 곧바로 위치정보 얻어오는 작업 시작
            requestMyLocation()
        }

        //내 위치 갱신버튼 클릭처리
        binding.toolbar.setNavigationOnClickListener { requestMyLocation() }

        // 새로고침 버튼 클릭처리
        binding.fabRefresh.setOnClickListener {
            requestMyLocation()

            ObjectAnimator.ofFloat(it, "translationY", -140f).start()
            ObjectAnimator.ofFloat(it, "rotationX", 360f).start()
        }
    }//oncreate

        //퍼미션 요청 및 결과를 받아오는 작업을 대행하는 대행사를 등록
        val permissionResultLauncher: ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) requestMyLocation()
                else Toast.makeText(this, "내 위치정보를 제공하지 않아서 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT)
                    .show()
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

                //위치 정보를 얻었으니.. 키워드 장소 검색 작업시작!
                searchPlaces()
            }
        }


        private fun setChoiceButtonsListener() {

        }
//멤버변수
        var choiceID = R.id.choice_01

        private fun clickChoice(view: View) {
            //클릭을하면 기존에 선택되었던 이미지뷰를 찾아서 배경이미지를 선택되지 않은 하얀색 원 그림으로 변경
            findViewById<ImageView>(choiceID).setBackgroundResource(R.drawable.bg_choice)

            //현재 클릭한 이미지뷰의 배경을 선택된 회색 원그림으로 변경
            view.setBackgroundResource(R.drawable.bg_choice_selected)
            //클릭한 뷰의 id를 저장
            choiceID = view.id

            when (choiceID) {
                R.id.choice_01 -> searchQuery = "화장실"
                R.id.choice_02 -> searchQuery = "약국"
                R.id.choice_03 -> searchQuery = "주유소"
                R.id.choice_04 -> searchQuery = "전기차 충전소"
                R.id.choice_05 -> searchQuery = "공원"
                R.id.choice_06 -> searchQuery = "주차장"
                R.id.choice_07 -> searchQuery = "편의점"
                R.id.choice_08 -> searchQuery = "식당"
                R.id.choice_09 -> searchQuery = "보석점"
                R.id.choice_10 -> searchQuery = "병원"

            }


            //바뀐 검색 장소명으로 검색 요청
            pgIndex1 = 1
            G.documents!!.clear()
            searchPlaces()

            //검색창에 글씨가 있다면 지우기..
            binding.etSearch.text.clear()
            binding.etSearch.clearFocus()
        }

        var pgIndex1 = 1

        //카카오 로컬 검색 API를 활용하여 키워드로 장소를 검색하는 기능 메소드
        fun searchPlaces() {
//            Toast.makeText(
//                this,
//                "$searchQuery\n${myLocation?.latitude},${myLocation?.longitude}",
//                Toast.LENGTH_SHORT
//            ).show()
            //레트로핏을 이용한 REST API 작업 수행 -GET방식
            val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
            val retrofitApiService = retrofit.create(RetrofitService::class.java)
            val call = retrofitApiService.searchPlace(
                searchQuery,
                myLocation?.longitude.toString(),
                myLocation?.latitude.toString(),
                pgIndex1
            )
            call.enqueue(object : Callback<KakaoSearchPlaceResponse> {
                override fun onResponse(
                    call: Call<KakaoSearchPlaceResponse>,
                    response: Response<KakaoSearchPlaceResponse>
                ) {
                    //응답받은 json 을 파싱한 객체를 참조하기
                    searchPlaceResponse = response.body()
                   G.documents?.addAll(searchPlaceResponse?.documents as MutableList<Place> )
//                    Toast.makeText(this@MainActivity, "${searchPlaceResponse!!.meta.pageable_count}", Toast.LENGTH_SHORT).show()

                    //먼저 데이터가 온전히 잘 왔는지 파악해보기
                    val meta: PlaceMeta? = searchPlaceResponse?.meta
                    val documents: MutableList<Place>? = searchPlaceResponse?.documents

//                AlertDialog.Builder(this@MainActivity).setMessage("${meta?.total_count}\n${documents?.get(0)?.place_name}").create().show()
                    // 무조건 검색이 완료되면 '리스트'형태로 먼저 보여주도록 할 것임
                    binding.bnv.selectedItemId = R.id.menu_bnv_list

                    // fab 버튼 원위치
                    ObjectAnimator.ofFloat(binding.fabRefresh, "translationY", 0f).start()
                    ObjectAnimator.ofFloat(binding.fabRefresh, "rotationX", 0f).start()

                }

                override fun onFailure(call: Call<KakaoSearchPlaceResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "서버 오류가 있습니다.", Toast.LENGTH_SHORT).show()
                }
            })


        }

    fun clickBtn() {
        // Android 13 버전부터 알림 사용 시 사용자 허가 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionResult = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
                return
            }

            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel("ch01", "MyChannel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(this, "ch01")

            // 알림 내용 설정
            builder.setSmallIcon(R.drawable.bg_choice)
            builder.setContentTitle("Ex40의 알림")
            builder.setContentText("알림 메세지를 이곳에 보여줍니다.")
            builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))

            // 알림 클릭 시 실행할 액티비티 설정
            val intent = Intent(this, SecondActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            builder.setContentIntent(pendingIntent)
            builder.setAutoCancel(true)

            // 알림창에 추가 액션 설정
            builder.addAction(R.drawable.bg_choice, "둘러보기", pendingIntent)
            builder.addAction(R.drawable.bg_choice, "옵션", pendingIntent)

            // 알림 스타일 설정
            val picStyle = NotificationCompat.BigPictureStyle(builder)
            picStyle.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.logo))

            val notification = builder.build()

            // 알림 발송
            notificationManager.notify(10, notification)
        }
    }


}//mainActivity