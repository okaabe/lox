package me.otofune.void.front

sealed class FrontException(message: String) : VoidException("Front", message) {
    class UnexpectedChar(char: Char, line: Int) : FrontException("Unexpected char $char in line $line")
    class NotFinishedString(line: Int) : FrontException("String not finished in line $line")
    class UnexpectedBreakLineString(line: Int) : FrontException("Unexpected \\n in a string in line $line")
}
