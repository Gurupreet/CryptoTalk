package com.guru.cryptotalk.data.api.firebase.FirebaseSyncs

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseSync
import com.guru.cryptotalk.data.api.model.User

class UserFirebaseSync(val userId: String) : FirebaseSync() {
    init {
        queryRef = mDatabaseRef.child("users").child(userId)
    }

    fun startUserFirebaseSync(completionHandler: FirebaseResponseCompletionHandler) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                completionHandler.onSuccess(user!!, FirebaseObserverType.CHILD_ADDED)
            }

            override fun onCancelled(p0: DatabaseError) {
                completionHandler.onFailure("")
            }
        }

        queryRef.addValueEventListener(valueEventListener)
        mValueEventListener = valueEventListener
    }
}
