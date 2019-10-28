package com.guru.cryptotalk.data.api.model

class User (var user_id: String, var addr: String, var player_id: String, var email: String, var created_at: Long, var last_active: Long, var balance: ArrayList<String>) {
    constructor() : this("", "", "", "", System.currentTimeMillis(), System.currentTimeMillis(), ArrayList())
}