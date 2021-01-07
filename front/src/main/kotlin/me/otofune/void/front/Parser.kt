package me.otofune.void.front

class Parser(
    private val tokens: List<Token>
) {
    private var actual = 0
    private val isAtEnd get() = peek().type == TokenType.EOF

    fun parse(): List<Stmt> {
        val stmts = mutableListOf<Stmt>()

        while(!isAtEnd) {
            stmts.add(statement())
        }

        return stmts
    }

    private fun statement(): Stmt {
        return when(peek().type) {
            TokenType.VAR -> varDecl()
            else -> Stmt.ExprStmt(expression())
        }
    }

    private fun varDecl(): Stmt {
        advance()
        val name = consume(TokenType.IDENTIFER)
        consume(TokenType.EQUAL)
        val value = expression()

        return Stmt.VarDeclStmt(name, value)
    }

    private fun expression(): Expr = equality()

    private fun equality(): Expr {
        var left = comparison()

        while(match(TokenType.EQUAL, TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            val op = previous()
            val right = comparison()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun comparison(): Expr {
        var left = term()

        while(match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            val op = previous()
            val right = term()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun term(): Expr {
        var left = factor()

        while(match(TokenType.PLUS, TokenType.MINUS)) {
            val op = previous()
            val right = factor()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun factor(): Expr {
        var left = unary()

        while(match(TokenType.STAR, TokenType.SLASH)) {
            val op = previous()
            val right = unary()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun unary(): Expr {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val op = previous()
            val right = primary()

            return Expr.Unary(op, right)
        }

        return primary()
    }

    private fun primary(): Expr = when {
        match(TokenType.FALSE) -> Expr.Literal(false)
        match(TokenType.TRUE) -> Expr.Literal(true)
        match(TokenType.STRING) -> Expr.Literal(previous().literal)
        match(TokenType.NUMBER) -> Expr.Literal(previous().literal)
        match(TokenType.LEFT_PAREN) -> Expr.Grouping(expression().also {
            consume(TokenType.RIGHT_PAREN)
        })
        match(TokenType.IDENTIFER) -> Expr.Literal(previous().lexeme)

        else -> {
            throw FrontException.InvalidExpression(previous().line)
        }
    }

    private fun consume(type: TokenType): Token {
        if (check(type)) return advance()

        throw FrontException.ExpectedTokenButGot(type, peek().type, peek().line)
    }

    private fun match(vararg types: TokenType): Boolean {
        for(type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd) return false

        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd) actual++
        return previous()
    }

    private fun previous(): Token = tokens[actual - 1]
    private fun peek(): Token = tokens[actual]
}

fun main() {
    kotlin.runCatching {
        val tokens = Lexer("var test = 2").scan()
        val parser = Parser(tokens)

        tokens.map { println(it) }
        parser.parse().map { println(it) }
    }.exceptionOrNull()?.also { throwable ->
        if (throwable is VoidException) {
            throwable.report()
        } else {
            println(throwable.message)
        }
    }
}