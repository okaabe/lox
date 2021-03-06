package me.otofune.void.grammar

import java.lang.RuntimeException

abstract class LoxException(
    private val type: String,
    message: String
) : RuntimeException(message) {
    open fun report() {
        println("[$type error] $message")
    }
}