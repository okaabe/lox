package me.otofune.void.interpreter.runtime

import me.otofune.void.grammar.Expr
import me.otofune.void.grammar.Stmt
import me.otofune.void.grammar.Token
import me.otofune.void.grammar.TokenType
import me.otofune.void.interpreter.exceptions.LoxRuntimeException

import me.otofune.void.interpreter.runtime.util.checkNumberOperand
import me.otofune.void.interpreter.runtime.util.isEqual
import me.otofune.void.interpreter.runtime.util.isTruthy

class Evaluator(
    private var environment: Environment = Environment(),
    private val locals: MutableMap<Expr, Int>
): Expr.Visitor<Any?>, Stmt.Visitor<Any?> {
    override fun visitExprStmt(stmt: Stmt.ExprStmt): Any? {
        return visitExpr(stmt.expr)
    }

    override fun visitSuperExpr(expr: Expr.Super): Any? {
        val distance = locals[expr]?:return null

        val extendedClass = environment.getAt("super", distance) as LoxCallable.LoxClass
        val classObject = environment.getAt("this", distance - 1) as LoxObject

        val property = extendedClass.getMethod(expr.property.lexeme)
            ?:throw LoxRuntimeException.UndefinedProperty(expr.keyword.lexeme, expr.property.lexeme)

        return property.bind(classObject)
    }

    override fun visitThisExpr(expr: Expr.This): Any? = lookupVariable(expr, expr.keyword)

    override fun visitSetExpr(expr: Expr.Set): Any? {
        val obj = visitExpr(expr.left)

        if (obj !is LoxObject) {
            throw LoxRuntimeException.InvalidObjectGetProperty(expr.property, expr.property.line)
        }

        return visitExpr(expr.value).also { value ->
            obj.set(expr.property.lexeme, value)
        }
    }

    override fun visitGetExpr(expr: Expr.Get): Any? {
        val obj = visitExpr(expr.left)

        if (obj !is LoxObject) {
            throw LoxRuntimeException.InvalidObjectGetProperty(expr.property.lexeme, expr.property.line)
        }

        return obj.get(expr.property.lexeme)
    }

    override fun visitClassStmt(stmt: Stmt.ClassStmt) {
        val methods = mutableMapOf<String, LoxCallable.LoxFunction>()
        val extends: LoxCallable.LoxClass? = if (stmt.extends != null) {
            val visited = visitExpr(stmt.extends!!)

            if (visited !is LoxCallable.LoxClass) {
                throw LoxRuntimeException.InvalidExtendedClass(
                    stmt.name.lexeme,
                    stmt.extends!!.variable.lexeme
                )
            }

            visited
        } else null

        extends?.also {
            environment = Environment(environment)
            environment.declare("super", extends)
        }

        stmt.methods.map { method ->
            methods.put(method.name.lexeme, LoxCallable.LoxFunction(method, environment))
        }

        extends?.also {
            environment = environment.tail!!
        }

        environment.declare(stmt.name.lexeme, LoxCallable.LoxClass(stmt.name.lexeme, extends, methods))
    }

    override fun visitWhileStmt(stmt: Stmt.WhileStmt) {
        while(isTruthy(visitExpr(stmt.condition))) {
            visitStmt(stmt.body)
        }
    }

    override fun visitReturnStmt(stmt: Stmt.ReturnStmt): Any? = throw ReturnException(stmt.expression?.let { visitExpr(it) })

    override fun visitVarStmt(stmt: Stmt.VarStmt) {
        environment.declare(stmt.name.lexeme, visitExpr(stmt.value))
    }

    override fun visitIfStmt(stmt: Stmt.IfStmt) {
        if (isTruthy(visitExpr(stmt.condition))) {
            visitStmt(stmt.thenDo)
        } else stmt.elseDo?.also { visitStmt(it) }
    }

    override fun visitBlockStmt(stmt: Stmt.BlockStmt) = executeBlock(stmt.statements, Environment(environment))

    fun executeBlock(statements: List<Stmt>, scopeEnvironment: Environment) {
        val globalScope = environment.also {
            environment = scopeEnvironment
        }

        try {
            for(statement in statements) visitStmt(statement)
        } finally {
            environment = globalScope
        }
    }

    override fun visitFunctionStmt(stmt: Stmt.FunctionStmt) {
        environment.declare(stmt.name.lexeme, LoxCallable.LoxFunction(stmt, environment))
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        val left = visitExpr(expr.left)
        val right = visitExpr(expr.right)

        return when(expr.op.type) {
            TokenType.MINUS -> checkNumberOperand(expr.op, left) - checkNumberOperand(expr.op, right)
            TokenType.STAR -> checkNumberOperand(expr.op, left) * checkNumberOperand(expr.op, right)
            TokenType.SLASH -> checkNumberOperand(expr.op, left) / checkNumberOperand(expr.op, right)
            TokenType.PLUS -> when {
                left is String && right is String -> left + right
                else -> checkNumberOperand(expr.op, left) + checkNumberOperand(expr.op, right)
            }
            TokenType.LESS -> checkNumberOperand(expr.op, left) < checkNumberOperand(expr.op, right)
            TokenType.GREATER -> checkNumberOperand(expr.op, left) > checkNumberOperand(expr.op, right)
            TokenType.LESS_EQUAL -> checkNumberOperand(expr.op, left) <= checkNumberOperand(expr.op, right)
            TokenType.GREATER_EQUAL -> checkNumberOperand(expr.op, left) >= checkNumberOperand(expr.op, right)

            TokenType.EQUAL_EQUAL -> isEqual(left, right)
            TokenType.BANG_EQUAL -> !isEqual(left, right)

            else -> null
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? = visitExpr(expr.expr)

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = visitExpr(expr.right)

        return when(expr.op.type) {
            TokenType.MINUS -> -checkNumberOperand(expr.op, right)
            TokenType.BANG -> !isTruthy(right)
            else -> null
        }
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? = expr.value

    override fun visitVariableExpr(expr: Expr.Variable): Any? = lookupVariable(expr, expr.variable)

    override fun visitAssignExpr(expr: Expr.Assign): Any? = visitExpr(expr.value).also { value ->
        environment.assign(expr.target.variable.lexeme, value)
    }

    override fun visitCallExpr(expr: Expr.Call): Any? {
        val calle = visitExpr(expr.calle)
        val arguments = mutableListOf<Any?>()

        for (argument in expr.arguments) {
            arguments.add(visitExpr(argument))
        }

        if (calle !is LoxCallable) {
            throw LoxRuntimeException.InvalidCalle(expr.calle)
        }

        if (arguments.size != calle.arity()) {
            throw LoxRuntimeException.InvalidArgumentsAmount(calle.arity(), arguments.size)
        }

        return calle.call(this, arguments)
    }

    private fun lookupVariable(expr: Expr, name: Token): Any? {
        val distance = locals[expr]

        return if (distance != null) {
            environment.getAt(name.lexeme, distance)
        } else {
            environment.get(name.lexeme)
        }
    }
}