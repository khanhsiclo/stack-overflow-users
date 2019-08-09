package com.martin.stackusers.application

import android.app.Application
import com.martin.stackusers.injection.AppComponent
import com.martin.stackusers.injection.DaggerAppComponent
import com.martin.stackusers.injection.modules.AppModule

class MainApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    @Suppress("DEPRECATION")
    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}