package com.example.myfirstapplication

import kotlin.math.pow
import kotlin.math.sqrt


class Tokens {
    /**
     * An abstract class representing tokens such as numbers, operators, constants
     * and parentheses
     *
     * Its inheritance hierarchy is generally as follows:
     *
     * ```
     * Token
     *  ├── LeftParenthesis
     *  ├── RightParenthesis
     *  └── NumberOrOperator
     *      ├── Number
     *      ├── Constant
     *      └── Operator
     *          ├── BinaryOperator
     *          └── UnaryOperator
     * ```
     */
    abstract class Token {
        /**
         * Determines whether `this` has higher operator precedence than `other`
         *
         * @param other The `Token` to compare the current one with
         * @return True if the current `Token` has higher precedence than `other`
         */
        fun hasHigherPrecedenceThan(other: Token): Boolean {
            val precedence = arrayListOf(
                Negation(),
                Factorial(),
                Exponentiation(),
                Divide(),
                Multiply(),
                Plus(),
                Minus(),
            )

            return precedence.indexOf(this) < precedence.indexOf(other)
        }
    }

    class LeftParenthesis : Token() {
        override fun toString(): String {
            return "("
        }
    }

    class RightParenthesis : Token() {
        override fun toString(): String {
            return ")"
        }
    }

    abstract class NumberOrOperator : Token()

    class Number(val value: Double) : NumberOrOperator() {
        override fun toString(): String {
            return this.value.toString()
        }
    }

    abstract class Constant() : NumberOrOperator() {
        abstract fun getValue(): Number
        abstract override fun toString(): String
    }

    class Pi() : Constant() {
        override fun getValue(): Number {
            return Number(kotlin.math.PI)
        }

        override fun toString(): String {
            return "π"
        }
    }

    abstract class Operator : NumberOrOperator()

    abstract class BinaryOperator : Operator() {
        /**
         * Performs a binary operation on two `Number`s
         *
         * @param val1 First operand
         * @param val2 Second operand
         * @return The result of the operation
         */
        abstract fun executeOn(val1: Number, val2: Number): Number
    }

    class Plus : BinaryOperator() {
        override fun executeOn(val1: Number, val2: Number): Number {
            return Number(val1.value + val2.value)
        }

        override fun toString(): String {
            return "+"
        }
    }

    class Minus : BinaryOperator() {
        override fun executeOn(val1: Number, val2: Number): Number {
            return Number(val1.value - val2.value)
        }

        override fun toString(): String {
            return "-"
        }
    }

    class Multiply : BinaryOperator() {
        override fun executeOn(val1: Number, val2: Number): Number {
            return Number(val1.value * val2.value)
        }

        override fun toString(): String {
            return "×"
        }
    }

    class Divide : BinaryOperator() {
        override fun executeOn(val1: Number, val2: Number): Number {
            return Number(val1.value / val2.value)
        }

        override fun toString(): String {
            return "÷"
        }
    }

    class Exponentiation : BinaryOperator() {
        override fun executeOn(val1: Number, val2: Number): Number {
            return Number(val1.value.pow(val2.value))
        }

        override fun toString(): String {
            return "^"
        }
    }

    abstract class UnaryOperator : Operator() {
        /**
         * Performs a unary operation on a `Number`
         *
         * @param value The operand
         * @return The result of the operation
         */
        abstract fun executeOn(value: Number): Number
    }

    class Factorial : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            fun factorial(n: Long): Long {
                return (
                        if (n < 2) 1
                        else n * factorial(n - 1)
                        )
            }

            return Number(
                factorial(
                    value.value.toLong()
                ).toDouble()
            )
        }

        override fun toString(): String {
            return "!"
        }
    }

    class Square : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            return Number(value.value * value.value)
        }

        override fun toString(): String {
            return "²"
        }
    }

    class Negation : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            return Number(-1.0 * value.value)
        }

        override fun toString(): String {
            return "~"
        }
    }


    class SquareRoot : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            return Number(sqrt(value.value))
        }

        override fun toString(): String {
            return "√"
        }
    }

    class Sin : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            return Number(kotlin.math.sin(value.value))
        }

        override fun toString(): String {
            return "sin"
        }
    }

    class Cos : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            return Number(kotlin.math.cos(value.value))
        }

        override fun toString(): String {
            return "cos"
        }
    }

    class Tan : UnaryOperator() {
        override fun executeOn(value: Number): Number {
            return Number(kotlin.math.tan(value.value))
        }

        override fun toString(): String {
            return "tan"
        }
    }
}
