package com.kotlingithub.utilities

import android.annotation.SuppressLint
import android.content.Context
import java.io.File

object MySharedPreference {

    fun setPreference(context: Context, prefName: String, prefKey: String, prefVal: Any) {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        when (prefVal) {
            is String -> editor.putString(prefKey, prefVal.toString())
            is Int -> editor.putInt(prefKey, prefVal)
            is Float -> editor.putFloat(prefKey, prefVal)
            is Long -> editor.putLong(prefKey, prefVal)
            is Boolean -> editor.putBoolean(prefKey, prefVal)
        }
        editor.apply()
    }

    fun getPreference(context: Context, prefName: String, prefKey: String, defaultVal: Any): Any? {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return when (defaultVal) {
            is String -> sharedPreferences.getString(prefKey, defaultVal.toString())
            is Int -> sharedPreferences.getInt(prefKey, defaultVal)
            is Long -> sharedPreferences.getLong(prefKey, defaultVal)
            is Float -> sharedPreferences.getFloat(prefKey, defaultVal)
            else -> sharedPreferences.getBoolean(prefKey, defaultVal as Boolean)
        }
    }

    fun clearPreference(context: Context, prefName: String) {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun deletePreference(context: Context, _preferenceName: String) {
        context.getSharedPreferences(_preferenceName, 0).edit().clear().apply()
        val xmlFile = File(getPreferencePrefix(context) + _preferenceName + ".xml")
        if (xmlFile.exists()) xmlFile.delete()

        val backFile = File(getPreferencePrefix(context) + _preferenceName + ".bak")
        if (backFile.exists()) backFile.delete()
    }

    @SuppressLint("SdCardPath")
    private fun getPreferencePrefix(context: Context): String {
        return "/data/data/" + context.packageName + "/shared_prefs/"
    }
}