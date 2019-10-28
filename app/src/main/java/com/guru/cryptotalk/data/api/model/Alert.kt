package com.guru.cryptotalk.data.api.model

data class Alert(var key: String, var addr: String, var ticker: String, var status: String, var operation: String, var price: Double, var time_in_seconds: Int, var percentage_change: Double, var created_at: Long,  var request: Request, var player_ids: ArrayList<String>, var hash: String ) {
    constructor() : this("","", "", "pending", "",
        0.0, 300, 0.0, System.currentTimeMillis(), Request(), ArrayList(), "")
}