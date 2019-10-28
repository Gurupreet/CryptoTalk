package com.guru.cryptotalk.data.api.model

data class Request(var addr: String,var user_id: String, var amt: Double, var env: String, var from_tkn: String, var to_tkn: String, var operation: String, var dst_addr: String) {
    constructor() : this("","", 0.0,
        "PROD", "", "", "", "ignore")
}