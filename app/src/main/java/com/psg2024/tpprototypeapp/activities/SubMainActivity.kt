package com.psg2024.tpprototypeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.databinding.ActivitySubMainBinding
import com.psg2024.tpprototypeapp.fragments.MapFragment
import com.psg2024.tpprototypeapp.fragments.PlaceListFragment
import com.psg2024.tpprototypeapp.fragments.PlaceMapFragment

class SubMainActivity : AppCompatActivity() {
    val binding by lazy{ActivitySubMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //처음 보여질 프레그먼트를 화면에 붙이기
        supportFragmentManager.beginTransaction().add(R.id.container_fragment3, MapFragment())
            .commit()

        //bnv의 선택에 따라 Fragment를 동적으로 교체.

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bnv_map -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, MapFragment()).commit()

                R.id.menu_bnv_addfriend -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, PlaceMapFragment()).commit()

                R.id.menu_bnv_list -> supportFragmentManager.beginTransaction()
                    .replace(R.id.container_fragment3, PlaceListFragment()).commit()



            }

            true// OnItemSelectedListener의 추상메소드는 리턴값을 가지고 있음. SAM변환을 하면 return키워드를 사용하면 안됨.
        }
    }
}