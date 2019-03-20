package com.example.calculator.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.calculator.R
import kotlinx.android.synthetic.main.activity_main.*
import com.example.calculator.mxparser.Expression
import com.example.calculator.room.CalculatorDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButton()
    }

    private fun initButton() {
        btn0.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnPlus.setOnClickListener(this)
        btnMinus.setOnClickListener(this)
        btnMultiply.setOnClickListener(this)
        btnDivide.setOnClickListener(this)
        btnPoint.setOnClickListener(this)
        btnCalculate.setOnClickListener(this)
        btnBrackets.setOnClickListener(this)
        btnPercent.setOnClickListener(this)
        btnSign.setOnClickListener(this)
        btnShowHistory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == null)
            return

        val operations = charArrayOf('+', '-', '*', '/')

        when (v.id) {
            R.id.btn0 -> textExpression.text = String.format("%s0", textExpression.text)
            R.id.btn1 -> textExpression.text = String.format("%s1", textExpression.text)
            R.id.btn2 -> textExpression.text = String.format("%s2", textExpression.text)
            R.id.btn3 -> textExpression.text = String.format("%s3", textExpression.text)
            R.id.btn4 -> textExpression.text = String.format("%s4", textExpression.text)
            R.id.btn5 -> textExpression.text = String.format("%s5", textExpression.text)
            R.id.btn6 -> textExpression.text = String.format("%s6", textExpression.text)
            R.id.btn7 -> textExpression.text = String.format("%s7", textExpression.text)
            R.id.btn8 -> textExpression.text = String.format("%s8", textExpression.text)
            R.id.btn9 -> textExpression.text = String.format("%s9", textExpression.text)

            R.id.btnDelete -> textExpression.text = textExpression.text.toString().dropLast(1)
            R.id.btnCancel -> {
                textExpression.text = ""
                textResult.text = ""
            }

            R.id.btnPlus -> plus(operations)
            R.id.btnMinus -> minus(operations)
            R.id.btnMultiply -> multiply(operations)
            R.id.btnDivide -> divide(operations)

            R.id.btnPoint -> point(operations)
            R.id.btnPercent -> percent()
            R.id.btnBrackets -> brackets(operations)

            R.id.btnCalculate -> calculate()

            R.id.btnShowHistory -> {
                val intent = Intent(applicationContext, HistoryOperationsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkExpression(expression: String): Boolean {
        val lastSymbol = expression.last()
        if (!lastSymbol.isDigit() && lastSymbol != ')' && lastSymbol != '%')
            return false

        val countOpenBrackets = expression.count { c: Char -> c == '('  }
        val countCloseBrackets = expression.count { c: Char -> c == ')'  }
        if (countOpenBrackets != countCloseBrackets)
            return false

        return true
    }

    private fun calculate() {
        val expression = textExpression.text.toString()
        if (expression.isEmpty())
            return

        if (checkExpression(expression)) {
            textResult.text = Expression(expression).calculate().toString()
            val expressionToDB = com.example.calculator.room.Expression(
                expression, "=${textResult.text}",
                System.currentTimeMillis()
            )

            val database = CalculatorDatabase.getInstance(this)
            database!!.expressionDao().insert(expressionToDB)
        } else {
            textResult.text = ""
            Toast.makeText(this, R.string.error_expression, Toast.LENGTH_SHORT).show()
        }
    }

    private fun brackets(operations: CharArray) {
        val expression = textExpression.text
        if (expression.isEmpty()) {
            textExpression.text = "("
            return
        }

        val lastSymbol = expression.last()
        if (lastSymbol in operations || lastSymbol == '(')
            textExpression.text = String.format("%s(", expression)
        else if (lastSymbol.isDigit() || lastSymbol == ')') {
            val countOpenBrackets = expression.count { c: Char -> c == '(' }
            val countCloseBrackets = expression.count { c: Char -> c == ')' }
            if (countOpenBrackets > countCloseBrackets)
                textExpression.text = String.format("%s)", expression)
            else
                textExpression.text = String.format("%s*(", expression)
        }
    }

    private fun percent() {
        val expression = textExpression.text
        if (expression.isEmpty())
            return

        val lastSymbol = expression.last()
        if (lastSymbol.isDigit() || lastSymbol == ')')
            textExpression.text = String.format("%s%%", expression)
        else
            Toast.makeText(this, R.string.error_add_symbol, Toast.LENGTH_SHORT).show()
    }

    private fun point(operations: CharArray) {
        val expression = textExpression.text
        if (expression.isEmpty()) {
            textExpression.text = "0."
            return
        }

        val lastSymbol = expression.last()
        if (lastSymbol in operations || lastSymbol == '(')
            textExpression.text = String.format("%s0.", expression)
        else if (lastSymbol.isDigit()) {
            val range = expression.length - 1 downTo 0
            for (i in range) {
                if (expression[i] in operations) {
                    textExpression.text = String.format("%s.", expression)
                    break
                } else if (expression[i] == '.')
                    break
                if (i == 0)
                    textExpression.text = String.format("%s.", expression)
            }
        }
    }

    private fun divide(operations: CharArray) {
        val expression = textExpression.text
        if (expression.isEmpty() || expression.length == 1 && !expression.last().isDigit())
            return

        val lastSymbol = expression.last()
        if (lastSymbol.isDigit() || lastSymbol == ')' || lastSymbol == '%')
            textExpression.text = String.format("%s/", expression)
        else if (lastSymbol in operations)
            textExpression.text = String.format("%s/", expression.dropLast(1))
        else
            Toast.makeText(this, R.string.error_add_symbol, Toast.LENGTH_SHORT).show()
    }

    private fun multiply(operations: CharArray) {
        val expression = textExpression.text
        if (expression.isEmpty() || expression.length == 1 && !expression.last().isDigit())
            return

        val lastSymbol = expression.last()
        if (lastSymbol.isDigit() || lastSymbol == ')' || lastSymbol == '%')
            textExpression.text = String.format("%s*", expression)
        else if (lastSymbol in operations)
            textExpression.text = String.format("%s*", expression.dropLast(1))
        else
            Toast.makeText(this, R.string.error_add_symbol, Toast.LENGTH_SHORT).show()
    }

    private fun minus(operations: CharArray) {
        if (textExpression.text.isEmpty()) {
            textExpression.text = "-"
            return
        }

        val lastSymbol = textExpression.text.last()
        if (lastSymbol.isDigit() || lastSymbol == ')' || lastSymbol == '%')
            textExpression.text = String.format("%s-", textExpression.text)
        else if (lastSymbol in operations)
            textExpression.text = String.format("%s-", textExpression.text.dropLast(1))
        else
            Toast.makeText(this, R.string.error_add_symbol, Toast.LENGTH_SHORT).show()
    }

    private fun plus(operations: CharArray) {
        val expression = textExpression.text
        if (expression.isEmpty() || expression.length == 1 && !expression.last().isDigit())
            return

        val lastSymbol = expression.last()
        if (lastSymbol.isDigit() || lastSymbol == ')' || lastSymbol == '%')
            textExpression.text = String.format("%s+", expression)
        else if (lastSymbol in operations)
            textExpression.text = String.format("%s+", expression.dropLast(1))
        else
            Toast.makeText(this, R.string.error_add_symbol, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        CalculatorDatabase.destroyInstance()
        super.onDestroy()
    }
}