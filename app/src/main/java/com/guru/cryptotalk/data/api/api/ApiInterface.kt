package com.guru.cryptotalk.data.api.api

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @POST("balance")
    fun getBalance(@Body params: HashMap<String, Any>): Call<ApiResponse>

    @POST("kyber")
    fun startTxnSwap(@Body params: HashMap<String, Any>): Call<ApiResponse>

    @POST("compound")
    fun startTxnLend(@Body params: HashMap<String, Any>): Call<ApiResponse>

}