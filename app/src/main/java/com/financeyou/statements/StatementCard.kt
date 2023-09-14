package com.financeyou.statements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.financeyou.data.StatementData
import com.financeyou.utils.Frequency

@Composable
fun StatementCard(
    statement: StatementData,
) {
   Card(
       modifier = Modifier
           .padding(horizontal = 16.dp, vertical = 4.dp)
           .fillMaxWidth()
           .height(80.dp)
   ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 4.dp, vertical = 16.dp),
           horizontalArrangement = Arrangement.Center
       ) {

           Column(
               verticalArrangement = Arrangement.spacedBy(2.dp),
               modifier = Modifier
                   .fillMaxHeight() // TODO: Style
           ) {
               Text(
                   text = statement.name,
                   style = MaterialTheme.typography.bodyLarge,
                   fontWeight = FontWeight.Bold
               )

               Frequency().getFrequencyMap()[statement.frequency]?.let {
                   Text(
                       text = it,
                       style = MaterialTheme.typography.bodySmall,
                   )
               }
           }

           Spacer( modifier = Modifier
               .weight(1f)
               .fillMaxHeight())

           Text(
               text = String.format("$%.2f", statement.amount),
               style = MaterialTheme.typography.labelMedium,
               color = if (statement.isIncome) Color.Green else Color.Red,
               textAlign = TextAlign.Center,
               modifier = Modifier
                   .align(Alignment.CenterVertically)
           )

           // TODO: delete statement and confirm with dialog
//           IconButton(
//               onClick = { /*TODO*/ },
//               modifier = Modifier
//                   .fillMaxHeight()
//           ) {
//               Icon(
//                   imageVector = Icons.Default.Delete,
//                   contentDescription = "More statement info"
//               )
//           }
       }
   }
}

@Preview
@Composable
fun PrevStatementCard() {
    StatementCard(
        StatementData(
        "Test Statement",
        false,
        100.00,
        Frequency.Enum.MONTHLY)
    )
}