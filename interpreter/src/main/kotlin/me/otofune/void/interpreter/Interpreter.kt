package me.otofune.void.interpreter

import me.otofune.void.grammar.Expr
import me.otofune.void.grammar.Stmt
import me.otofune.void.interpreter.resolver.Resolver
import me.otofune.void.interpreter.runtime.Builtin
import me.otofune.void.interpreter.runtime.Environment
import me.otofune.void.interpreter.runtime.Evaluator

class Interpreter {
    private val environment = Environment().also {
        Builtin(it).setup()
    }
    private val locals: MutableMap<Expr, Int> = mutableMapOf()

    fun resolve(expr: Expr, depth: Int) {
        locals[expr] = depth
    }

    fun interpret(statements: List<Stmt>) {
        Resolver(this).resolve(statements)

        val evaluator = Evaluator(environment, locals)

        statements.map { statement ->
            evaluator.visitStmt(statement)
        }
    }
}
