package me.otofune.void.grammar

sealed class LoxGrammarException(message: String) : LoxException("Grammar", message) {
    class UnexpectedChar(char: Char, line: Int) : LoxGrammarException("Unexpected char $char in line $line")
    class NotFinishedString(line: Int) : LoxGrammarException("String not finished in line $line")
    class ExpectedTokenButGot(expected: TokenType, got: TokenType, line: Int) : LoxGrammarException("Expected token $expected but got $got in line $line")
    class InvalidExpression(line: Int) : LoxGrammarException("Unexpected/Invalid expression in line $line")
    class InvalidAssignmentTarget(line: Int) : LoxGrammarException("Invalid assignment target in line $line")
    class ParamsLimitExceeded(line: Int): LoxGrammarException("Params limit exceeded in line $line, functions can't have more than $MAX_PARAM parameters")
}
