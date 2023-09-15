package com.financeyou.statements

import com.financeyou.data.StatementData
import com.financeyou.utils.Frequency

sealed interface StatementEvent {
    // Statement parameter setters
    object SaveStatement: StatementEvent
    data class SetName(val name: String): StatementEvent
    data class SetIsIncome(val isIncome: Boolean): StatementEvent
    data class SetAmount(val amount: String): StatementEvent
    data class SetFrequency(val frequency: Frequency.Enum): StatementEvent

    // UI setters
    object ShowDialog: StatementEvent
    object HideDialog: StatementEvent
    object FrequencyExpansionChanged: StatementEvent
    object CollapseFrequencies: StatementEvent
    data class SortStatements(val sortType: StatementSortType): StatementEvent
    data class UpdateStatement(val statement: StatementData): StatementEvent
    data class DeleteStatement(val statement: StatementData): StatementEvent
}