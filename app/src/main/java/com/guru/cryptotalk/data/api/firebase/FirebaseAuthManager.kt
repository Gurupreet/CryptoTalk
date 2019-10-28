package com.guru.cryptotalk.data.api.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.guru.cryptotalk.data.api.model.User
import com.guru.cryptotalk.data.api.notifications.NotificationManager

class FirebaseAuthManager  {

    companion object {
        private var databaseRef = FirebaseDatabase.getInstance().reference
        private var firebaseAuth = FirebaseAuth.getInstance()
        private var instance: FirebaseAuthManager? = null
        private var currentUser: User? = null
        fun getInstance() = instance ?: FirebaseAuthManager()
    }

    fun fetchOrCreateUser() {
        if (firebaseAuth.currentUser != null) {
            fetchUser()
        } else {
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    createUser()
                }
            }
        }
    }

    fun createUser() {
        getCurrentUserId()?.let {
            val user = User()
            user.user_id = it
            databaseRef.child("users").child(it).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    fetchUser()
                }
            }
        }
    }

    fun fetchUser() {
        getCurrentUserId()?.let {
            databaseRef.child("users").child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    currentUser = user
                    NotificationManager.getInstance().registerForNotification()
                }

            })
        }
    }


    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    fun getUserPlayerId(): String {
        return  currentUser?.player_id ?: ""
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}