package me.otofune.void.grammar

sealed class Expr {
    interface Visitor<T> {
        fun visitExpr(expr: Expr): T = expr.visit(this)
        fun visitBinaryExpr(expr: Binary) : T
        fun visitGroupingExpr(expr: Grouping): T
        fun visitUnaryExpr(expr: Unary): T
        fun visitLiteralExpr(expr: Literal): T
        fun visitVariableExpr(expr: Variable): T
        fun visitAssignExpr(expr: Assign): T
        fun visitCallExpr(expr: Call): T
        fun visitGetExpr(expr: Get): T
        fun visitSetExpr(expr: Set): T
        fun visitThisExpr(expr: This): T
        fun visitSuperExpr(expr: Super): T
    }

    abstract fun <T> visit(visitor: Visitor<T>): T

    data class Binary(
        val left: Expr,
        val op: Token,
        val right: Expr
    ) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitBinaryExpr(this)
    }

    data class Assign(
        val target: Variable,
        val value: Expr
    ): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitAssignExpr(this)
    }

    data class Call(
        val calle: Expr,
        val arguments: List<Expr>,
    ): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitCallExpr(this)
    }

    data class Unary(
        val op: Token,
        val right: Expr
    ) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitUnaryExpr(this)
    }

    data class Literal(val value: Any?): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitLiteralExpr(this)
    }

    data class Grouping(val expr: Expr) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitGroupingExpr(this)
    }

    data class Variable(val variable: Token): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitVariableExpr(this)
    }

    data class Get(
        val left: Expr,
        val property: Token
    ) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitGetExpr(this)
    }

    data class Set(
        val left: Expr,
        val property: Token,
        val value: Expr
    ): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitSetExpr(this)
    }

    data class This(
        val keyword: Token
    ): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitThisExpr(this)
    }

    data class Super(
        val keyword: Token,
        val property: Token
    ): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitSuperExpr(this)
    }
}
