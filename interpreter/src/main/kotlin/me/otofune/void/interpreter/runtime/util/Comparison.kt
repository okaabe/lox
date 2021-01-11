package me.otofune.void.interpreter.runtime.util

fun isTruthy(right: Any?): Boolean = when (right) {
    null -> false
    is Boolean -> right.toString().toBoolean()
    else -> true
}

fun isEqual(left: Any?, right: Any?): Boolean = when {
    left == null && right == null -> true
    left == null -> false
    else -> left == right
}