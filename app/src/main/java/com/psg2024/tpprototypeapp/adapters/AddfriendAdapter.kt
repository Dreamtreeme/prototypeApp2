package com.psg2024.tpprototypeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.psg2024.tpprototypeapp.fragments.AddFriendFragment
import com.psg2024.tpprototypeapp.fragments.AddFriendFragment2

class AddfriendAdapter(private val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = arrayOf(
        AddFriendFragment(),
        AddFriendFragment2()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]


}
