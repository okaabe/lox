package me.otofune.void.interpreter.runtime

import me.otofune.void.interpreter.exceptions.VoidRuntimeException
import me.otofune.void.interpreter.exceptions.VoidRuntimeException.*

class Environment(
    private val tail: Environment? = null
) {
    private val variables: MutableMap<String, Any?> = mutableMapOf()

    fun declare(name: String, value: Any?) {
        if (variables.contains(name)) {
            throw AlreadyDeclared(name)
        }

        variables[name] = value
    }

    fun get(name: String): Any? {
        if (variables.contains(name)) return variables[name]
        if (tail != null) return tail.get(name)

        throw ReferenceError(name)
    }

    fun assign(name: String, value: Any?) {
        when {
            variables.contains(name) -> variables[name] = value
            tail != null -> tail.assign(name, value)

            else -> {
                throw ReferenceError(name)
            }
        }
    }
}
