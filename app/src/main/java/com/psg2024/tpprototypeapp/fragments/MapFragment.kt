package com.psg2024.tpprototypeapp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
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
import com.psg2024.tpprototypeapp.activities.LocationActivity
import com.psg2024.tpprototypeapp.activities.MainActivity
import com.psg2024.tpprototypeapp.activities.MakeRoomActivity
import com.psg2024.tpprototypeapp.activities.PlaceDetailActivity
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.databinding.FragmentMapBinding

class MapFragment : Fragment() {
   private val binding by lazy { FragmentMapBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment에서 Bundle 객체 가져오기

        // Bundle 객체에서 LatLng 객체 추출
        val myPos = arguments?.getParcelable<LatLng>("pos")
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //카카오 지도 start
        binding.mapView.start(mapReadyCallback)
        // 지도에 좌표를 찍으면 서버에 등록, 내 좌표 얻어오는 코드-> 얻어온 좌표 아이디와 함께 보내기
        // 그리고 서버에서 좌표값 가져와서 지도에 계속 그리기
        // 친구초대를 한 경우 수락을 누른 순간 방에 들어와짐. 방에 들어오면 위의 코드가 발동하면서 같은 컬렉션에 저장됨
        // 그렇다면 방 만들기를 눌렀을때 컬렉션 이름을 고유의 값으로 정해야함. 자기아이디+ 모임장소+날짜시간 조합해서 만들면됨
        // 초대를 보냈을때 확인을 누르면 이 방으로 오게됨. 오게된 순간 같은 컬렉션에 저장. 즉 방만들기로 들가면 유일한 컬렉션이 생기는거고
        // 일단 방만들때 서버에 컬렉션 만드는거나하자



    }

    private val mapReadyCallback : KakaoMapReadyCallback = object : KakaoMapReadyCallback(){
        override fun onMapReady(kakaoMap: KakaoMap) {


            //현재 내 위치를 지도의 중심위치로 설정
            val latitude: Double= G.documents!!.get(0).y.toDouble() ?: 37.5666 //비동기는 null이 올수 있으니 non nullable 쓰는걸 비추
            val longitude: Double = G.documents!!.get(0).x.toDouble() ?: 126.9782
            val myPos : LatLng = LatLng.from(latitude, longitude)


            // 내위치를 얻어왔으면 지도 카메라를 이동
            val cameraUpdate : CameraUpdate = CameraUpdateFactory.newCenterPosition(myPos, 16)
            kakaoMap.moveCamera(cameraUpdate)


            // 내 위치에 대한 마커를 추가하기
            val labelOptions : LabelOptions = LabelOptions.from(myPos).setStyles(R.drawable.ic_mypin) //백터 그래픽 이미지는 안됨 png로 써야함
            // 라벨이 그려질 레이어 객체 소환
            val labelLayer: LabelLayer = kakaoMap.labelManager!!.layer!!
            labelLayer.addLabel(labelOptions)


            val placeList :List<Place>? = (activity as LocationActivity).searchPlaceResponse?.documents
            placeList?.forEach{

                val pos = LatLng.from(it.y.toDouble(), it.x.toDouble())
                val options = LabelOptions.from(pos).setStyles(R.drawable.ic_pin).setTexts(it.place_name).setTag(it)
                kakaoMap.labelManager!!.layer!!.addLabel(options)

            }//forEach

            // 라벨 클릭에 반응하기
            kakaoMap.setOnLabelClickListener { kakaoMap, layer, label ->

                label.apply {
                    //정보창 [infowindow] 보여주기
                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16,16,16,16)
                    layout.setBackground(R.drawable.base_msg,true)

                    texts.forEach {
                        val guiText= GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.WHITE)
                        layout.addView(guiText)
                    }//forEach
                    // [정보창 info window] 객체 만들기
                    val options: InfoWindowOptions = InfoWindowOptions.from(position)
                    options.body= layout
                    options.setBodyOffset(0f, -10f)
                    options.setTag(tag)

                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)

                }//apply..


            }//label click

            //[정보창 클릭에 반응하기
            kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->
                //장소에 대한 상세 소개 웹페이지를 보여주는 화면으로 이동
                val intent = Intent(requireContext(), MakeRoomActivity::class.java)

                // 클릭한 장소에 대한 정보를 json문자열로 변환하여 전달해주기
                val place=infoWindow.tag as Place
                Toast.makeText(requireContext(), "${place.place_name}", Toast.LENGTH_SHORT).show()
                val locationName : String = place.place_name
                intent.putExtra("place",locationName)
                G.pos.add(place.y)
                G.pos.add(place.x)


//                val json:String= Gson().toJson(place)
//                intent.putExtra("place", json)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }



    }

}