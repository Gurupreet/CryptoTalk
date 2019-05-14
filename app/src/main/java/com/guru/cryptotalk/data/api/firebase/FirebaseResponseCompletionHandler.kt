package com.guru.cryptotalk.data.api.firebase

interface FirebaseResponseCompletionHandler {

    fun onSuccess(result: Any, observerType: FirebaseObserverType)
    fun onFailure(result: Any)
}