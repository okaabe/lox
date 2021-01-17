package me.otofune.void.interpreter.resolver

import me.otofune.void.grammar.Expr
import me.otofune.void.grammar.Stmt
import me.otofune.void.interpreter.Interpreter

class Resolver(
    private val interpreter: Interpreter,
): Stmt.Visitor<Any?>, Expr.Visitor<Any?> {
    fun resolve(statements: List<Stmt>) = statements.map {
        visitStmt(it)
    }

    override fun visitBlockStmt(stmt: Stmt.BlockStmt) {
        TODO("Not yet implemented")
    }

    override fun visitExprStmt(stmt: Stmt.ExprStmt) {
        TODO("Not yet implemented")
    }

    override fun visitVarStmt(stmt: Stmt.VarStmt) {
        TODO("Not yet implemented")
    }

    override fun visitIfStmt(stmt: Stmt.IfStmt) {
        TODO("Not yet implemented")
    }

    override fun visitFunctionStmt(stmt: Stmt.FunctionStmt) {
        TODO("Not yet implemented")
    }

    override fun visitReturnStmt(stmt: Stmt.ReturnStmt) {
        TODO("Not yet implemented")
    }

    override fun visitWhileStmt(stmt: Stmt.WhileStmt) {
        TODO("Not yet implemented")
    }

    override fun visitAssignExpr(expr: Expr.Assign) {
        TODO("Not yet implemented")
    }

    override fun visitBinaryExpr(expr: Expr.Binary) {
        TODO("Not yet implemented")
    }

    override fun visitCallExpr(expr: Expr.Call) {
        TODO("Not yet implemented")
    }

    override fun visitGroupingExpr(expr: Expr.Grouping) {
        TODO("Not yet implemented")
    }

    override fun visitLiteralExpr(expr: Expr.Literal) {
        TODO("Not yet implemented")
    }

    override fun visitUnaryExpr(expr: Expr.Unary) {
        TODO("Not yet implemented")
    }

    override fun visitVariableExpr(expr: Expr.Variable) {
        TODO("Not yet implemented")
    }
}