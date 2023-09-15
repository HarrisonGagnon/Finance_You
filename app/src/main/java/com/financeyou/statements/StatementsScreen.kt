package com.financeyou.statements

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
                                {
                                    // TODO: Make calculation for time periods other than one month
                                    if (!it.isIncome) {it.amount * -1} else {it.amount}
                                    when (it.frequency) {
                                        Frequency.Enum.ONCE -> {it.amount}
                                        Frequency.Enum.DAILY -> {it.amount * 30.437}
                                        Frequency.Enum.WEEKLY -> {it.amount * 4}
                                        Frequency.Enum.BIWEEKLY -> {it.amount * 2}
                                        Frequency.Enum.MONTHLY -> {it.amount}
                                        // TODO: Determine what to do with time periods greater than one month
                                        Frequency.Enum.SEMIANNUALLY -> {it.amount}
                                        Frequency.Enum.ANNUALLY -> {it.amount}
                                    }
                                }
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
                StatementCard(
                    onEvent = onEvent,
                    statement = statement
                )
            }
        }
    }
}

