package com.financeyou.statements

import com.financeyou.data.StatementData
import com.financeyou.utils.Frequency

data class StatementState(
    // Sorted list of all statements and sort type
    val statements: List<StatementData> = emptyList(),
    val sortType: StatementSortType = StatementSortType.MOST_RECENT,

    // Statement parameters
    val name: String = "",
    val isIncome: Boolean = false,
    val amount: Double = 0.00,
    val frequency: Frequency.Enum = Frequency.Enum.MONTHLY,

    // UI Modifier Variables
    val isAddingStatement: Boolean = false,
    val isFreqExpanded: Boolean = false,
    val isNameError: Boolean = false,
    val isAmountError: Boolean = false
)