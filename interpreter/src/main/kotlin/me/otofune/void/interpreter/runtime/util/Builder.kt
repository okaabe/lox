package me.otofune.void.interpreter.runtime.util

import me.otofune.void.interpreter.runtime.Environment
import me.otofune.void.interpreter.runtime.VoidCallable

fun Environment.declareFunction(name: String, params: Int, callback: (args: List<Any?>) -> Any?) = declare(name, VoidCallable.VoidNativeFunction(
    name, params, callback
))
