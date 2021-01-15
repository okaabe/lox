package me.otofune.void.interpreter.runtime

import me.otofune.void.interpreter.runtime.util.declareFunction

class Builtin(
    private val environment: Environment
) {
    fun setup() {
        with(environment) {
            declareFunction("println", 1) {
                println(it[0])
            }

            declareFunction("clock") {
                System.currentTimeMillis().toDouble()
            }
        }
    }
}