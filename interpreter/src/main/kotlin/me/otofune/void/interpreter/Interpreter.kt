package me.otofune.void.interpreter

import me.otofune.void.grammar.Stmt
import me.otofune.void.interpreter.runtime.Environment
import me.otofune.void.interpreter.runtime.Evaluator
import me.otofune.void.interpreter.runtime.Executor

class Interpreter {
    private val environment = Environment()
    private val evaluator = Evaluator(environment)
    private val executor = Executor(evaluator, environment)

    fun interpret(statements: List<Stmt>) {
        statements.map { statement ->
            execute(statement)
        }
    }

    private fun execute(statement: Stmt) {
        executor.visitStmt(statement)
    }
}
