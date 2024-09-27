package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var resultView: TextView
    private lateinit var clearButton: Button
    private lateinit var equalsButton: Button
    private lateinit var backspaceButton: Button // Backspace button
    private lateinit var plusMinusButton: Button // +/- button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views
        inputField = findViewById(R.id.numbers)
        resultView = findViewById(R.id.answer)
        clearButton = findViewById(R.id.btn_clr)
        equalsButton = findViewById(R.id.btn_equals)
        backspaceButton = findViewById(R.id.btn_back) // Backspace button
        plusMinusButton = findViewById(R.id.btn_pn) // +/- button

        // Number and operator buttons
        val buttons = listOf(
            R.id.btn_num0, R.id.btn_num1, R.id.btn_num2, R.id.btn_num3, R.id.btn_num4,
            R.id.btn_num5, R.id.btn_num6, R.id.btn_num7, R.id.btn_num8, R.id.btn_num9,
            R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_divide, R.id.btn_per,
            R.id.btn_brac, R.id.btn_dec
        )

        // Set click listeners for all buttons
        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it) }
        }

        // Clear button functionality
        clearButton.setOnClickListener {
            inputField.text.clear()
            resultView.text = ""
        }

        // Equals button functionality
        equalsButton.setOnClickListener {
            evaluateExpression()
        }

        // Backspace button functionality
        backspaceButton.setOnClickListener {
            val currentText = inputField.text.toString()
            if (currentText.isNotEmpty()) {
                inputField.setText(currentText.dropLast(1))
                inputField.setSelection(inputField.text.length) // Move cursor to the end
            }
        }

        // +/- button functionality
        plusMinusButton.setOnClickListener {
            togglePlusMinus()
        }
    }

    private fun onButtonClick(view: View) {
        if (view is Button) {
            val currentText = inputField.text.toString()
            val buttonText = when (view.id) {
                R.id.btn_mul -> "*"  // Multiplication symbol for ExpressionBuilder
                R.id.btn_divide -> "/"  // Division symbol for ExpressionBuilder
                R.id.btn_per -> "/100"  // Percentage logic
                R.id.btn_brac -> handleBrackets(currentText) // Handle parentheses logic
                else -> view.text.toString()  // Handle numbers and other operators
            }
            inputField.setText(currentText + buttonText)
            inputField.setSelection(inputField.text.length) // Move cursor to the end
        }
    }

    private fun handleBrackets(currentText: String): String {
        // Simple logic to alternate between opening and closing brackets
        val openBrackets = currentText.count { it == '(' }
        val closeBrackets = currentText.count { it == ')' }
        return if (openBrackets > closeBrackets) ")" else "("
    }

    private fun togglePlusMinus() {
        val currentText = inputField.text.toString()
        if (currentText.isNotEmpty()) {
            try {
                // If the current number is positive, make it negative and vice versa
                val currentValue = currentText.toDouble()
                val toggledValue = (-currentValue).toString()
                inputField.setText(toggledValue)
            } catch (e: NumberFormatException) {
                // If input is invalid for +/- toggling, don't change the text
            }
        }
    }
//new app
    private fun evaluateExpression() {
        try {
            val expressionText = inputField.text.toString()
                .replace("×", "*")  // Replace multiplication symbol for ExpressionBuilder
                .replace("÷", "/")  // Replace division symbol for ExpressionBuilder
            val expression = ExpressionBuilder(expressionText).build()
            val result = expression.evaluate()

            // Set result as the new input and show it in both the input field and the result view
            inputField.setText(result.toString())
            inputField.setSelection(inputField.text.length) // Move cursor to the end
            resultView.text = result.toString()
        } catch (e: Exception) {
            resultView.text = "Error"
        }
    }
}


