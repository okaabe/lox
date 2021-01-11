package me.otofune.void.interpreter.runtime.util

import me.otofune.void.front.Token
import me.otofune.void.interpreter.exceptions.VoidRuntimeException

fun checkNumberOperand(operator: Token, operand: Any?): Double {
    if (operand is Double) {
        return operand.toString().toDouble()
    }

    throw VoidRuntimeException.InvalidOperand(operator.lexeme, operand)
}