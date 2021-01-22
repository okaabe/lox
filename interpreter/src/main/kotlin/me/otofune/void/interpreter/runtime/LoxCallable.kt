package me.otofune.void.interpreter.runtime

import me.otofune.void.grammar.Stmt

sealed class LoxCallable {
    abstract fun call(evaluator: Evaluator, arguments: List<Any?>): Any?
    abstract fun arity(): Int

    data class LoxFunction(
        private val statement: Stmt.FunctionStmt,
        private val closure: Environment
    ): LoxCallable() {
        override fun arity(): Int = statement.params.size

        override fun call(evaluator: Evaluator, arguments: List<Any?>): Any? {
            val scopeEnvironment = Environment(closure)

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

        fun bind(obj: LoxObject): LoxFunction {
            val environment = Environment(closure)

            environment.declare("this", obj)

            return LoxFunction(statement, environment)
        }

        override fun toString(): String = "[Function ${statement.name.lexeme}]"
    }

    data class LoxNativeFunction(
        val name: String,
        private val paramsSize: Int,
        private val callback: (args: List<Any?>) -> Any?
    ): LoxCallable() {
        override fun arity(): Int = paramsSize

        override fun call(evaluator: Evaluator, arguments: List<Any?>): Any? {
            return callback(arguments)
        }

        override fun toString(): String = "[Native Function $name]"
    }

    data class LoxClass(
        val name: String,

        private val methods: MutableMap<String, LoxFunction>,
    ): LoxCallable() {
        override fun arity(): Int {
            val method = getMethod("init")

            return if (method != null) {
                method.arity()
            } else {
                0
            }
        }

        override fun call(evaluator: Evaluator, arguments: List<Any?>): Any? {
            val instance = LoxObject(this)

            getMethod("init")?.also { initializer ->
                initializer.bind(instance).call(evaluator, arguments)
            }

            return instance
        }

        fun getMethod(name: String): LoxFunction? = methods[name]

        override fun toString(): String = "[Class $name]"
    }
}
