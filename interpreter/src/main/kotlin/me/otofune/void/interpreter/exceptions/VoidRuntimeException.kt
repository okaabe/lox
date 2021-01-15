package me.otofune.void.interpreter.exceptions

import me.otofune.void.grammar.FrontException
import me.otofune.void.grammar.VoidException

sealed class VoidRuntimeException(message: String) : VoidException("Runtime", message) {
    class AlreadyDeclared(reference: Any?): VoidRuntimeException("$reference is already been declared")
    class ReferenceError(reference: Any?) : VoidRuntimeException("$reference is not defined")
    class InvalidOperand(operator: String, operand: Any?): VoidRuntimeException("$operator $operand - The operand must be a number")
    class InvalidCalle(calle: Any?) : VoidRuntimeException("$calle, you can only call functions and classes")
    class InvalidArgumentsAmount(expected: Int, got: Int): VoidRuntimeException("Expected $expected arguments, but got $got")
}
