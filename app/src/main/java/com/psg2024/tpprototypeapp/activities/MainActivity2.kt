package com.psg2024.tpprototypeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivityMain2Binding
import com.psg2024.tpprototypeapp.databinding.ActivityMakeRoomBinding

class MainActivity2 : AppCompatActivity() {

    val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnNoty.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        binding.toolbar.setNavigationOnClickListener { exitbutton() }
        binding.mkroom.setOnClickListener { startActivity(Intent(this, MakeRoomActivity::class.java)) }
        binding.addfriend.setOnClickListener { startActivity(Intent(this, AddFriendActivity::class.java)) }
    }

    private fun exitbutton() {
        AlertDialog.Builder(this).setMessage("앱을 종료하시겠습니까?").setNegativeButton("취소",{p0,p1 -> }).setPositiveButton("확인", {p0,p1 -> finish()}).show()
    }
}