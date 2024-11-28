package com.example.chatappv3

import android.app.Application
import com.zegocloud.zimkit.services.ZIMKit
import com.zegocloud.zimkit.services.ZIMKitConfig

class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        myApp = this
        val zimKitConfig = ZIMKitConfig()
        ZIMKit.initWith(this,Constants.APP_ID,Constants.APP_SIGN,zimKitConfig)
        ZIMKit.initNotifications()
    }
    companion object {
        var myApp:MyApplication? = null

    }
}