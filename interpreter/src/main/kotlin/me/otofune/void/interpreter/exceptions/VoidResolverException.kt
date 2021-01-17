package me.otofune.void.interpreter.exceptions

import me.otofune.void.grammar.VoidException

sealed class VoidResolverException(message: String): VoidException("Resolver", message)
