package com.psg2024.tpprototypeapp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.activities.LocationActivity
import com.psg2024.tpprototypeapp.activities.MakeRoomActivity
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.databinding.FragmentMapBinding

class MapFragment : Fragment() {
   private val binding by lazy { FragmentMapBinding.inflate(layoutInflater) }


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
        binding.mapView.start(mapReadyCallback)
        // 지도에 좌표를 찍으면 서버에 등록, 내 좌표 얻어오는 코드-> 얻어온 좌표 아이디와 함께 보내기
        // 그리고 서버에서 좌표값 가져와서 지도에 계속 그리기
        // 친구초대를 한 경우 수락을 누른 순간 방에 들어와짐. 방에 들어오면 위의 코드가 발동하면서 같은 컬렉션에 저장됨
        // 그렇다면 방 만들기를 눌렀을때 컬렉션 이름을 고유의 값으로 정해야함. 자기아이디+ 모임장소+날짜시간 조합해서 만들면됨
        // 초대를 보냈을때 확인을 누르면 이 방으로 오게됨. 오게된 순간 같은 컬렉션에 저장.



    }

    private val mapReadyCallback : KakaoMapReadyCallback = object : KakaoMapReadyCallback(){
        override fun onMapReady(kakaoMap: KakaoMap) {


            G.documents ?: return

            val latitude: Double=
                G.documents!!.get(0).y.toDouble() //비동기는 null이 올수 있으니 non nullable 쓰는걸 비추
            val longitude: Double = G.documents!!.get(0).x.toDouble()
            val myPos : LatLng = LatLng.from(latitude, longitude)



            val cameraUpdate : CameraUpdate = CameraUpdateFactory.newCenterPosition(myPos, 16)
            kakaoMap.moveCamera(cameraUpdate)




            val placeList :List<Place>? = (activity as LocationActivity).searchPlaceResponse?.documents
            placeList?.forEach{

                val pos = LatLng.from(it.y.toDouble(), it.x.toDouble())
                val options = LabelOptions.from(pos).setStyles(R.drawable.ic_pin).setTexts(it.place_name).setTag(it)
                kakaoMap.labelManager!!.layer!!.addLabel(options)

            }


            kakaoMap.setOnLabelClickListener { kakaoMap, layer, label ->

                label.apply {

                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16,16,16,16)
                    layout.setBackground(R.drawable.base_msg,true)

                    texts.forEach {
                        val guiText= GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.WHITE)
                        layout.addView(guiText)
                    }

                    val options: InfoWindowOptions = InfoWindowOptions.from(position)
                    options.body= layout
                    options.setBodyOffset(0f, -10f)
                    options.setTag(tag)

                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)

                }//apply..


            }//label click


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





                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }



    }

}