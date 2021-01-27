package com.posytron.avacycmp.system

import android.content.Context
import android.content.SharedPreferences

class CMPSharedPreferencesWrapper(context: Context) {
    private val SHARED_PREFERENCES_NAME = "CMPAvacyPreferences"
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun saveString(key: String?, value: String?): String? {
        sharedPreferences!!.edit().putString(key, value).apply()
        return value
    }

    fun getString(key: String?): String? {
        return getString(key, "")
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences!!.getString(key, defaultValue)
    }

    fun remove(key: String?) {
        sharedPreferences!!.edit().remove(key).apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun getAll(): MutableMap<String, String>? {
        return sharedPreferences!!.all as MutableMap<String, String>?
    }

    fun removeAll() {
        val all = getAll()
        for ((key, _) in all!!) {
            remove(key)
        }
    }

}