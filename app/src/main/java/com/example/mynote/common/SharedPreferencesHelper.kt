package com.example.mynote.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesHelper {

    companion object {

        private const val APP_SHARED_PREF = "App_SharedPref"
        private const val UPDATE_TIME = "update_time"

        private lateinit var sharedPrefs: SharedPreferences

        @Volatile
        private var INSTANCE: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildHelper(context)
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            sharedPrefs = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE)
            return SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        sharedPrefs.edit(commit = true) { putLong(UPDATE_TIME, time) }
    }

    fun getUpdateTime() = sharedPrefs.getLong(UPDATE_TIME, 0)

    fun getCacheDuration() = sharedPrefs.getString("cache_duration", "")
}