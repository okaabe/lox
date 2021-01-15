package me.otofune.void.grammar

sealed class FrontException(message: String) : VoidException("Front", message) {
    class UnexpectedChar(char: Char, line: Int) : FrontException("Unexpected char $char in line $line")
    class NotFinishedString(line: Int) : FrontException("String not finished in line $line")
    class ExpectedTokenButGot(expected: TokenType, got: TokenType, line: Int) : FrontException("Expected token $expected but got $got in line $line")
    class InvalidExpression(line: Int) : FrontException("Unexpected/Invalid expression in line $line")
    class InvalidAssignmentTarget(line: Int) : FrontException("Invalid assignment target in line $line")
    class ParamsLimitExceeded(line: Int): FrontException("Params limit exceeded in line $line, functions can't have more than $MAX_PARAM parameters")
}
