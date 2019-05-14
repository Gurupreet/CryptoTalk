package com.guru.cryptotalk.data.api.model

data class Crypto(
    val name: String?,
    val exchange: String?,
    val symbol: String?,
    val day_change: Double?,
    val day_change_percentage: Double?,
    val day_high: Double?,
    val day_low: Double?,
    val volume_traded: Double?,
    val last_price: Double?,
    val updated: Long?,
    val avatarUrl: String?
) {
    constructor() : this("", "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, "")
}