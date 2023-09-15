package com.financeyou.statements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.financeyou.R
import com.financeyou.utils.Frequency
import com.financeyou.utils.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStatementDialog(
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
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
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
                            .padding(horizontal = 4.dp)
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
                                onEvent(StatementEvent.SetName(it))
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
                                checkedTrackColor = Color.hsl(140f, .71f, .19f),
                                uncheckedThumbColor = Color.Red,
                                uncheckedTrackColor = Color.hsl(350f, .71f, .19f),
                            )
                        )
                    }

                    // Statement amount
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 4.dp)
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
                                onEvent(StatementEvent.SetAmount(it))
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
                            // Reset amount then display error text
                            onEvent(StatementEvent.SetAmount("000"))
                            Text(
                                text = "Amount error",
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
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isFreqExpanded)},
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        // Statement frequency dropdown contents
                        ExposedDropdownMenu(
                            expanded = state.isFreqExpanded,
                            onDismissRequest = { onEvent(StatementEvent.CollapseFrequencies) },
                            modifier = Modifier
                                .fillMaxWidth()
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