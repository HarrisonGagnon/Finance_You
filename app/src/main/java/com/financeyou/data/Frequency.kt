package com.financeyou.data

import java.util.EnumMap

class Frequency {
    // Frequency enum
    enum class FrequencyEnum {
        ONCE,
        DAILY,
        WEEKLY,
        BIWEEKLY,
        MONTHLY,
        SEMIANNUALLY,
        ANNUALLY
    }

    // Provide frequency map
    private var enumToString = EnumMap<FrequencyEnum, String>(FrequencyEnum::class.java)
    init {
        // Initialize map values
        enumToString[FrequencyEnum.ONCE]         = "One-time"
        enumToString[FrequencyEnum.DAILY]        = "Daily"
        enumToString[FrequencyEnum.WEEKLY]       = "Weekly"
        enumToString[FrequencyEnum.BIWEEKLY]     = "Biweekly"
        enumToString[FrequencyEnum.MONTHLY]      = "Monthly"
        enumToString[FrequencyEnum.SEMIANNUALLY] = "Semiannually"
        enumToString[FrequencyEnum.ANNUALLY]     = "Annually"
    }

    fun getFrequencyMap (): EnumMap<FrequencyEnum, String> {
        return enumToString
    }
}