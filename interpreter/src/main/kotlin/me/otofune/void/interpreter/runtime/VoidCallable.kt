package me.otofune.void.interpreter.runtime

import me.otofune.void.grammar.Stmt

sealed class VoidCallable {
    abstract fun call(evaluator: Evaluator, arguments: List<Any?>): Any?
    abstract fun arity(): Int

    class VoidFunction(
        private val statement: Stmt.FunctionStmt,
        private val environment: Environment
    ): VoidCallable() {
        override fun arity(): Int = statement.params.size

        override fun call(evaluator: Evaluator, arguments: List<Any?>): Any? {
            val scopeEnvironment = Environment(environment)

            statement.params.forEachIndexed { index, token ->
                scopeEnvironment.declare(token.lexeme, arguments.getOrNull(index))
            }

            evaluator.executeBlock(statement.body, scopeEnvironment)

            return null
        }

        override fun toString(): String = "Function ${statement.name.lexeme}"
    }
}