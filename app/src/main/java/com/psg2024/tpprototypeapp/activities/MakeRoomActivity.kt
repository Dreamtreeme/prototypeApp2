package com.psg2024.tpprototypeapp.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivityMakeRoomBinding
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date

class MakeRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityMakeRoomBinding.inflate(layoutInflater) }
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }





        // 선택된 날짜 처리


        binding.btnDateTime.setOnClickListener {
            // MaterialDatePicker 생성
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("날짜 선택")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, "date")
            datePicker.addOnPositiveButtonClickListener {
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
                    val period = if (timePicker.hour < 12) "오전" else "오후"
                    val hour = if (timePicker.hour == 0 || timePicker.hour == 12) 12 else timePicker.hour % 12
                    binding.viewTime.text = "$period ${hour}시 ${timePicker.minute}분"
                }
            }
        }// 날짜 선택 끝났을때
    }
}