package com.guru.cryptotalk.data.api.firebase.FirebaseSyncs

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseSync
import com.guru.cryptotalk.data.api.model.Crypto

class CryptoListFirebaseSync(type: String) : FirebaseSync() {
    init {
        queryRef = mDatabaseRef.child("symbols").child(type).orderByChild("volume_traded")
    }
    fun startCryptoFirebbaseSync(completionHandler: FirebaseResponseCompletionHandler) {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                try {
                    val crypto: Any? = snapshot?.getValue(Crypto::class.java)
                    completionHandler.onSuccess(crypto!!, FirebaseObserverType.CHILD_ADDED)
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                try {
                    val crypto: Any? = snapshot?.getValue(Crypto::class.java)
                    completionHandler.onSuccess(crypto!!,FirebaseObserverType.CHILD_CHANGED)
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                try {
                    val crypto: Any? = snapshot?.getValue(Crypto::class.java)
                    completionHandler.onSuccess(crypto!! ,FirebaseObserverType.CHILD_REMOVED)
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
        }

        queryRef.addChildEventListener(childEventListener)
        mChildEventListener = childEventListener
    }
}