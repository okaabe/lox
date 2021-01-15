package me.otofune.void.interpreter.runtime

import java.lang.RuntimeException

class ReturnException(
    val value: Any?
) : RuntimeException()
