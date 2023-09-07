package com.financeyou.ui

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.financeyou.ui.component.AnimatedCircle
import com.financeyou.ui.component.StatementCard
import com.financeyou.ui.component.extractProportions
import com.financeyou.ui.theme.FinanceYouTheme

@ExperimentalMaterial3Api
@Preview
@Composable
fun FinanceYouApp() {
    // Creates the "scaffolding" for the application
    var showStatementDialog by remember { mutableStateOf(false) }

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

@Composable
private fun AddStatementDialog(
    onDismissClicked : () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissClicked,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Text(
                    text = "Add Statement",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                )
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight())
                TextButton(
                    onClick = onDismissClicked, // TODO: Add statement to database
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Text("Save", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
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