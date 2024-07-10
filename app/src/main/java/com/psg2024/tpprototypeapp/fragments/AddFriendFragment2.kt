package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.psg2024.tpprototypeapp.G.Companion.FriendRequestList
import com.psg2024.tpprototypeapp.adapters.RequestFriendAdapter
import com.psg2024.tpprototypeapp.databinding.FragmentAddFriend2Binding

class AddFriendFragment2 : Fragment() {

    private lateinit var binding : FragmentAddFriend2Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFriend2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FriendRequestList ?: return


        binding.recyclerView.adapter = RequestFriendAdapter(requireContext(), FriendRequestList!! )
        binding.recyclerView.adapter!!.notifyDataSetChanged()




        }





    }




