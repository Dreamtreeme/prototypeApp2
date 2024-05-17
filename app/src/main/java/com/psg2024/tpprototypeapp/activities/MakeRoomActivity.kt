package com.psg2024.tpprototypeapp.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivityMakeRoomBinding
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

class MakeRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityMakeRoomBinding.inflate(layoutInflater) }
    var datetomili: Long = 0
    var timetomili: Long = 0
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnLocation.setOnClickListener { startActivity(Intent(this, LocationActivity::class.java)) }
        val s: String? = intent.getStringExtra("place")
        binding.receiveLocation.text= s
        binding.goSubMain.setOnClickListener {
            makeCollection()
            setLocationInFirestore()
            finish()
            //sharedPreferences에 G.collectionName 저장
            saveSharedPreferences(this, "collectionName", G.collectionName!!)

            startActivity(Intent(this, SubMainActivity::class.java))
            }









        // 선택된 날짜 처리


        binding.btnDateTime.setOnClickListener {
            // MaterialDatePicker 생성
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("날짜 선택")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, "date")
            datePicker.addOnPositiveButtonClickListener {
                datetomili = it
                Toast.makeText(this, "$datetomili", Toast.LENGTH_SHORT).show()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val dateString = sdf.format(Date(it))
                binding.viewDate.setText(dateString)

                // MaterialTimePicker 생성
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H) // 시간 형식 설정 (12시간 또는 24시간)
                    .setHour(12) // 초기 시간 설정 (0~23)
                    .setMinute(0) // 초기 분 설정 (0~59)
                    .setTitleText("시간 선택") // 다이얼로그 제목 설정
                    .build()

                timePicker.show(supportFragmentManager, "time")
                timePicker.addOnPositiveButtonClickListener { it ->
                    // 시간 선택 완료시 선택한 시간 밀리초로받기
                    val t = timePicker.hour.toLong()*60*60*1000
                    Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
                    val t2 = timePicker.minute.toLong()*60*1000
                    Toast.makeText(this, "$t2", Toast.LENGTH_SHORT).show()
                    timetomili= t+t2
                    Toast.makeText(this, "$timetomili", Toast.LENGTH_SHORT).show()
                    val period = if (timePicker.hour < 12) "오전" else "오후"
                    val hour = if (timePicker.hour == 0 || timePicker.hour == 12) 12 else timePicker.hour % 12
                    binding.viewTime.text = "$period ${hour}시 ${timePicker.minute}분"
                }
            }
        }// 날짜 선택 끝났을때
    }

    private fun makeCollection() {
        val s =binding.receiveLocation.text.toString()
        val apointmentTime = (timetomili+datetomili).toString()
        G.collectionName = G.userAccount?.ID+","+s+","+apointmentTime
        Toast.makeText(this, "${G.collectionName}", Toast.LENGTH_SHORT).show()

    }

    private fun setLocationInFirestore() {
        val db = Firebase.firestore.collection(G.collectionName!!)
        val userInformation: MutableMap<String, Any> = mutableMapOf()
        userInformation["ID"] = G.userAccount!!.ID
        userInformation["Lat"] =0
        userInformation["Long"] =0
        userInformation["LoLat"] = G.pos[0]
        userInformation["LoLong"] = G.pos[1]
        db.document(G.userAccount!!.ID).set(userInformation).addOnSuccessListener {
            Toast.makeText(this, "위치가 등록되었습니다", Toast.LENGTH_SHORT).show()

        }


    }

    fun saveSharedPreferences(context: Context, key: String, value: Any) {
        val sharedPreferences = context.getSharedPreferences("YOUR_APP_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass}")
        }

        editor.apply() // 비동기적으로 저장
        // editor.commit() // 동기적으로 저장 (더 느릴 수 있지만, 데이터 손실 가능성이 낮음)
    }


}