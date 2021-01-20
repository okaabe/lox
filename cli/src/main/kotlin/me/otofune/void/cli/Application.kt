package me.otofune.void.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import me.otofune.void.grammar.Lexer
import me.otofune.void.grammar.LoxException
import me.otofune.void.grammar.Parser
import me.otofune.void.interpreter.Interpreter
import me.otofune.void.interpreter.runtime.ReturnException
import java.io.File

class Application : CliktCommand() {
    private val path by argument()
        .help("The file that will be executed")

    private val interpret by option("--interpret", "-i")
        .flag()
        .help("Execute the code with the lox interpreter")

    private val debug by option("--debug", "-d")
        .flag()
        .help("Execute the code in debug mode")

    override fun run() {
        val logger = Logger(debug)

        kotlin.runCatching {
            File(path).also { file ->
                val fileText = file.readText()
                logger.log(fileText, "Code")
                val tokens = Lexer(fileText).scan()
                tokens.map { logger.log(it.toString(), "Lexer token") }
                val statements = Parser(tokens).parse()
                statements.map { logger.log(it.toString(), "Parser statement") }

                if (interpret) {
                    Interpreter().interpret(statements)
                }
            }
        }.exceptionOrNull()?.also { throwable ->
            when(throwable) {
                is LoxException -> throwable.report()
                is ReturnException -> println("Unexpected return exception in somewhere")
                else -> println("[Native exception] ${throwable.message}")
            }
        }
    }
}

fun main(args: Array<String>) = Application().main(args)