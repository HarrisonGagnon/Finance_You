package com.financeyou.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatementCard(
    name: String,
    income: Boolean,
    modifier: Modifier = Modifier
) {
   Card(
       modifier = modifier
           .padding(horizontal = 16.dp, vertical = 4.dp),
       colors = CardDefaults.cardColors(
           containerColor = if (income) MaterialTheme.colorScheme.primaryContainer
           else MaterialTheme.colorScheme.errorContainer
       )
   ) {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(20.dp)
       ) {

           Text(
               text = name,
               color = if (income) Color.Green else Color.Red)
       }
   }
}