package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private lateinit var edtNumber1: EditText
    private lateinit var edtNumber2: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtNumber1 = findViewById(R.id.edtNumber1)
        edtNumber2 = findViewById(R.id.edtNumber2)
        tvResult = findViewById(R.id.tvResult)
        btnAdd = findViewById(R.id.btnAdd)
        btnSubtract = findViewById(R.id.btnSubtract)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnDivide = findViewById(R.id.btnDivide)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnAdd.setOnClickListener {
            if (validateInput()) {
                val num1 = edtNumber1.text.toString().toDouble()
                val num2 = edtNumber2.text.toString().toDouble()
                val result = num1 + num2
                tvResult.text = "Result: ${formatResult(result)}"
            }
        }

        btnSubtract.setOnClickListener {
            if (validateInput()) {
                val num1 = edtNumber1.text.toString().toDouble()
                val num2 = edtNumber2.text.toString().toDouble()
                val result = num1 - num2
                tvResult.text = "Result: ${formatResult(result)}"
            }
        }

        btnMultiply.setOnClickListener {
            if (validateInput()) {
                val num1 = edtNumber1.text.toString().toDouble()
                val num2 = edtNumber2.text.toString().toDouble()
                val result = num1 * num2
                tvResult.text = "Result: ${formatResult(result)}"
            }
        }

        btnDivide.setOnClickListener {
            if (validateInput()) {
                val num1 = edtNumber1.text.toString().toDouble()
                val num2 = edtNumber2.text.toString().toDouble()

                if (num2 == 0.0) {
                    tvResult.text = "Result: Undefined"
                } else {
                    val result = num1 / num2
                    tvResult.text = "Result: ${formatResult(result)}"
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val strNum1 = edtNumber1.text.toString()
        val strNum2 = edtNumber2.text.toString()

        if (strNum1.isEmpty() || strNum2.isEmpty()) {
            tvResult.text = "Error: Please enter two numbers"
            return false
        }

        return try {
            strNum1.toDouble()
            strNum2.toDouble()
            true
        } catch (e: NumberFormatException) {
            tvResult.text = "Error: invalid format"
            return false
        }
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            val formatted = String.format("%.6f", value).trimEnd('0').trimEnd('.')
            formatted
        }
    }
}
