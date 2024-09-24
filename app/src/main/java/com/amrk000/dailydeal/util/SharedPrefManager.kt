package com.amrk000.dailydeal.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class SharedPrefManager private constructor (context: Context) {
    private var sharedPreferences: SharedPreferences

    // Keys
    private val SIGNED_IN = "signedIn"
    private val USER_ID = "userId"

    init {
        sharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    companion object {
        private lateinit var instance: SharedPrefManager

        fun get(context: Context): SharedPrefManager {
            if (!this::instance.isInitialized) instance = SharedPrefManager(context)
            return instance
        }
    }

    var userSignedIn: Boolean
        get() = sharedPreferences.getBoolean(SIGNED_IN, false)
        set(signedIn) {
            sharedPreferences.edit()
                .putBoolean(SIGNED_IN, signedIn)
                .apply()
        }

    var userId: Long
        get() = sharedPreferences.getLong(USER_ID, -1)
        set(userId) {
            sharedPreferences.edit()
                .putLong(USER_ID, userId)
                .apply()
        }

}