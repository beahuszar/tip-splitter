package com.example.tipsplitter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.math.BigDecimal

@Composable
fun TipSplitterScreen(modifier: Modifier = Modifier) {
    var billText by rememberSaveable { mutableStateOf("") }
    var tipPercent by rememberSaveable { mutableFloatStateOf(15f) }
    var people by rememberSaveable { mutableIntStateOf(1) }

    val bill = billText.toBigDecimalOrNull()?.takeIf { it >= BigDecimal.ZERO } ?: BigDecimal.ZERO
    val split = TipCalculator.split(bill, tipPercent.toInt(), people)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Tip Splitter", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = billText,
            onValueChange = { billText = it },
            label = { Text("Bill amount") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bill"),
        )

        Text("Tip: ${tipPercent.toInt()}%")
        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..30f,
            steps = 5,
            modifier = Modifier.testTag("tip"),
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("People")
            OutlinedButton(onClick = { if (people > 1) people-- }, modifier = Modifier.testTag("minus")) { Text("-") }
            Text("$people", modifier = Modifier.testTag("people"))
            OutlinedButton(onClick = { people++ }, modifier = Modifier.testTag("plus")) { Text("+") }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Tip: ${split.tip}")
                Text("Total: ${split.total}")
                Text(
                    "Each pays: ${split.perPerson}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("perPerson"),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TipSplitterScreenPreview() {
    MaterialTheme { TipSplitterScreen() }
}
