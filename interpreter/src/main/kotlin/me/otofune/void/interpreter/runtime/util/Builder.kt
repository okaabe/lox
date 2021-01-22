package me.otofune.void.interpreter.runtime.util

import me.otofune.void.interpreter.runtime.Environment
import me.otofune.void.interpreter.runtime.LoxCallable

fun Environment.declareFunction(name: String, params: Int = 0, callback: (args: List<Any?>) -> Any?) = declare(name, LoxCallable.LoxNativeFunction(
    name, params, callback
))
