package com.guru.cryptotalk.data.api.api

data class ApiResponse(val isError: Boolean, val errorMsg: String, val hash: String, val balance: Int, val ETH: Double, val WETH: Double, val KNC: Double, val DAI: Double, val OMG: Double,
    val BAT: Double,
val LINK: Double,
val ENJ: Double,
val BNT: Double ,
val TUSD: Double,
val REP: Double,
val ZRX: Double,
val REN: Double,
val MKR: Double,
val WBTC: Double,
val USDC: Double,
val PAX: Double,
val USDT: Double,
val SNX: Double )