package com.psg2024.tpprototypeapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.adapters.AddfriendAdapter
import com.psg2024.tpprototypeapp.databinding.ActivityAddFriendBinding
import com.psg2024.tpprototypeapp.network.MyFirebaseMessagingService

class AddFriendActivity : AppCompatActivity() {

    val binding by lazy { ActivityAddFriendBinding.inflate(layoutInflater) }

    private lateinit var pager2: ViewPager2
    private lateinit var adapter: AddfriendAdapter
    private lateinit var tablayout: TabLayout
    private val tabTitle = arrayOf("추가", "친구 요청")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pager2 = binding.pager
        adapter=AddfriendAdapter(this)
        pager2.adapter= adapter

        tablayout = binding.tabLayout

        TabLayoutMediator(tablayout, pager2){tab, position ->
            tab.text = tabTitle[position]}.attach()

        binding.toolbar.setNavigationOnClickListener { finish() }


    }
}