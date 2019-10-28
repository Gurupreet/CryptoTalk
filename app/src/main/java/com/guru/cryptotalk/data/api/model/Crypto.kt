package com.guru.cryptotalk.data.api.model

data class Crypto(
    var name: String?,
    var exchange: String?,
    var symbol: String?,
    var day_change: Double?,
    var day_change_percentage: Double?,
    var day_high: Double?,
    var day_low: Double?,
    var volume_traded: Double?,
    var last_price: Double?,
    var updated: Long?,
    var avatarUrl: String?
) {
    constructor() : this("", "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, "")
}