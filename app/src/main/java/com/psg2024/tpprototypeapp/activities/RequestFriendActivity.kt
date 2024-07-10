package com.psg2024.tpprototypeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psg2024.tpprototypeapp.databinding.ActivityAddFriendBinding

class RequestFriendActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddFriendBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}