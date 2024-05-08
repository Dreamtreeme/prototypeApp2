package com.psg2024.tpprototypeapp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.activities.MainActivity
import com.psg2024.tpprototypeapp.activities.PlaceDetailActivity
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceMapBinding
import java.util.Timer
import java.util.TimerTask

class SubMapFragment : Fragment() {




    private val binding : FragmentPlaceMapBinding by lazy { FragmentPlaceMapBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //카카오 지도 start
        Log.d("위도경도", "${G.pos[0].toDouble()} ${G.pos[1].toDouble()}")
        binding.mapView2.start(mapReadyCallback)


    }
    val db = Firebase.firestore.collection(G.collectionName!!)








    private val mapReadyCallback : KakaoMapReadyCallback = object : KakaoMapReadyCallback(){
        override fun onMapReady(kakaoMap: KakaoMap) {

            //목적지를 지도 중심으로 설정
            val latitude: Double =
                G.pos[0].toDouble() ?: 37.5666 //비동기는 null이 올수 있으니 non nullable 쓰는걸 비추
            val longitude: Double = G.pos[1].toDouble() ?: 126.9782
            val thatPos: LatLng = LatLng.from(latitude, longitude)


            // 내위치를 얻어왔으면 지도 카메라를 이동
            val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(thatPos, 16)
            kakaoMap.moveCamera(cameraUpdate)

            // 내 위치에 대한 마커를 추가하기
            val labelOptions: LabelOptions =
                LabelOptions.from(thatPos).setStyles(R.drawable.ic_mypin).setTexts(//목적지 위치
                    "목적지",
                    "${G.collectionName!!.split(",")[1]}"
                )
            // 라벨이 그려질 레이어 객체 소환
            val labelLayer: LabelLayer = kakaoMap.labelManager!!.layer!!
            labelLayer.addLabel(labelOptions)

            // Firestore에서 필드값 변경 감지
            db.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // 오류 처리
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty)
                    snapshot.forEach {
                        // 서버의 좌표 가져와서 지도에 보여주는 로직 추가
                        val latitude3  = it.get("Lat").toString().toDouble()
                        val longitude3 = it.get("Long").toString().toDouble()
                        val newPos2= LatLng.from(latitude3, longitude3)
                        val options3 = LabelOptions.from(newPos2).setStyles(R.drawable.ic_pin)
                            .setTexts(it.id, "${it.id}님의 위치입니다.").setTag(it)
                        kakaoMap.labelManager!!.layer!!.addLabel(options3)
                    }

            }
            // 라벨 클릭에 반응하기
            kakaoMap.setOnLabelClickListener { kakaoMap, layer, label ->

                label.apply {
                    //정보창 [infowindow] 보여주기
                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16, 16, 16, 16)
                    layout.setBackground(R.drawable.base_msg, true)

                    texts.forEach {
                        val guiText = GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.WHITE)
                        layout.addView(guiText)
                    }//forEach
                    // [정보창 info window] 객체 만들기
                    val options: InfoWindowOptions = InfoWindowOptions.from(position)
                    options.body = layout
                    options.setBodyOffset(0f, -10f)
                    options.setTag(tag)

                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)

                }//apply..
            }


        }
    }
}