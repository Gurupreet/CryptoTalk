package com.guru.cryptotalk.data.api

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SharedPrefrenceManager {
    private val sharedPrefKey =  "SHARED_PREF"
    private val PRIVATE_KEY = "private_key"
    private val WALLET_FILE_PATH = "wallet_file_path"

    val keyData : MutableLiveData<String> = MutableLiveData()

    fun savePrivateKey(context: Context, key: String) {
        val sharedPreferences = context.getSharedPreferences(sharedPrefKey,  Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(PRIVATE_KEY, key).apply()
        if (keyData.value != null && keyData.value == key) {
            //don't update for same key
        } else {
            keyData.value = key
        }
    }

    fun saveWalletFileName(context: Context, path: String) {
        val sharedPreferences = context.getSharedPreferences(sharedPrefKey,  Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(WALLET_FILE_PATH, path).apply()
    }

    fun getWalletFileName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(sharedPrefKey,  Context.MODE_PRIVATE)
        return sharedPreferences.getString(WALLET_FILE_PATH, "");
    }

    fun getPrivateKey(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(sharedPrefKey,  Context.MODE_PRIVATE)
        return sharedPreferences.getString(PRIVATE_KEY, "");
    }
}