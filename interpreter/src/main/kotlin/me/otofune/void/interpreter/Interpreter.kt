package me.otofune.void.interpreter

import me.otofune.void.grammar.Stmt
import me.otofune.void.interpreter.runtime.Builtin
import me.otofune.void.interpreter.runtime.Environment
import me.otofune.void.interpreter.runtime.Evaluator

class Interpreter {
    private val environment = Environment().also {
        Builtin(it).setup()
    }
    private val evaluator = Evaluator(environment)

    fun interpret(statements: List<Stmt>) {
        statements.map { statement ->
            execute(statement)
        }
    }

    private fun execute(statement: Stmt) {
        evaluator.visitStmt(statement)
    }
}
