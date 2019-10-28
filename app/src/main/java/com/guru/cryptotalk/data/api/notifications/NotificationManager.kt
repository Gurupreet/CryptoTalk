package com.guru.cryptotalk.data.api.notifications

import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.onesignal.OneSignal

class NotificationManager {
    companion object {
        private var instance: NotificationManager? = null
        fun getInstance() = instance ?: NotificationManager()
    }

    fun registerForNotification() {
        OneSignal.idsAvailable { playerId, registrationId ->
            FirebaseManager.getInstance().updateUser("player_id", playerId)
        }
    }

}