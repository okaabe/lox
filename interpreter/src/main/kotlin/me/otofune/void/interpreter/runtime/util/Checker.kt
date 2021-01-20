package me.otofune.void.interpreter.runtime.util

import me.otofune.void.grammar.Token
import me.otofune.void.interpreter.exceptions.LoxRuntimeException

fun checkNumberOperand(operator: Token, operand: Any?): Double {
    if (operand is Double) {
        return operand.toString().toDouble()
    }

    throw LoxRuntimeException.InvalidOperand(operator.lexeme, operand)
}