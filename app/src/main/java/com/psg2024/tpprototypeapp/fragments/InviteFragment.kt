package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.psg2024.tpprototypeapp.databinding.FragmentInviteFriendBinding
import com.psg2024.tpprototypeapp.databinding.FragmentListBinding

class InviteFragment : Fragment() {

    private lateinit var binding : FragmentInviteFriendBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentInviteFriendBinding.inflate(inflater, container, false)
        return binding.root
    }


}
