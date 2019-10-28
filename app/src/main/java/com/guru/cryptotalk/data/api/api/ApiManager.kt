package com.guru.cryptotalk.data.api.api

import android.util.Log
import com.guru.cryptotalk.App
import com.guru.cryptotalk.Constants
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.guru.cryptotalk.data.api.model.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager {
    companion object {
        private var instance: ApiManager? = null
        fun getInstance() = instance ?: ApiManager()
        val apiInterface = ApiService.getClient().create(ApiInterface::class.java)
    }

    fun getBalance(addr: String, apiResponseHandler: ApiResponseHandler) {
        val params = HashMap<String, Any>()
        params.put("env", "PROD")
        params.put("addr", addr)
        params.put("operation", "BALANCE")

        val map = HashMap<String, Any>()
        map.put("params", params);

        apiInterface.getBalance(params = map).enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                apiResponseHandler.onFailed(t.message.toString())
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (!response.body()!!.isError) {
                  apiResponseHandler.onComplete(response.body()!!)
                } else {
                    apiResponseHandler.onFailed(response.body()?.errorMsg!!)
                }
            }
        })


    }

    fun startTxn(req: Request, alertId: String, apiResponseHandler: ApiResponseHandler) {
        val params = HashMap<String, Any>()
        params.put("env", "PROD")
        params.put("operation", req.operation)
        params.put("from_tkn", req.from_tkn)
        params.put("to_tkn", req.to_tkn)
        params.put("token", req.from_tkn)
        params.put("amt_float", req.amt)
        params.put("addr", req.addr)
        params.put("dst_addr", req.dst_addr)
        params.put("private_key", SharedPrefrenceManager.getPrivateKey(App.instance))

        val map = HashMap<String, Any>()
        map.put("params", params)
        if (req.operation == Constants.OPERATION_SWAP) {
            apiInterface.startTxnSwap(params = map).enqueue(object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    apiResponseHandler.onFailed(t.message.toString())
                    FirebaseManager.getInstance()
                        .updateAlert(alertId, "status","error")
                }

                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (!response.body()!!.isError && !response.body()?.hash.isNullOrEmpty()) {
                        FirebaseManager.getInstance()
                            .updateAlert(alertId, "hash", response.body()!!.hash)
                    }
                }
            })
        } else if (req.operation == Constants.OPERATION_LEND) {
                apiInterface.startTxnLend(params = map).enqueue(object : Callback<ApiResponse> {
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        apiResponseHandler.onFailed(t.message.toString())
                        FirebaseManager.getInstance()
                            .updateAlert(alertId, "status","error")
                    }

                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (!response.body()!!.isError && !response.body()?.hash.isNullOrEmpty()) {
                            FirebaseManager.getInstance()
                                .updateAlert(alertId, "hash", response.body()!!.hash)
                        }
                    }
                })
        }
    }
}