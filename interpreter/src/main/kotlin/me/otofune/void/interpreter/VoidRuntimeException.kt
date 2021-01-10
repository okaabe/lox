package me.otofune.void.interpreter

import me.otofune.void.front.VoidException

sealed class VoidRuntimeException(message: String) : VoidException("Runtime", message) {
    class ReferenceError(reference: Any?): VoidRuntimeException("$reference is not defined")
    class HasAlreadyBeenDeclared(name: String): VoidRuntimeException("$name has already been declared")
}

