package me.otofune.void.cli

import me.otofune.void.front.Lexer
import me.otofune.void.front.Parser
import me.otofune.void.front.VoidException
import me.otofune.void.interpreter.Interpreter
import java.nio.file.Files
import java.nio.file.Paths

object Executor {
    fun findAndExecuteFile(file: String) {
        if (!file.endsWith(".void")) {
            return println("Invalid file")
        }

        Files.readAllBytes(Paths.get(file)).also { byteArray ->
            executeCode(byteArray.decodeToString())
        }
    }

    private fun executeCode(code: String) {
        val lexer = Lexer(code)
        val parser = Parser(lexer.scan())

        kotlin.runCatching {
            Interpreter().interpret(parser.parse())
        }.exceptionOrNull()?.also { throwable ->
            if (throwable is VoidException) return throwable.report()
            println(throwable.message)
        }
    }
}
