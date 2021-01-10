package me.otofune.void.front

sealed class FrontException(message: String) : VoidException("Front", message) {
    class UnexpectedChar(char: Char, line: Int) : FrontException("Unexpected char $char in line $line")
    class NotFinishedString(line: Int) : FrontException("String not finished in line $line")
    class ExpectedTokenButGot(expected: TokenType, got: TokenType, line: Int) : FrontException("Expected token $expected but got $got in line $line")
    class InvalidExpression(line: Int) : FrontException("Unexpected/Invalid expression in line $line")
}
