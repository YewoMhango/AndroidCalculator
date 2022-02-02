package com.example.myfirstapplication


fun parse(input: Array<Char>): Array<Tokens.Token>? {
    val output = mutableListOf<Tokens.Token>()

    var i = 0

    while (i < input.size) {
        output.add(
            when (input[i]) {
                '+' -> Tokens.Plus()
                '-' -> Tokens.Minus()
                '×' -> Tokens.Multiply()
                '÷' -> Tokens.Divide()
                '^' -> Tokens.Exponentiation()
                '!' -> Tokens.Factorial()
                '(' -> Tokens.LeftParenthesis()
                ')' -> Tokens.RightParenthesis()
                '√' -> Tokens.SquareRoot()
                '²' -> Tokens.Square()
                'π' -> Tokens.Pi()
                't' -> Tokens.Tan()
                'c' -> Tokens.Cos()
                's' -> Tokens.Sin()
                else -> {
                    if (input[i].isDigit() || input[i] == '.') {
                        var numberString = ""

                        numberString += input[i]

                        while (i + 1 < input.size && (input[i + 1].isDigit() || input[i + 1] == '.')) {
                            i += 1
                            numberString += input[i]
                        }

                        val number = numberString.toDoubleOrNull()

                        if (number == null) {
                            return null
                        } else {
                            Tokens.Number(number)
                        }
                    } else {
                        return null
                    }
                }
            }
        )

        i += 1
    }

    return output.toTypedArray()
}

fun infixToPostfix(expression: Array<Tokens.Token>): Array<Tokens.Token> {
    val stack = mutableListOf<Tokens.Token>()
    val result = mutableListOf<Tokens.Token>()

    for ((i, token) in expression.withIndex()) {
        when (token) {
            is Tokens.Number -> {
                if (
                    i != 0 &&
                    (expression[i - 1] is Tokens.Number ||
                            expression[i - 1] is Tokens.RightParenthesis ||
                            expression[i - 1] is Tokens.Constant)
                ) {
                    stack.add(Tokens.Multiply())
                }
                result.add(token)
            }

            is Tokens.Constant -> {
                if (
                    i != 0 &&
                    (expression[i - 1] is Tokens.Number ||
                            expression[i - 1] is Tokens.RightParenthesis ||
                            expression[i - 1] is Tokens.Constant)
                ) {
                    stack.add(Tokens.Multiply())
                }
                result.add(token.getValue())
            }

            is Tokens.LeftParenthesis -> {
                if (
                    i != 0 &&
                    (expression[i - 1] is Tokens.Number ||
                            expression[i - 1] is Tokens.RightParenthesis ||
                            expression[i - 1] is Tokens.Constant)
                ) {
                    stack.add(Tokens.Multiply())
                }
                stack.add(Tokens.LeftParenthesis())
            }

            is Tokens.RightParenthesis -> {
                while (stack.isNotEmpty() && stack.last() !is Tokens.LeftParenthesis) {
                    result.add(stack.removeLast())
                }
                if (stack.size > 0) {
                    stack.removeLast()
                }
            }

            else -> {
                val token =
                    if (token is Tokens.Minus && (
                                i == 0
                                        || expression[i - 1] is Tokens.Operator
                                        || expression[i - 1] is Tokens.LeftParenthesis
                                )
                    ) {
                        Tokens.Negation()
                    } else {
                        token
                    }

                if (
                    (
                            token is Tokens.UnaryOperator &&
                                    token !is Tokens.Negation &&
                                    token !is Tokens.Square &&
                                    token !is Tokens.Factorial
                            )
                    && i != 0
                    && (
                            expression[i - 1] is Tokens.Number
                                    || expression[i - 1] is Tokens.RightParenthesis
                                    || expression[i - 1] is Tokens.Constant
                            )
                ) {
                    stack.add(Tokens.Multiply())
                }

                while (
                    stack.isNotEmpty()
                    && stack.last() !is Tokens.LeftParenthesis
                    && stack.last().hasHigherPrecedenceThan(token)
                ) {
                    result.add(stack.removeLast())
                }

                if (token is Tokens.Factorial || token is Tokens.Square) {
                    result.add(token)
                } else {
                    stack.add(token)
                }
            }
        }
    }

    while (stack.isNotEmpty()) {
        result.add(stack.removeLast())
    }

    return result.toTypedArray()
}

fun evaluatePostfix(expression: Array<Tokens.Token>): Double? {
    val stack = mutableListOf<Tokens.Token>()

    for (element in expression) {
        if (element is Tokens.Number) {
            stack.add(element)
        } else if (element is Tokens.Operator && stack.size > 0) {
            val answer = if (element is Tokens.UnaryOperator) {
                if (stack.size == 0) {
                    return null
                }
                val last = stack.removeLast()

                if (last is Tokens.Number) {
                    element.executeOn(last)
                } else {
                    // printError("Not a valid number: $last")
                    return null
                }
            } else if (element is Tokens.BinaryOperator) {
                if (stack.size == 0) {
                    return null
                }
                val secondOperand = stack.removeLast()

                if (stack.size == 0) {
                    return null
                }
                val firstOperand = stack.removeLast()

                if (firstOperand is Tokens.Number) {
                    if (secondOperand is Tokens.Number) {
                        element.executeOn(firstOperand, secondOperand)
                    } else {
                        // printError("Not a valid number: $secondOperand")
                        return null
                    }
                } else {
                    // printError("Not a valid number: $firstOperand")
                    return null
                }
            } else {
                return null
            }

            stack.add(answer)
        }
    }

    return if (stack.size > 0) {
        when (val last = stack.last()) {
            is Tokens.Number -> last.value
            else -> {
                // printError("Invalid result: $last")
                null
            }
        }
    } else {
        null
    }
}
