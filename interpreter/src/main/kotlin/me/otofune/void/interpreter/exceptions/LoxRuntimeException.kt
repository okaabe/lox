package me.otofune.void.interpreter.exceptions

import me.otofune.void.grammar.LoxException

sealed class LoxRuntimeException(message: String) : LoxException("Runtime", message) {
    class AlreadyDeclared(reference: Any?): LoxRuntimeException("$reference is already been declared")
    class ReferenceError(reference: Any?) : LoxRuntimeException("$reference is not defined")
    class InvalidOperand(operator: String, operand: Any?): LoxRuntimeException("$operator $operand - The operand must be a number")
    class InvalidCalle(calle: Any?) : LoxRuntimeException("$calle, you can only call functions and classes")
    class InvalidArgumentsAmount(expected: Int, got: Int): LoxRuntimeException("Expected $expected arguments, but got $got")
    class InvalidObjectGetProperty(property: Any?, line: Int): LoxRuntimeException(".$property Invalid object get property")
    class UndefinedProperty(obj: String, property: String): LoxRuntimeException("Undefined object property, $obj doesn't have $property property")
}
