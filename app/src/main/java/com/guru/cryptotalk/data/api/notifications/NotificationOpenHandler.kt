package com.guru.cryptotalk.data.api.notifications

import android.content.Intent
import com.guru.cryptotalk.App
import com.guru.cryptotalk.ui.MainActivity
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal

class NotificationOpenHandler: OneSignal.NotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenResult?) {
        val actionType = result?.action?.type
        val data = result?.notification?.payload?.additionalData ?: null
        OneSignal.clearOneSignalNotifications()



            val intent = Intent(App.instance, MainActivity::class.java)
            intent.putExtra("EXTRA_LAUNCHED_BY_NOTIFICATION", true)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            App.instance.startActivity(intent)
            return

    }

}