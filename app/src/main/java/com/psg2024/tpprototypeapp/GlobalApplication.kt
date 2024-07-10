package com.psg2024.tpprototypeapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()


        KakaoSdk.init(this, "d6b81d530b8befbab4521cd32233df21")
    }
}