package com.example.myfirstapplication

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var characters = mutableListOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        /**
         * Represents the symbol which each button is supposed to input when clicked
         */
        val buttonCharacterMap = mapOf(
            R.id.button0 to '0',
            R.id.button1 to '1',
            R.id.button2 to '2',
            R.id.button3 to '3',
            R.id.button4 to '4',
            R.id.button5 to '5',
            R.id.button6 to '6',
            R.id.button7 to '7',
            R.id.button8 to '8',
            R.id.button9 to '9',
            R.id.buttonDiv to '÷',
            R.id.buttonExp to '^',
            R.id.buttonFact to '!',
            R.id.buttonLPar to '(',
            R.id.buttonMinus to '-',
            R.id.buttonMul to '×',
            R.id.buttonPlus to '+',
            R.id.buttonPoint to '.',
            R.id.buttonRPar to ')',
            R.id.buttonSin to 's',
            R.id.buttonCos to 'c',
            R.id.buttonTan to 't',
            R.id.buttonPi to 'π',
            R.id.buttonSqr to '²',
            R.id.buttonSqrt to '√',
        )

        buttonCharacterMap.forEach({ (buttonId, character) ->
            findViewById<Button>(buttonId).setOnClickListener {
                enterCharacter(character)
            }
        })

        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            buttonEqualsPressed()
        }
        findViewById<Button>(R.id.buttonBackspace).setOnClickListener {
            if (characters.size > 0) {
                characters.removeLast()
                updateInputAndOutput()
            }
        }
        findViewById<Button>(R.id.buttonClear).setOnClickListener {
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
        var inputString = ""

        for (t in characters) {
            inputString += "$t"
        }

        inputString = inputString.replace("s", "sin")
        inputString = inputString.replace("c", "cos")
        inputString = inputString.replace("t", "tan")

        findViewById<TextView>(R.id.input).text = inputString
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
            findViewById<TextView>(R.id.answer).text = ""

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