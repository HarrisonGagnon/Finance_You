package com.financeyou.utils

import java.text.NumberFormat

val formatCurrency = { amount: Double ->
    NumberFormat
    .getCurrencyInstance()
    .format(amount)
}
