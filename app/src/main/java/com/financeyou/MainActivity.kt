package com.financeyou

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.financeyou.ui.FinanceYouApp
import com.financeyou.ui.theme.FinanceYouTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the App's main UI content
        setContent {
            FinanceYouTheme {
                FinanceYouApp()
            }
        }
    }
}