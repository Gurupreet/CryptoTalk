package com.guru.cryptotalk.data.api.firebase

import android.util.Log
import com.google.firebase.database.*

open class FirebaseSync {
    protected var mDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    protected var mChildEventListener: ChildEventListener? = null
    protected var mValueEventListener: ValueEventListener? = null
    protected lateinit var queryRef: Query


    fun isSnapshotExists(completionHandler: FirebaseResponseCompletionHandler) {

        queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    completionHandler.onFailure("Can't load data")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                completionHandler.onFailure("Can't load data")
            }
        })
    }

    fun keepSynced() {
        mDatabaseRef.keepSynced(true)
    }

    fun cleanupListener() {
        if (mChildEventListener != null) {
            Log.i("DB Ref", "Sync DB Ref : $mDatabaseRef")
            mDatabaseRef.removeEventListener(mChildEventListener!!)
            mChildEventListener = null
        }

        if (mValueEventListener != null) {
            mDatabaseRef.removeEventListener(mValueEventListener!!)
            mValueEventListener = null
        }

        mDatabaseRef.keepSynced(false)
    }

    fun queryOrderByKey(key: String, childRef: DatabaseReference): Query {
        queryRef = childRef.orderByChild(key)
        return queryRef
    }

    fun queryLimitedToFirst(limit: Int, key: String, childRef: DatabaseReference): Query {
        queryRef = queryOrderByKey(key, childRef).limitToFirst(limit)
        return queryRef
    }

    fun queryLimitedToLast(limit: Int, key: String, childRef: DatabaseReference): Query {
        queryRef = queryOrderByKey(key, childRef).limitToLast(limit)
        return queryRef
    }

    fun queryEndingAtValue(value: Double?, limit: Int, key: String, childRef: DatabaseReference): Query {
        queryRef = queryOrderByKey(key, childRef).endAt(value!!).limitToLast(limit)
        return queryRef
    }

    fun queryValueEqualto(value: Int, key: String, childRef: DatabaseReference): Query {
        queryRef = queryOrderByKey(key, childRef).equalTo(value.toDouble())
        return queryRef
    }

    fun queryValueEqualto(value: Boolean, key: String, childRef: DatabaseReference): Query {
        queryRef = queryOrderByKey(key, childRef).equalTo(value)
        return queryRef
    }

    fun queryOrderbyValueEqualTo(childRef: DatabaseReference, value: Boolean): Query {
        queryRef = childRef.equalTo(value)
        return queryRef
    }


    fun stopFirebaseSync() {
        if (mChildEventListener != null) {
            mDatabaseRef?.removeEventListener(mChildEventListener!!)
            mChildEventListener = null
        }
        if (mValueEventListener != null) {
            mDatabaseRef?.removeEventListener(mValueEventListener!!)
            mValueEventListener = null
        }
    }

}