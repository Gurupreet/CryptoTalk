package com.guru.cryptotalk.data.api.notifications

import android.util.Log
import com.guru.cryptotalk.data.api.api.ApiManager
import com.guru.cryptotalk.data.api.api.ApiResponseHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.model.Alert
import com.onesignal.OSNotification
import com.onesignal.OneSignal

class NotificationReceievedHandler: OneSignal.NotificationReceivedHandler  {

    override fun notificationReceived(notification: OSNotification?) {
        val data = notification?.payload?.additionalData
        val alertId = data?.getString("alertID") ?: ""
        if (!alertId.isNullOrEmpty()) {
            FirebaseManager.getInstance().getAlert(alertId, object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    val alert = result as Alert
                    ApiManager.getInstance().startTxn(alert.request, alertId, object : ApiResponseHandler {
                        override fun onComplete(data: Any) {

                        }

                        override fun onFailed(data: Any) {

                        }

                    })
                }

                override fun onFailure(result: Any) {
                }

            })

        }
    }

}