package com.psg2024.tpprototypeapp

import com.psg2024.tpprototypeapp.data.FriendRequestID

import com.psg2024.tpprototypeapp.data.KakaoSearchPlaceResponse
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.data.Test
import com.psg2024.tpprototypeapp.data.UserAccount
import com.psg2024.tpprototypeapp.data.UserFriend

class G{


    //동반 객체 - 자바의 static 멤버와 비슷한 기능
    companion object {

        var inviteList: MutableList<String> = mutableListOf()

        //로그인 계정 정보- [아이디, 이메일 정보 저장 객체]
        var userAccount: UserAccount?=null

        var userFriend: UserFriend?=null

        var documents: MutableList<Place>? = mutableListOf()

        var token : String? =null

        var FriendRequestList: MutableList<FriendRequestID>? = mutableListOf()

        var docmentsID: String? =null

        var collectionName: String? =null

        var pos : ArrayList<String> = arrayListOf()

        var friendList: MutableList<String> = mutableListOf()








    }
}