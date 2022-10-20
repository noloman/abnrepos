package com.nulltwenty.abnrepos

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AbnReposApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDialog().build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder().detectAll().penaltyLog().build()
        )
    }
}