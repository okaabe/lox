package me.otofune.void.grammar

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
            TokenType.VAR -> parseVarDeclaration()
            TokenType.IF -> parseIfDeclaration()
            TokenType.WHILE -> parseWhileDeclaration()
            TokenType.FN -> parseFunctionDeclaration()
            TokenType.RETURN -> parseReturnStatement()
            TokenType.LEFT_BRACE -> Stmt.BlockStmt(parseStatementsBlock())

            else -> Stmt.ExprStmt(parseExpression())
        }
    }

    private fun parseWhileDeclaration(): Stmt {
        advance()

        consume(TokenType.LEFT_PAREN)
        val condition = parseExpression()
        consume(TokenType.RIGHT_PAREN)

        return Stmt.WhileStmt(condition, statement())
    }

    private fun parseReturnStatement(): Stmt {
        advance()

        val value = if (!match(TokenType.SEMICOLON)) {
            parseExpression()
        } else null

        consume(TokenType.SEMICOLON)

        return Stmt.ReturnStmt(value)
    }

    private fun parseFunctionDeclaration(): Stmt {
        advance()
        val name = consume(TokenType.IDENTIFER)

        consume(TokenType.LEFT_PAREN)

        val parameters = consumeParameters()

        consume(TokenType.RIGHT_PAREN)
        consume(TokenType.LEFT_BRACE)

        val body = parseStatementsBlock()
        return Stmt.FunctionStmt(name, parameters, body)
    }

    private fun consumeParameters(): List<Token> {
        val parameters = mutableListOf<Token>()
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (parameters.size > MAX_PARAM) {
                    throw GrammarException.ParamsLimitExceeded(previous().line)
                }

                parameters.add(consume(TokenType.IDENTIFER))
            } while(match(TokenType.COMMA))
        }

        return parameters
    }

    private fun parseStatementsBlock(): List<Stmt> {
        val statements = mutableListOf<Stmt>()

        if (check(TokenType.LEFT_BRACE)) {
            advance()
        }

        while(!check(TokenType.RIGHT_BRACE) && !isAtEnd) {
            statements.add(statement())
        }

        consume(TokenType.RIGHT_BRACE)
        return statements
    }

    private fun parseIfDeclaration(): Stmt {
        advance()
        consume(TokenType.LEFT_PAREN)
        val condition = parseExpression()//($expr)

        consume(TokenType.RIGHT_PAREN)
        val thenDo = statement()

        return when {
            match(TokenType.ELSE) -> Stmt.IfStmt(condition, thenDo, statement())
            else -> Stmt.IfStmt(condition, thenDo)
        }
    }

    private fun parseVarDeclaration(): Stmt {
        advance()
        val name = consume(TokenType.IDENTIFER)
        consume(TokenType.EQUAL)
        val value = parseExpression()

        return Stmt.VarStmt(name, value)
    }

    private fun parseExpression(): Expr = parseAssignmentExpression()

    private fun parseAssignmentExpression(): Expr {
        val left = parseEqualityExpression()

        if (match(TokenType.EQUAL)) {
            val value = parseAssignmentExpression()

            if (left is Expr.Variable) {
                return Expr.Assign(left, value)
            }

            throw GrammarException.InvalidAssignmentTarget(previous().line)
        }

        return left
    }

    private fun parseEqualityExpression(): Expr {
        var left = parseComparisonExpression()

        while(match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            val op = previous()
            val right = parseComparisonExpression()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun parseComparisonExpression(): Expr {
        var left = parseTermExpression()

        while(match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            val op = previous()
            val right = parseTermExpression()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun parseTermExpression(): Expr {
        var left = parseFactorExpression()

        while(match(TokenType.PLUS, TokenType.MINUS)) {
            val op = previous()
            val right = parseFactorExpression()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun parseFactorExpression(): Expr {
        var left = parseUnaryExpression()

        while(match(TokenType.STAR, TokenType.SLASH)) {
            val op = previous()
            val right = parseUnaryExpression()

            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun parseUnaryExpression(): Expr {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val op = previous()
            val right = parseCallExpression()

            return Expr.Unary(op, right)
        }

        return parseCallExpression()
    }

    private fun parseCallExpression(): Expr {
        var left = parsePrimaryExpression()

        while(true) left = when {
            match(TokenType.LEFT_PAREN) -> finishCall(left)
            else -> break
        }

        return left
    }

    private fun parsePrimaryExpression(): Expr = when {
        match(TokenType.FALSE) -> Expr.Literal(false)
        match(TokenType.TRUE) -> Expr.Literal(true)
        match(TokenType.STRING) -> Expr.Literal(previous().literal)
        match(TokenType.NUMBER) -> Expr.Literal(previous().literal)
        match(TokenType.NIL) -> Expr.Literal(null)
        match(TokenType.LEFT_PAREN) -> Expr.Grouping(parseExpression().also {
            consume(TokenType.RIGHT_PAREN)
        })
        match(TokenType.IDENTIFER) -> Expr.Variable(previous())

        else -> {
            throw GrammarException.InvalidExpression(previous().line)
        }
    }

    private fun finishCall(left: Expr): Expr {
        val arguments = mutableListOf<Expr>()
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(parseExpression())
            } while(match(TokenType.COMMA))
        }

        consume(TokenType.RIGHT_PAREN)

        return Expr.Call(left, arguments)
    }

    private fun consume(type: TokenType): Token {
        if (check(type)) return advance()

        throw GrammarException.ExpectedTokenButGot(type, peek().type, peek().line)
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
