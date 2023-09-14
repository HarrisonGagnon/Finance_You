package com.financeyou.statements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeyou.data.StatementDAO
import com.financeyou.data.StatementData
import com.financeyou.utils.Frequency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat

@OptIn(ExperimentalCoroutinesApi::class)
class StatementsViewModel(
    private val statementDao: StatementDAO
): ViewModel() {

    // Private Flows
    private val _sortType = MutableStateFlow(StatementSortType.MOST_RECENT)
    private val _statements = _sortType
        .flatMapLatest {
            when (it) {
                StatementSortType.MOST_RECENT -> statementDao.getAllStatements()
                StatementSortType.AMOUNT_ASC -> statementDao.getStatementsOrderedByAmountAsc()
                StatementSortType.AMOUNT_DESC -> statementDao.getStatementsOrderedByAmountDesc()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(StatementState())

    // Combine private Flows into public State
    val state = combine(_state, _statements, _sortType) { state, statements, sortType ->
       state.copy(
           sortType = sortType,
           statements = statements
       )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatementState())

    /**
     * @brief Handles Statement events
     */
    fun onEvent(event: StatementEvent) {
        when (event) {
            // Statement parameter setters
            is StatementEvent.SetName -> {
                // Update Statement name
                _state.update {
                    it.copy(name = event.name)
                }
            }
            is StatementEvent.SetIsIncome -> {
                // Update Statement income/expense
                _state.update {
                    it.copy(isIncome = event.isIncome)
                }
            }
            is StatementEvent.SetAmount -> {
                // Update Statement amount
                _state.update {
                    it.copy(
                        amount = event.amount,
                        isAmountError = false
                    )
                }
            }
            is StatementEvent.SetFrequency -> {
                // Update Statement frequency
                _state.update {
                    it.copy(
                        frequency = event.frequency,
                        isFreqExpanded = false
                    )
                }
            }
            is StatementEvent.SaveStatement -> {
                // Error checking
                // TODO: add error checking

                // Add Statement to database
                viewModelScope.launch {
                    statementDao.upsertStatement(StatementData(
                        name = state.value.name,
                        isIncome = state.value.isIncome,
                        amount = state.value.amount,
                        frequency =state.value.frequency
                    ))
                }

                // Reset state
                _state.update {
                    it.copy(
                        // Reset Statement parameters
                        name = "",
                        isIncome = false,
                        amount = 0.00,
                        frequency = Frequency.Enum.MONTHLY,

                        // Reset UI state variables
                        isAddingStatement = false,
                        isFreqExpanded = false,
                        isAmountError = true,
                        isNameError = true
                    )
                }
            }

            // UI setters
            StatementEvent.ShowDialog -> {
                // Update isAddingStatement
                _state.update {
                    it.copy(isAddingStatement = true)
                }
            }
            StatementEvent.HideDialog -> {
                // Update isAddingStatement
                _state.update {
                    it.copy(isAddingStatement = false)
                }
            }
            StatementEvent.FrequencyExpansionChanged -> {
                // Update isFreqExpanded
                _state.update {
                    it.copy(isFreqExpanded = !state.value.isFreqExpanded)
                }
            }
            StatementEvent.CollapseFrequencies -> {
                // Update isFreqExpanded
                _state.update {
                    it.copy(isFreqExpanded = false)
                }
            }
            is StatementEvent.SetIsNameError -> {
               // Update isNameError
                _state.update {
                    it.copy(isNameError = event.isNameError)
                }
            }
            is StatementEvent.SetIsAmountError -> {
                // Update isAmountError
                _state.update {
                    it.copy(isAmountError = event.isAmountError)
                }
            }
            is StatementEvent.SortStatements -> {
                _sortType.value = event.sortType
            }
            is StatementEvent.DeleteStatement -> {
                // Delete statement
//                viewModelScope.launch {
//                    statementDao.deleteStatement()
//                }
            }
        }
    }



}