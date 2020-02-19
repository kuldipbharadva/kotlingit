package com.kotlingithub

import android.app.Application
import com.facebook.FacebookSdk
import com.kotlingithub.dbRealm.RealmHelper

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RealmHelper.getRealmInstance(applicationContext)
        FacebookSdk.sdkInitialize(this)
    }
}