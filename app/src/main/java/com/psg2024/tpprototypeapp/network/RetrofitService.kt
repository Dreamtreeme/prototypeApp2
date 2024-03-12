package com.psg2024.tpprototypeapp.network

import com.psg2024.tpprototypeapp.data.KakaoSearchPlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    // 카카오 로컬 검색 api...요청해주는 코드 만들어줘 우선 응답 타입: 스트링
    @Headers("Authorization: KakaoAK c1f9c0a7641d9c43ec5ab1dcda96744e")
    @GET("/v2/local/search/keyword.json")
    fun searchPlaceToString(@Query("query") query:String,@Query("x") longitude:String,@Query("y") latitude:String) :Call<String>


    // 응답 타입을 서치플레이스레스폰스로
    @Headers("Authorization: KakaoAK c1f9c0a7641d9c43ec5ab1dcda96744e")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun searchPlace(@Query("query") query:String,@Query("x") longitude:String,@Query("y") latitude:String, @Query("page") page:Int) :Call<KakaoSearchPlaceResponse>

    // 네이버 api
    @GET("/v1/nid/me")
    fun getNidUserInfo(@Header("Authorization:") authoriztion: String) : Call<String>
}