package me.otofune.void.grammar

sealed class GrammarException(message: String) : VoidException("Grammar", message) {
    class UnexpectedChar(char: Char, line: Int) : GrammarException("Unexpected char $char in line $line")
    class NotFinishedString(line: Int) : GrammarException("String not finished in line $line")
    class ExpectedTokenButGot(expected: TokenType, got: TokenType, line: Int) : GrammarException("Expected token $expected but got $got in line $line")
    class InvalidExpression(line: Int) : GrammarException("Unexpected/Invalid expression in line $line")
    class InvalidAssignmentTarget(line: Int) : GrammarException("Invalid assignment target in line $line")
    class ParamsLimitExceeded(line: Int): GrammarException("Params limit exceeded in line $line, functions can't have more than $MAX_PARAM parameters")
}
