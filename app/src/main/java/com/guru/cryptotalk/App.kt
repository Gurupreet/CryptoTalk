package com.guru.cryptotalk

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import com.guru.cryptotalk.data.api.firebase.FirebaseAuthManager
import com.guru.cryptotalk.data.api.notifications.NotificationOpenHandler
import com.guru.cryptotalk.data.api.notifications.NotificationReceievedHandler
import org.koin.android.ext.android.startKoin
import com.guru.cryptotalk.di.*
import com.onesignal.OneSignal
import org.web3j.crypto.Credentials
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class App: Application() {

    companion object {
        lateinit var instance: App
        var cred: Credentials? = null
        var nfc = NumberFormat.getCurrencyInstance(Locale("en", "US"))
        var nf = NumberFormat.getNumberInstance(Locale("en", "US"))
        var df = DecimalFormat("##.##")
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin(this, listOf(AppModule))
        loadCredentials()
        initFirebase()
        initOneSingal()
        FirebaseAuthManager.getInstance().fetchOrCreateUser()

    }

    private fun initFirebase() {
        //firebase init
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    private fun initOneSingal() {
        //onesignal init
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.startInit(this)
            .setNotificationOpenedHandler(NotificationOpenHandler())
            .setNotificationReceivedHandler(NotificationReceievedHandler())
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .init()
        OneSignal.clearOneSignalNotifications()
    }



    private fun loadCredentials() {
        if (!SharedPrefrenceManager.getPrivateKey(this).isNullOrEmpty()) {
            cred = Credentials.create(SharedPrefrenceManager.getPrivateKey(this))
        }
    }
}