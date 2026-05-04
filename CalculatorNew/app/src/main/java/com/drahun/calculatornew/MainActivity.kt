package com.drahun.calculatornew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaxCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun TaxCalculatorScreen() {
    var inputText by remember { mutableStateOf("") }
    var taxPercent by remember { mutableFloatStateOf(0f) }

    val billAmount = inputText.toDoubleOrNull() ?: 0.0
    val taxAmount = (billAmount * taxPercent) / 100.0

    val formattedBill = String.format(Locale.US, "%.2f", billAmount)
    val formattedTax = String.format(Locale.US, "%.2f", taxAmount)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
            },
            label = { Text("Введіть суму рахунку") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Відсоток податку: ${taxPercent.toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = taxPercent,
                onValueChange = { newPercent ->
                    taxPercent = newPercent
                },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (inputText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                val resultText = "Вартість рахунку: $formattedBill$, відсоток: ${taxPercent.toInt()}%\n" +
                        "Сума податку: $formattedTax$"

                Text(
                    text = resultText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}