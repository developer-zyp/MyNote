package com.example.mynote

import android.app.Application
import android.content.Context

class AppInstance : Application() {
    companion object {
        val TAG = AppInstance::class.java.simpleName
        private lateinit var instance: AppInstance

        fun getInstance(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}