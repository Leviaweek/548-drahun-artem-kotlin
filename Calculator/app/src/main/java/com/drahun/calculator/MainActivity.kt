package com.drahun.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.drahun.calculator.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.editText.doAfterTextChanged {
            calculateAndDisplay()
        }

        binding.slider.addOnChangeListener { _, _, _ ->
            calculateAndDisplay()
        }
    }

    private fun calculateAndDisplay() {
        val inputText = binding.editText.text.toString()

        if (inputText.isEmpty()) {
            binding.textView.text = ""
            return
        }

        val billAmount = inputText.toDoubleOrNull() ?: 0.0
        val taxPercent = binding.slider.value.toDouble()

        val taxAmount = (billAmount * taxPercent) / 100.0

        val formattedBill = String.format(Locale.US, "%.2f", billAmount)
        val formattedTax = String.format(Locale.US, "%.2f", taxAmount)

        val resultText = "Вартість рахунку: $formattedBill$, відсоток: ${taxPercent.toInt()}%\n" +
                "Сума податку: $formattedTax$"

        binding.textView.text = resultText
    }
}