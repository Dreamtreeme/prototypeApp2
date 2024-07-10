package com.psg2024.tpprototypeapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
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
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceMapBinding

class SubMapFragment : Fragment() {




    private val binding : FragmentPlaceMapBinding by lazy { FragmentPlaceMapBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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


            val latitude: Double =
                G.pos[0].toDouble()
            val longitude: Double = G.pos[1].toDouble()
            val thatPos: LatLng = LatLng.from(latitude, longitude)



            val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(thatPos, 16)
            kakaoMap.moveCamera(cameraUpdate)


            val labelOptions: LabelOptions =
                LabelOptions.from(thatPos).setStyles(R.drawable.ic_mypin).setTexts(//목적지 위치
                    "목적지",
                    "${G.collectionName!!.split(",")[1]}"
                )

            val labelLayer: LabelLayer = kakaoMap.labelManager!!.layer!!
            labelLayer.addLabel(labelOptions)


            db.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // 오류 처리
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty)
                    snapshot.forEach {

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

                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16, 16, 16, 16)
                    layout.setBackground(R.drawable.base_msg, true)

                    texts.forEach {
                        val guiText = GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.WHITE)
                        layout.addView(guiText)
                    }//forEach

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