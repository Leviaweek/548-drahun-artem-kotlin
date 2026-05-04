package com.drahun.calculator2

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
        // В Compose мы отказываемся от setContentView и ViewBinding
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
    // 1. Создаем состояния (State) для ввода текста и ползунка
    var inputText by remember { mutableStateOf("") }
    var taxPercent by remember { mutableFloatStateOf(0f) } // Значение слайдера (от 0 до 100)

    // 2. Логика расчетов (автоматически пересчитывается при изменении State)
    val billAmount = inputText.toDoubleOrNull() ?: 0.0
    val taxAmount = (billAmount * taxPercent) / 100.0

    val formattedBill = String.format(Locale.US, "%.2f", billAmount)
    val formattedTax = String.format(Locale.US, "%.2f", taxAmount)

    // 3. Строим сам UI (Интерфейс)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Поле ввода (Замена старого EditText) ---
        OutlinedTextField(
            value = inputText,
            onValueChange = { newValue ->
                // Обновляем состояние при вводе
                inputText = newValue
            },
            label = { Text("Введіть суму рахунку") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Слайдер (Тот самый компонент из Практики №8) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Відсоток податку: ${taxPercent.toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = taxPercent,
                onValueChange = { newPercent ->
                    // Обновляем состояние при движении ползунка
                    taxPercent = newPercent
                },
                valueRange = 0f..100f, // Диапазон от 0 до 100 процентов
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- Вывод результата (Замена старого TextView) ---
        // Показываем блок с результатами только если поле ввода не пустое
        // (как в твоем старом if (inputText.isEmpty()) return)
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