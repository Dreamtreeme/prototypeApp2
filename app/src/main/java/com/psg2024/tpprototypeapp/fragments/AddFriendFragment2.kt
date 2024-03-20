package com.psg2024.tpprototypeapp.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.G.Companion.FriendRequestList
import com.psg2024.tpprototypeapp.R
import com.psg2024.tpprototypeapp.activities.AddFriendActivity
import com.psg2024.tpprototypeapp.activities.RequestFriendActivity
import com.psg2024.tpprototypeapp.adapters.RequestFriendAdapter
import com.psg2024.tpprototypeapp.databinding.FragmentAddFriend2Binding
import com.psg2024.tpprototypeapp.databinding.RecyclerItemFriendRequestBinding
import okhttp3.internal.notify


class AddFriendFragment2 : Fragment() {

    private lateinit var binding : FragmentAddFriend2Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        binding = FragmentAddFriend2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("scroll_position", binding.recyclerView.layoutManager?.onSaveInstanceState())
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        G.FriendRequestList ?: return


        binding.recyclerView.adapter = RequestFriendAdapter(requireContext(), FriendRequestList!! )




        }





    }




