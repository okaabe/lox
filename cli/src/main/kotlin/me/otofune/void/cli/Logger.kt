package me.otofune.void.cli

class Logger(
    private val debug: Boolean = false
) {
    fun log(text: String, prefix: String = "Log") {
        if (!debug) return

        println("[$prefix] $text")
    }
}