package com.example.myfirstapplication

/**
 * This function transforms a `Char` array of user input into an array of
 * tokens representing the logical parts of a mathematical expression, like
 * numbers, operators, parentheses, etc. The resulting array is in infix
 * notation
 *
 * @param input The input array
 * @return An array of `Token`s
 */
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

                        // Since input[i], the current character, is a digit or a decimal point,
                        // and a number might be composed of multiple digits, we want to concatenate
                        // them all into one string which will be converted into a single number
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

/**
 * This function takes an **infix** expression generated by the `parse` function
 * and converts it into a **postfix** expression for easier calculation of the
 * result.
 *
 * Basically, an infix expression has the operator between two operands (like
 * `1 + 2`) while a postfix expression has the operator after the two operands
 * (like `1 2 +`). The natural way a user would write math expressions is with
 * the infix notation, but postfix expressions are much easier for a machine to
 * compute (using a stack ADT)
 *
 * @param expression An array of `Token`s in infix notation
 * @return An array of `Token`s in postfix notation
 */
fun infixToPostfix(expression: Array<Tokens.Token>): Array<Tokens.Token> {
    val stack = mutableListOf<Tokens.Token>()
    val result = mutableListOf<Tokens.Token>()

    for ((i, token) in expression.withIndex()) {
        when (token) {
            is Tokens.Number -> {
                // If the number is in a situation which requires implicit multiplication,
                // such as when the number follows a closing bracket, then insert a
                // multiplication symbol
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
                // If the constant is in a situation which requires implicit multiplication,
                // then insert a multiplication symbol (For example, `3π` would become `3 * π`)
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
                // Again, implicit multiplication cases
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
                val operator =
                    // For situations where a minus should be interpreted as negation
                    // rather than subtraction
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

                // More implicit multiplication. But this time when there is a prefixed
                // unary function such as sin, cos, tan or √
                if (
                    (
                            operator is Tokens.UnaryOperator &&
                                    operator !is Tokens.Negation &&
                                    operator !is Tokens.Square &&
                                    operator !is Tokens.Factorial
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
                    && stack.last().hasHigherPrecedenceThan(operator)
                ) {
                    result.add(stack.removeLast())
                }

                // Factorial (!) and Square (²) are already postfix notations, so
                // they just need to be added to the resulting array of tokens
                if (operator is Tokens.Factorial || operator is Tokens.Square) {
                    result.add(operator)
                } else {
                    stack.add(operator)
                }
            }
        }
    }

    while (stack.isNotEmpty()) {
        result.add(stack.removeLast())
    }

    return result.toTypedArray()
}

/**
 * Evaluates a mathematical postfix expression to generate an answer/result
 *
 * @param expression A postfix expression to be evaluated
 * @return The answer
 */
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
