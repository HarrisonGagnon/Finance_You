package com.financeyou.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.financeyou.R
import com.financeyou.data.Frequency
import com.financeyou.ui.component.AnimatedCircle
import com.financeyou.ui.component.StatementCard
import com.financeyou.ui.component.extractProportions
import com.financeyou.ui.theme.FinanceYouTheme
import java.text.DecimalFormat
import java.text.NumberFormat

@ExperimentalMaterial3Api
@Composable
fun FinanceYouApp() {
    // Screen state variables
    var showStatementDialog by rememberSaveable { mutableStateOf(false) }

    // Main scaffolding layout for the application
    Scaffold(
        bottomBar = {
            // TODO: Create navigation bar
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showStatementDialog = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add statement.")
            }
        }
    ) { innerPadding ->
        FinanceYouOverview(
            modifier = Modifier
                .padding(innerPadding)
        )
        when {
           showStatementDialog -> {
               AddStatementDialog(onDismissClicked = { showStatementDialog = false })
           }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStatementDialog(
    onDismissClicked : () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissClicked,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Statement variables to be saved
                var name by rememberSaveable { mutableStateOf("") }
                var frequency by rememberSaveable { mutableStateOf(Frequency.FrequencyEnum.MONTHLY) }

                // Header row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Close button
                    TextButton(
                        onClick = onDismissClicked,
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Icon(
                            Icons.Filled.Close, "Close",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }

                    // Add Statement header text
                    Text(
                        text = stringResource(R.string.statement_add),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier // TODO: Styling
                    )

                    // Spacer between header and save
                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    // Save button
                    TextButton(
                        onClick = onDismissClicked, // TODO: Add statement to database
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Text("Save", color = MaterialTheme.colorScheme.primary)
                    }
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
                        // Statement name column variables
                        var isNameError by rememberSaveable { mutableStateOf(false) }

                        // Statement name text input
                        OutlinedTextField(
                            singleLine = true,
                            label = {
                                Text(
                                    text = stringResource(R.string.statement_name)
                                )
                            },
                            value = name,
                            onValueChange = {
                                // Error check new value
                                if (it.isBlank()) {
                                    isNameError = true
                                } else {
                                    isNameError = false
                                    name = it
                                }
                            },
                            trailingIcon = {
                                if (isNameError) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            isError = isNameError,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        // Statement name error
                        if (isNameError) {
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
                    // Statement Income/Expense column variables
                    var isIncome by rememberSaveable { mutableStateOf(false) }

                    // Statement Income/Expense
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        // Statement Income/Expense text
                        Text(
                            text = stringResource(
                                if (isIncome) R.string.statement_income
                                else R.string.statement_expense),
                            color = if (isIncome) Color.Green else Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )

                        // Statement Income/Expense switch
                        Switch(
                            checked = isIncome,
                            onCheckedChange = { isIncome = it },
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
                        // Statement amount column variables
                        var amount by rememberSaveable { mutableStateOf("") }
                        var amountError by rememberSaveable { mutableStateOf(StatementAmountError.NONE) }

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
                            value = amount,
                            onValueChange = {
                                // Error check new value
                                if (it.isBlank()) {
                                    amountError = StatementAmountError.BLANK
                                } else if (it.toFloatOrNull() == null) {
                                    amountError = StatementAmountError.NOT_NUM
                                }
                                else {
                                    val clean = it.replace("""[,.]""".toRegex(), "")
                                    val parsed = clean.toDouble()
                                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100)).drop(1)
                                    amountError = StatementAmountError.NONE
                                    if (parsed == 0.00)
                                        amountError = StatementAmountError.ZERO_VAL
                                    amount = formatted
                                }
                            },
                            colors = if (isIncome) TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Green)
                            else TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Red),
                            trailingIcon = {
                                if (amountError != StatementAmountError.NONE) {
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
                        if (amountError != StatementAmountError.NONE) {
                            Text(
                                text = when (amountError) {
                                    StatementAmountError.NOT_NUM -> "Not a number"
                                    StatementAmountError.BLANK -> "Cannot leave blank"
                                    StatementAmountError.ZERO_VAL -> "Cannot leave zero"
                                    else -> "Unknown amount error"
                                },
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
                    // Statement frequency row variables
                    var freqIsExpanded by rememberSaveable { mutableStateOf(false) }
                    var freqText by rememberSaveable { mutableStateOf(Frequency().getFrequencyMap()[Frequency.FrequencyEnum.MONTHLY]!!) }

                    // Statement frequency
                    ExposedDropdownMenuBox(
                        expanded = freqIsExpanded,
                        onExpandedChange = { freqIsExpanded = !freqIsExpanded },
                        modifier = Modifier // TODO: Style
                            .fillMaxHeight()
                    ) {
                        // Statement frequency dropdown menu anchor
                        TextField(
                            readOnly = true,
                            value = freqText,
                            onValueChange = {},
                            label = {
                                Text(
                                    text = stringResource(R.string.statement_frequency),
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = freqIsExpanded)},
                            modifier = Modifier.menuAnchor()
                        )

                        // Statement frequency dropdown contents
                        ExposedDropdownMenu(
                            expanded = freqIsExpanded,
                            onDismissRequest = { freqIsExpanded = false },
                        ) {
                            // Use frequency map to populate dropdown
                            val freqMap = Frequency().getFrequencyMap()
                            freqMap.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.value) },
                                    onClick = {
                                        frequency = it.key
                                        freqText = it.value
                                        freqIsExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Statement interest rate and frequency
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }
            }
        }
    }
}

/**
 * Enum for statement amount error
 */
private enum class StatementAmountError {
    NONE,
    BLANK,
    NOT_NUM,
    ZERO_VAL
}

@Preview
@Composable
private fun DialogPreview()
{
    FinanceYouTheme {
        AddStatementDialog(onDismissClicked = {})
    }
}

@Composable
private fun FinanceYouOverview(
    statements: List<String> = List (15) {"$it"},
    modifier: Modifier = Modifier
) {
    // TODO: Modify params
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Box(modifier = Modifier)
            {
                AnimatedCircle(
                    proportions = statements.extractProportions { it.toFloat() },
                    colors = List (15) { Color.Green},
                    Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                )
                Column (
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "Net",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = List (15) {it.toFloat()}.sum().toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        items(items = statements, key = {it}) { statement ->
            StatementCard(
                name = statement,
                income = statement.toInt() % 5 == 0
            )
        }
    }
}