package me.otofune.void.interpreter.resolver

import me.otofune.void.grammar.Expr
import me.otofune.void.grammar.Stmt
import me.otofune.void.grammar.Token
import me.otofune.void.interpreter.Interpreter
import me.otofune.void.interpreter.exceptions.LoxResolverException
import java.util.*

class Resolver(
    private val interpreter: Interpreter,
): Stmt.Visitor<Any?>, Expr.Visitor<Any?> {
    private val scopes: Stack<MutableMap<String, Boolean>> = Stack()
    private var location: Location = Location.NONE

    fun resolve(statements: List<Stmt>) = statements.map {
        visitStmt(it)
    }

    override fun visitThisExpr(expr: Expr.This) {
        if (location != Location.METHOD) {
            throw LoxResolverException.IllegalThisStatement(expr.keyword.line)
        }

        resolveLocal(expr, expr.keyword)
    }

    override fun visitGetExpr(expr: Expr.Get) {
        visitExpr(expr.left)
    }

    override fun visitSetExpr(expr: Expr.Set) {
        visitExpr(expr.value)
        visitExpr(expr.left)
    }

    override fun visitClassStmt(stmt: Stmt.ClassStmt) {
        declare(stmt.name)
        define(stmt.name)

        beginScope()

        scopes.peek()["this"] = true

        stmt.methods.map { method ->
            resolveFunction(method, Location.METHOD)
        }

        endScope()
    }

    override fun visitBlockStmt(stmt: Stmt.BlockStmt) {
        beginScope()
        resolve(stmt.statements)
        endScope()
    }

    override fun visitExprStmt(stmt: Stmt.ExprStmt) = visitExpr(stmt.expr)

    override fun visitVarStmt(stmt: Stmt.VarStmt) {
        declare(stmt.name)
        visitExpr(stmt.value)
        define(stmt.name)
    }

    override fun visitIfStmt(stmt: Stmt.IfStmt) {
        visitExpr(stmt.condition)
        visitStmt(stmt.thenDo)
        visitStmt(stmt.elseDo!!)
    }

    override fun visitFunctionStmt(stmt: Stmt.FunctionStmt) {
        declare(stmt.name)
        define(stmt.name)

        resolveFunction(stmt, Location.FUNCTION)
    }

    override fun visitReturnStmt(stmt: Stmt.ReturnStmt) {
        if (location == Location.NONE) {
            throw LoxResolverException.IllegalReturnStatement()
        }

        visitExpr(stmt.expression!!)
    }

    override fun visitWhileStmt(stmt: Stmt.WhileStmt) {
        visitExpr(stmt.condition)
        visitStmt(stmt.body)
    }

    override fun visitAssignExpr(expr: Expr.Assign) {
        visitExpr(expr.value)
        resolveLocal(expr, expr.target.variable)
    }

    override fun visitBinaryExpr(expr: Expr.Binary) {
        visitExpr(expr.left)
        visitExpr(expr.right)
    }

    override fun visitCallExpr(expr: Expr.Call) {
        visitExpr(expr.calle)

        expr.arguments.map { arg ->
            visitExpr(arg)
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping) = visitExpr(expr.expr)

    override fun visitLiteralExpr(expr: Expr.Literal) {}

    override fun visitUnaryExpr(expr: Expr.Unary) = visitExpr(expr.right)

    override fun visitVariableExpr(expr: Expr.Variable) {
        if (!scopes.isEmpty() && scopes.peek()[expr.variable.lexeme] == false) {
            throw LoxResolverException.IllegalVariableInitializer(expr.variable.lexeme, expr.variable.line)
        }

        resolveLocal(expr, expr.variable)
    }

    private fun resolveLocal(expr: Expr, name: Token) {
        for(i in scopes.size - 1 downTo 0) {
            if (scopes[i].containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size - 1 - i)
            }
        }
    }

    private fun resolveFunction(stmt: Stmt.FunctionStmt, loc: Location) {
        val enclosingLocation = this.location
        location = loc

        beginScope()

        stmt.params.map {
            declare(it)
            define(it)
        }

        resolve(stmt.body)

        endScope()
        location = enclosingLocation
    }

    private fun declare(name: Token) {
        if (scopes.isEmpty()) {
            return
        }

        scopes.peek().also {
            it[name.lexeme] = false
        }
    }

    private fun define(name: Token) {
        if (scopes.isEmpty()) {
            return
        }

        scopes.peek().also {
            it[name.lexeme] = true
        }
    }

    private fun beginScope() = scopes.push(mutableMapOf())
    private fun endScope() = scopes.pop()
}
