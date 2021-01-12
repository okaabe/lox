package me.otofune.void.interpreter.runtime

import me.otofune.void.interpreter.exceptions.VoidRuntimeException.*

class Environment() {
    private val variables: MutableMap<String, Any?> = mutableMapOf()

    fun declare(name: String, value: Any?) {
        if (variables.contains(name)) {
            throw AlreadyDeclared(name)
        }

        variables[name] = value
    }

    fun get(name: String): Any? {
        if (variables.contains(name)) return variables[name]

        throw ReferenceError(name)
    }

    fun assign(name: String, value: Any?) {
        get(name).also {
            variables[name] = value
        }
    }
}
