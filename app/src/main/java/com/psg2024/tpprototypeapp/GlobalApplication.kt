package com.psg2024.tpprototypeapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화작업
        KakaoSdk.init(this, "d6b81d530b8befbab4521cd32233df21")
    }
}