package com.psg2024.tpprototypeapp.activities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.G.Companion.FriendRequestList
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.adapters.AddfriendAdapter
import com.psg2024.tpprototypeapp.data.FriendRequestID
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
        val db = FirebaseFirestore.getInstance()

// 문서 참조 가져오기
        val docRef = db.collection("friendUsers")


        val test =docRef.whereEqualTo("FriendId", G.userAccount!!.ID).whereEqualTo("accept","false").get()

        test.addOnSuccessListener{

            if (it.documents.size > 0) {

                it.documents.forEach { documentSnapshot ->
                    val FIDstring: String =documentSnapshot.get("ID") as String
                    val FID: FriendRequestID = FriendRequestID(FIDstring)
                    FriendRequestList!!.add(FID)
                    G.docmentsID = documentSnapshot.id}

            }




//            val builder = NotificationCompat.Builder(this, "ch01")
//            builder.setSmallIcon(R.drawable.bg_choice)
//            builder.setContentTitle("친구 요청 알림")
//            builder.setContentText("친구 요청이 왔습니다. 확인해보세요!!.")
//            builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
//
//            //알림 클릭 시 실행할 액티비티 설정
//            val intent = Intent(this, AddFriendActivity::class.java)
//            val pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//            builder.setContentIntent(pendingIntent)
//            builder.setAutoCancel(true)
//
//            val notification = builder.build()
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.notify(10, notification)
        }


    }
}