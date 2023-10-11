package com.example.pokemonkotlindemo.base

import android.app.Application
import com.example.pokemonkotlindemo.BuildConfig
import com.example.pokemonkotlindemo.base.network.request.ApiManager
import com.example.pokemonkotlindemo.base.network.request.AppConfig

class MyApplication : Application() {

    companion object {
        lateinit var context : Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        AppConfig.init(BuildConfig.BUILD_TYPE)
        ApiManager.getInstance().init(this)
    }
}