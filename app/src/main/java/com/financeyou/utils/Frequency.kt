package com.financeyou.utils

import java.util.EnumMap

class Frequency {
    // Frequency enum
    enum class Enum {
        ONCE,
        DAILY,
        WEEKLY,
        BIWEEKLY,
        MONTHLY,
        SEMIANNUALLY,
        ANNUALLY
    }

    // Provide frequency map
    private var enumToString = EnumMap<Enum, String>(Enum::class.java)
    init {
        // Initialize map values
        enumToString[Enum.ONCE]         = "One-time"
        enumToString[Enum.DAILY]        = "Daily"
        enumToString[Enum.WEEKLY]       = "Weekly"
        enumToString[Enum.BIWEEKLY]     = "Biweekly"
        enumToString[Enum.MONTHLY]      = "Monthly"
        enumToString[Enum.SEMIANNUALLY] = "Semiannually"
        enumToString[Enum.ANNUALLY]     = "Annually"
    }

    fun getFrequencyMap (): EnumMap<Enum, String> {
        return enumToString
    }
}