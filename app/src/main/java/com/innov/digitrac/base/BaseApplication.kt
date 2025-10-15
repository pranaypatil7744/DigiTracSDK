package com.innov.digitrac.base

import android.app.Application

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        com.innov.digitrac.base.LibraryInitializer.init(this)
    }


}