package com.guru.cryptotalk.data.api.api

interface ApiResponseHandler {
    fun onComplete(data: Any)
    fun onFailed(data: Any)
}