package com.guru.cryptotalk.data.api.firebase

import android.util.Log
import com.google.firebase.database.*
import com.guru.cryptotalk.data.api.model.Alert

class FirebaseManager {

    companion object {
        private var databaseRef = FirebaseDatabase.getInstance().reference
        private var instance: FirebaseManager? = null
        fun getInstance() = instance ?: FirebaseManager()
        }

    fun addALert(alert: Alert) {
        if (FirebaseAuthManager.getInstance().getCurrentUserId().isNotEmpty()) {
            val ref = databaseRef.child("data/alerts").child(FirebaseAuthManager.getInstance().getCurrentUserId());
            val ref2 = databaseRef.child("alerts")
            val key = ref.push().key
            alert.key = key!!
            ref.child(key!!).setValue(
                alert
            ) { error, ref ->
                if (!error?.message.isNullOrEmpty()) {
                    Log.d("error %s", error?.message)
                }
            }
            ref2.child(key!!).setValue(
                alert
            ) { error, ref2 ->
                if (!error?.message.isNullOrEmpty()) {
                    Log.d("error %s", error?.message)
                }
            }
        }
    }

    fun updateAlert(alertId: String, key: String, value: Any) {
        val map : HashMap<String, Any> = HashMap()
        map.put(key, value)
        databaseRef.child("data/alerts").
            child(FirebaseAuthManager.getInstance().getCurrentUserId()).
            child(alertId)
            .updateChildren(map)
    }

    fun getAlert(alertId: String, firebaseResponseCompletionHandler: FirebaseResponseCompletionHandler) {
        databaseRef.child("alerts").child(alertId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                firebaseResponseCompletionHandler.onFailure("")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val alert = snapshot.getValue(Alert::class.java)
                firebaseResponseCompletionHandler.onSuccess(alert!!, FirebaseObserverType.CHILD_ADDED)
            }

        })
    }

    fun updateUser(key: String, value: Any) {
        if (FirebaseAuthManager.getInstance().getCurrentUserId().isNotEmpty()) {
            val update: HashMap<String, Any> = HashMap()
            update.put(key, value)
            databaseRef.child("users").child(FirebaseAuthManager.getInstance().getCurrentUserId())
                .updateChildren(update)
        }
    }
}