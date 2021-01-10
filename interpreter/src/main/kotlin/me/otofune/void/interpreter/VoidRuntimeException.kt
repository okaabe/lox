package me.otofune.void.interpreter

<<<<<<< HEAD
import me.otofune.void.front.Token
import me.otofune.void.front.VoidException

sealed class VoidRuntimeException(message: String) : VoidException("Runtime", message) {
    class ReferenceError(reference: Any?): VoidRuntimeException("$reference is not defined")
    class HasAlreadyBeenDeclared(name: String): VoidRuntimeException("$name has already been declared")
}
=======
sealed class VoidRuntimeException
>>>>>>> 2bacfc360f8d467d9495aa1b00686de2200f3338
