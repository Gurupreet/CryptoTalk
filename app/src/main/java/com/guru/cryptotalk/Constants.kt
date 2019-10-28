package com.guru.cryptotalk

class Constants {
    companion object {
        val TAG_CRYPTO = "crypto"
        val TAG_ALERTS = "alerts"
        val TAG_NEWS = "news"

        val upArrow = 0x25B2
        val downArrow = 0x25BC

        val KEY_PREFIX = "ethereum:0x"
        val OPERATION_SWAP = "SWAP"
        val OPERATION_LEND = "LEND"

        fun getEmojiByUnicode(unicode: Int): String {
            return String(Character.toChars(unicode))
        }

        fun getUpArrow(): String {
            return getEmojiByUnicode(upArrow)
        }

        fun getDownArrow(): String {
            return getEmojiByUnicode(downArrow)
        }

        val arrayOfTokens = arrayListOf<String>("ETH",
                "WETH",
               "KNC",
                "DAI",
                "OMG",
                "SNT",
                "ELF",
                "POWR",
                "MANA",
                "BAT",
                "REQ",
                "RDN",
                "APPC",
                "ENG",
                "BQX",
                "AST",
                "LINK",
                "DGX",
                "STORM",
                "IOST",
                "ABT",
                "ENJ",
                "BLZ",
                "POLY",
                "LBA",
                "CVC",
                "POE",
                "PAY",
                "DTA",
                "BNT",
                "TUSD",
                "LEND",
                "MTL",
                "MOC",
                "REP",
                "ZRX",
                "DAT",
                "REN",
                "QKC",
                "MKR",
                "EKO",
                "OST",
                "PT",
                "ABYSS",
                "WBTC",
                "MLN",
                "USDC",
                "EURS",
                "CDT",
                "MCO",
                "PAX",
                "GEN",
                "LRC",
                "RLC",
                "NPXS",
                "GNO",
                "MYB",
                "BAM",
                "SPN",
                "EQUAD",
                "UPP",
                "CND",
                "USDT",
                "SNX",
                "BTU",
                "TKN")
    }
}