package me.otofune.void.grammar

private val RESERVED_KEYWORDS = mapOf(
    "class" to TokenType.CLASS,
    "fn" to TokenType.FN,
    "true" to TokenType.TRUE,
    "false" to TokenType.FALSE,
    "var" to TokenType.VAR,
    "return" to TokenType.RETURN,
    "while" to TokenType.WHILE,
    "for" to TokenType.FOR,
    "if" to TokenType.IF,
    "super" to TokenType.SUPER,
    "else" to TokenType.ELSE,
    "nil" to TokenType.NIL,
    "this" to TokenType.THIS
)

class Lexer(
    private val code: String
) {
    private val tokens: MutableList<Token> = mutableListOf()

    private var current= 0
    private var start = 0
    private var line = 1

    private val isAtEnd get() = current >= code.length

    fun scan(): List<Token> {
        while(!isAtEnd) {
            start = current
            scanSingle()
        }

        addToken(TokenType.EOF)

        return tokens
    }

    private fun scanSingle() {
        when(val char = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)

            '+' -> addToken(TokenType.PLUS)
            '-' -> addToken(TokenType.MINUS)
            '/' -> if (match('/')) consumeComment() else addToken(TokenType.SLASH)
            '*' -> addToken(TokenType.STAR)

            '=' -> if (match('=')) addToken(TokenType.EQUAL_EQUAL) else addToken(TokenType.EQUAL)
            '>' -> if (match('=')) addToken(TokenType.GREATER_EQUAL) else addToken(TokenType.GREATER)
            '<' -> if (match('=')) addToken(TokenType.LESS_EQUAL) else addToken(TokenType.LESS)
            '!' -> if (match('=')) addToken(TokenType.BANG_EQUAL) else addToken(TokenType.BANG)

            ' ', '\r', ';' -> return
            '\n' -> line++

            '"' -> consumeString()

            '&' -> if (match('&')) addToken(TokenType.AND) else throw LoxGrammarException.UnexpectedChar(char, line)
            '|' -> if (match('|')) addToken(TokenType.OR) else throw LoxGrammarException.UnexpectedChar(char, line)

            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)

            else -> {
                if (isValidStartOfIdentifer(char)) return consumeIdentifer()
                if (isDigit(char)) return consumeNumber()
            }
        }
    }

    private fun isValidStartOfIdentifer(char: Char?): Boolean = char?.toLowerCase() in 'a' .. 'z'
    private fun isDigit(char: Char?): Boolean = char in '0' .. '9'

    private fun consumeNumber() {
        while(isDigit(code.getOrNull(current)) && !isAtEnd) {
            advance()
        }

        addToken(TokenType.NUMBER, code.substring(start, current).toDouble())
    }

    private fun consumeString() {
        while(code.getOrNull(current) != '"' && !isAtEnd) {
            advance()
        }

        if (isAtEnd) {
            throw LoxGrammarException.NotFinishedString(line)
        }

        advance()
        addToken(TokenType.STRING, code.substring(start + 1, current - 1))
    }

    private fun consumeIdentifer() {
        while(isValidStartOfIdentifer(code.getOrNull(current)) && !isAtEnd) {
            advance()
        }

        addToken(RESERVED_KEYWORDS.getOrElse(code.substring(start, current)) {
            TokenType.IDENTIFER
        })
    }

    private fun consumeComment() {
        while(!match('\n') && !isAtEnd) {
            advance()
        }

        line++
    }

    private fun advance(): Char {
        current++
        return code[current - 1]
    }

    private fun match(char: Char): Boolean {
        if (isAtEnd) return false
        if (code.getOrNull(current) == char) {
            advance()
            return true
        }

        return false
    }

    private fun addToken(type: TokenType) = addToken(type, null)
    private fun addToken(type: TokenType, literal: Any?) {
        tokens.add(Token(type, code.substring(start, current), literal, line))
    }
}
