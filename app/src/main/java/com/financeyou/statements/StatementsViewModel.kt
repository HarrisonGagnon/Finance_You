package com.financeyou.statements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeyou.data.StatementData
import com.financeyou.data.StatementRepository
import com.financeyou.utils.Frequency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StatementsViewModel(
    private val statementRepository: StatementRepository
): ViewModel() {


    // Private Flows
    private val _sortType = MutableStateFlow(StatementSortType.MOST_RECENT)
    private val _state = MutableStateFlow(StatementState())
    private var _statements = _sortType.flatMapLatest {
            when (it) {
                StatementSortType.MOST_RECENT -> statementRepository.allStatementsRecent()
                StatementSortType.AMOUNT_ASC -> statementRepository.allStatementsAmountAsc()
                StatementSortType.AMOUNT_DESC -> statementRepository.allStatementsAmountDesc()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
                errorCheckName(event.name)
                if (!state.value.isNameError) {
                    _state.update {
                        it.copy(name = event.name)
                    }
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
                errorCheckAmount(event.amount)
                if (!state.value.isAmountError) {
                    val clean = event.amount.replace("""[,.]""".toRegex(), "")
                    val parsed = clean.toDouble()
                    _state.update {
                        it.copy(
                            amount = parsed/100,
                            isAmountError = false
                        )
                    }
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

            // Statement modifiers
            is StatementEvent.SaveStatement -> {
                // TODO: add error checking
                errorCheckName(state.value.name)

                // Add Statement to database
                viewModelScope.launch(Dispatchers.IO) {
                    statementRepository.upsert(StatementData(
                        name = state.value.name,
                        isIncome = state.value.isIncome,
                        amount = state.value.amount,
                        frequency =state.value.frequency
                    ))
                }

                resetStatementState()
            }
            is StatementEvent.UpdateStatement -> {
                // Set state to statement to update and show dialog
                _state.update {
                    it.copy(
                        name = event.statement.name,
                        isIncome = event.statement.isIncome,
                        amount = event.statement.amount,
                        frequency = event.statement.frequency,
                        isAddingStatement = true
                    )
                }
            }
            is StatementEvent.DeleteStatement -> {
                // Delete statement
                viewModelScope.launch(Dispatchers.IO) {
                    statementRepository.delete(event.statement)
                }
            }

            // UI setters
            StatementEvent.ShowDialog -> {
                // Update isAddingStatement and reset state
                resetStatementState()
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
            is StatementEvent.SortStatements -> {
                _sortType.value = event.sortType
            }
        }
    }

    private fun errorCheckName(name: String) {
        if (name.isBlank()) {
            _state.update {
                it.copy(isNameError = true)
            }
        }
    }

    private fun errorCheckAmount(amount: String) {
        if (amount.isBlank() || amount.toFloatOrNull() == null) {
            _state.update {
                it.copy(
                    isAmountError = true
                )
            }
        }
        else
        {
            // TODO: Fix input
        }
    }

    private fun resetStatementState() {
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
                isAmountError = false,
                isNameError = false
            )
        }
    }
}