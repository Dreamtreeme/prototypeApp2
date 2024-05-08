package com.psg2024.tpprototypeapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.psg2024.tpprototypeapp.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        //1.5초 후에 로그인화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
          startActivity(Intent(this, LoginActivity::class.java))
          finish()
        },1500)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("ch01", "MyChannel", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

    }
}