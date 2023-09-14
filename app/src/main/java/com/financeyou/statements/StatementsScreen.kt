package com.financeyou.statements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.financeyou.R
import com.financeyou.ui.component.AnimatedCircle
import com.financeyou.ui.component.extractProportions
import com.financeyou.ui.component.mapColors
import com.financeyou.utils.Frequency
import com.financeyou.utils.formatCurrency


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun StatementsScreen(
    state: StatementState,
    onEvent: (StatementEvent) -> Unit
) {
    // Screen scaffold
    Scaffold(
        bottomBar = {
            // TODO: Create navigation bar
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(StatementEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add statement.")
            }
        }
    ) { innerPadding ->

        // Show Add Statement Dialog
        when {
            state.isAddingStatement -> {
                AddStatementDialog(
                    state = state,
                    onEvent = onEvent
                )
            }
        }

        // Scaffold screen main layout
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Animated circle header item
            item {
                Box(modifier = Modifier)
                {
                AnimatedCircle(
                    proportions = state.statements.extractProportions { it.amount },
                    colors = state.statements.map { mapColors(it.isIncome) },
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                    Column (
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = "Net",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = formatCurrency(
                                state.statements.sumOf
                                { if (it.isIncome) it.amount else (it.amount * -1)}
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            // Statements Sticky Header
            stickyHeader {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.statements_header),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            items(items = state.statements) { statement ->
                StatementCard(statement)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStatementDialog(
    state: StatementState,
    onEvent: (StatementEvent) -> Unit
) {
    Dialog(
        onDismissRequest = { onEvent(StatementEvent.HideDialog) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // TODO: Consider moving error checks to ViewModel
                // Header row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Add Statement header text
                    Text(
                        text = stringResource(R.string.statement_add),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()// TODO: Styling
                    )
                }

                // Statement name
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        // Statement name text input
                        OutlinedTextField(
                            singleLine = true,
                            label = {
                                Text(
                                    text = stringResource(R.string.statement_name)
                                )
                            },
                            value = state.name,
                            onValueChange = {
                                // Error check new value
                                if (it.isBlank()) {
//                                    isNameError = true
                                    onEvent(StatementEvent.SetIsNameError(true))
                                    onEvent(StatementEvent.SetName(""))
                                } else {
                                    onEvent(StatementEvent.SetIsNameError(false))
                                    onEvent(StatementEvent.SetName(it))
                                }
                                // Update savable
//                                isSavable = checkSavable()
                            },
                            trailingIcon = {
                                if (state.isNameError) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            isError = state.isNameError,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        // Statement name error
                        if (state.isNameError) {
                            Text(
                                text = stringResource(R.string.statement_name_error),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Statement type and amount
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Statement Income/Expense
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        // Statement Income/Expense text
                        Text(
                            text = stringResource(
                                if (state.isIncome) R.string.statement_income
                                else R.string.statement_expense),
                            color = if (state.isIncome) Color.Green else Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )

                        // Statement Income/Expense switch
                        Switch(
                            checked = state.isIncome,
                            onCheckedChange = { onEvent(StatementEvent.SetIsIncome(it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Green,
                                //TODO: update color checkedTrackColor =
                                uncheckedThumbColor = Color.Red
                                //TODO: update color uncheckedTrackColor =
                            )
                        )
                    }

                    // Statement amount
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        // Statement amount text input
                        OutlinedTextField(
                            singleLine = true,
                            label = {
                                Text(
                                    text = stringResource(R.string.statement_amount)
                                )
                            },
                            leadingIcon = { Text("$") },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.statement_zero)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            value = formatCurrency(state.amount).drop(1),
                            onValueChange = {
                                if (it.isBlank() || it.toFloatOrNull() == null) {
                                    onEvent(StatementEvent.SetIsAmountError(true))
                                }
                                else
                                {
                                    // TODO: Fix input
                                    val clean = it.replace("""[,.]""".toRegex(), "")
                                    val parsed = clean.toDouble()
                                    onEvent(StatementEvent.SetAmount(parsed/100))
                                }
                            },
                            colors = if (state.isIncome) TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Green)
                            else TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Red),
                            trailingIcon = {
                                if (state.isAmountError) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        // Statement amount error
                        if (state.isAmountError) {
                            Text(
                                text = "Amount error",
//                                when (amountError) {
//                                    StatementAmountError.NOT_NUM -> "Not a number"
//                                    StatementAmountError.BLANK -> "Cannot leave blank"
//                                    StatementAmountError.ZERO_VAL -> "Cannot leave zero"
//                                    else -> "Amount error"
//                                },
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Statement frequency
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Statement frequency
                    ExposedDropdownMenuBox(
                        expanded = state.isFreqExpanded,
                        onExpandedChange = { onEvent(StatementEvent.FrequencyExpansionChanged) },
                        modifier = Modifier // TODO: Style
                            .fillMaxWidth()
                    ) {
                        // Statement frequency dropdown menu anchor
                        TextField(
                            readOnly = true,
                            value = Frequency().getFrequencyMap()[state.frequency]!!,
                            onValueChange = {},
                            label = {
                                Text(
                                    text = stringResource(R.string.statement_frequency),
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isFreqExpanded)},
                            modifier = Modifier.menuAnchor()
                        )

                        // Statement frequency dropdown contents
                        ExposedDropdownMenu(
                            expanded = state.isFreqExpanded,
                            onDismissRequest = { onEvent(StatementEvent.CollapseFrequencies) },
                        ) {
                            // Use frequency map to populate dropdown
                            val freqMap = Frequency().getFrequencyMap()
                            freqMap.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.value) },
                                    onClick = {
                                        onEvent(StatementEvent.SetFrequency(it.key))
                                    }
                                )
                            }
                        }
                    }
                }

                // TODO: add statement interest rate and compounding frequency
                // Statement interest rate and frequency
//                Row (
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                }

                // Cancel and Save icons
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel Button
                    TextButton(
                        onClick = { onEvent(StatementEvent.HideDialog) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    // Save button
                    TextButton(
                        onClick = {
                            if (!state.isNameError || !state.isAmountError)
                            {
                                onEvent(StatementEvent.SaveStatement)
                            }
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}