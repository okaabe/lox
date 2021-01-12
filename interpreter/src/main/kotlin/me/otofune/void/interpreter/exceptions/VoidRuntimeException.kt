package me.otofune.void.interpreter.exceptions

import me.otofune.void.grammar.VoidException

sealed class VoidRuntimeException(message: String) : VoidException("Runtime", message) {
    class AlreadyDeclared(reference: Any?): VoidRuntimeException("$reference is already been declared")
    class ReferenceError(reference: Any?) : VoidRuntimeException("$reference is not defined")
    class InvalidOperand(operator: String, operand: Any?): VoidRuntimeException("$operator $operand - The operand must be a number")
}
