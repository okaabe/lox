package me.otofune.void.interpreter.exceptions

import me.otofune.void.grammar.LoxException

sealed class LoxResolverException(message: String): LoxException("Resolver", message) {
    class IllegalVariableInitializer(name: Any?, line: Int): LoxResolverException("Illegal variable($name) initializer in line $line")
    class IllegalReturnStatement(): LoxResolverException("Illegal return statement")
    class IllegalThisStatement(line: Int): LoxResolverException("Illegal this statement in $line")
}
