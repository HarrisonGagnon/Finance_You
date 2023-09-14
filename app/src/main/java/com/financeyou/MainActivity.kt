package com.financeyou

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.financeyou.data.FinanceYouDatabase
import com.financeyou.data.StatementData
import com.financeyou.statements.StatementsScreen
import com.financeyou.statements.StatementsViewModel
import com.financeyou.ui.theme.FinanceYouTheme
import com.financeyou.utils.Frequency

class MainActivity : ComponentActivity() {

    // Database
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceYouDatabase::class.java,
            "statements.db"
        ).build()
    }

    // ViewModels
    private val statementsViewModel by viewModels<StatementsViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StatementsViewModel(database.statementDAO) as T
                }
            }
        }
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the App's main UI content
        setContent {
            FinanceYouTheme {
                // TODO: Determine screens and navigation

                val state by statementsViewModel.state.collectAsState()
                StatementsScreen(
                    state = state,
                    onEvent = statementsViewModel::onEvent)
            }
        }
    }
}