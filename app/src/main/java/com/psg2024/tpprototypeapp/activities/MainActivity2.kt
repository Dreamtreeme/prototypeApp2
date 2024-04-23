package com.psg2024.tpprototypeapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.psg2024.tpprototypeapp.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {

    val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        clickBtn()

        binding.toolbar.setNavigationOnClickListener { exitbutton() }
        binding.mkroom.setOnClickListener { startActivity(Intent(this, MakeRoomActivity::class.java)) }
        binding.addfriend.setOnClickListener { startActivity(Intent(this, AddFriendActivity::class.java)) }
    }

    private fun exitbutton() {
        AlertDialog.Builder(this).setMessage("앱을 종료하시겠습니까?").setNegativeButton("취소",{p0,p1 -> }).setPositiveButton("확인", {p0,p1 -> finish()}).show()
    }

    private fun clickBtn() {

        // Android 13 버전부터 알림 사용 시 사용자 허가 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionResult = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
                return
            }
        }
    }//clickbtn

}