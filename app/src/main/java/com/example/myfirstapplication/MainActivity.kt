package com.example.myfirstapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var characters = mutableListOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button0).setOnClickListener {
            enterCharacter('0')
        }
        findViewById<Button>(R.id.button1).setOnClickListener {
            enterCharacter('1')
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            enterCharacter('2')
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            enterCharacter('3')
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            enterCharacter('4')
        }
        findViewById<Button>(R.id.button5).setOnClickListener {
            enterCharacter('5')
        }
        findViewById<Button>(R.id.button6).setOnClickListener {
            enterCharacter('6')
        }
        findViewById<Button>(R.id.button7).setOnClickListener {
            enterCharacter('7')
        }
        findViewById<Button>(R.id.button8).setOnClickListener {
            enterCharacter('8')
        }
        findViewById<Button>(R.id.button9).setOnClickListener {
            enterCharacter('9')
        }
        findViewById<Button>(R.id.buttonDiv).setOnClickListener {
            enterCharacter('÷')
        }
        findViewById<Button>(R.id.buttonExp).setOnClickListener {
            enterCharacter('^')
        }
        findViewById<Button>(R.id.buttonFact).setOnClickListener {
            enterCharacter('!')
        }
        findViewById<Button>(R.id.buttonLPar).setOnClickListener {
            enterCharacter('(')
        }
        findViewById<Button>(R.id.buttonMinus).setOnClickListener {
            enterCharacter('-')
        }
        findViewById<Button>(R.id.buttonMul).setOnClickListener {
            enterCharacter('×')
        }
        findViewById<Button>(R.id.buttonPlus).setOnClickListener {
            enterCharacter('+')
        }
        findViewById<Button>(R.id.buttonPoint).setOnClickListener {
            enterCharacter('.')
        }
        findViewById<Button>(R.id.buttonRPar).setOnClickListener {
            enterCharacter(')')
        }
        findViewById<Button>(R.id.buttonSin).setOnClickListener {
            enterCharacter('s')
        }
        findViewById<Button>(R.id.buttonCos).setOnClickListener {
            enterCharacter('c')
        }
        findViewById<Button>(R.id.buttonTan).setOnClickListener {
            enterCharacter('t')
        }
        findViewById<Button>(R.id.buttonPi).setOnClickListener {
            enterCharacter('π')
        }
        findViewById<Button>(R.id.buttonSqr).setOnClickListener {
            enterCharacter('²')
        }
        findViewById<Button>(R.id.buttonSqrt).setOnClickListener {
            enterCharacter('√')
        }

        findViewById<Button>(R.id.buttonEq).setOnClickListener {
            buttonEqualsPressed()
        }
        findViewById<Button>(R.id.buttonC).setOnClickListener {
            if (characters.size > 0) {
                characters.removeLast()
                updateInputAndOutput()
            }
        }
        findViewById<Button>(R.id.buttonCE).setOnClickListener {
            characters = mutableListOf<Char>()
            updateInputAndOutput()
        }
    }

    private fun enterCharacter(char: Char) {
        characters.add(char)
        updateInputAndOutput()
    }

    private fun updateInputAndOutput() {
        updateInput()
        updateOutput()
    }

    private fun updateInput() {
        var acc = ""

        for (t in characters) {
            acc += "$t"
        }

        acc = acc.replace("s", "sin")
        acc = acc.replace("c", "cos")
        acc = acc.replace("t", "tan")

        findViewById<TextView>(R.id.input).text = acc
    }

    private fun updateOutput() {
        val tokens = parse(characters.toTypedArray())

        if (tokens != null) {
            val postfix = infixToPostfix(tokens)
            val result = evaluatePostfix(postfix)

            displayResultToOutput(result)
        } else {
            findViewById<TextView>(R.id.answer).text = getString(R.string.error_text)
        }
    }

    private fun buttonEqualsPressed() {
        val tokens = parse(characters.toTypedArray())

        if (tokens != null) {
            val postfix = infixToPostfix(tokens)
            val result = evaluatePostfix(postfix)

            characters = displayResultToOutput(result).toMutableList()

            updateInput()
        } else {
            findViewById<TextView>(R.id.answer).text = getString(R.string.error_text)
        }
    }

    private fun displayResultToOutput(result: Double?): String {
        var resultString = result.toString()

        if (result != null) {
            val pointIndex = resultString.indexOf('.')

            if (resultString.slice((pointIndex + 1) until resultString.length) == "0") {
                resultString = resultString.slice(0 until pointIndex)
            }
        } else {
            resultString = ""
        }

        resultString = resultString.replace("E", "×10^")

        findViewById<TextView>(R.id.answer).text = resultString

        return resultString
    }
}