package me.otofune.void.interpreter.runtime

import me.otofune.void.grammar.Stmt

sealed class VoidCallable {
    abstract fun call(evaluator: Evaluator, arguments: List<Any?>): Any?
    abstract fun arity(): Int

    data class VoidFunction(
        private val statement: Stmt.FunctionStmt,
        private val environment: Environment
    ): VoidCallable() {
        override fun arity(): Int = statement.params.size

        override fun call(evaluator: Evaluator, arguments: List<Any?>): Any? {
            val scopeEnvironment = Environment(environment)

            statement.params.forEachIndexed { index, token ->
                scopeEnvironment.declare(token.lexeme, arguments.getOrNull(index))
            }


            try {
                evaluator.executeBlock(statement.body, scopeEnvironment)
            } catch(returnException: ReturnException) {
                return returnException.value
            }

            return null
        }

        override fun toString(): String = "[Function ${statement.name.lexeme}]"
    }

    data class VoidNativeFunction(
        val name: String,
        private val paramsSize: Int,
        private val callback: (args: List<Any?>) -> Any?
    ): VoidCallable() {
        override fun arity(): Int = paramsSize

        override fun call(evaluator: Evaluator, arguments: List<Any?>): Any? {
            return callback(arguments)
        }

        override fun toString(): String = "[Native Function $name]"
    }
}