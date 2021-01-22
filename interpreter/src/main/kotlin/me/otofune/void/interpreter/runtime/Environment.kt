package me.otofune.void.interpreter.runtime

import me.otofune.void.interpreter.exceptions.LoxRuntimeException.*

class Environment(
    val tail: Environment? = null
) {
    private val declarations: MutableMap<String, Any?> = mutableMapOf()

    fun declare(name: String, value: Any?) {
        if (declarations.contains(name)) {
            throw AlreadyDeclared(name)
        }

        declarations[name] = value
    }

    fun get(name: String): Any? {
        if (declarations.contains(name)) return declarations[name]
        if (tail != null) return tail.get(name)

        throw ReferenceError(name)
    }

    fun assign(name: String, value: Any?) {
        when {
            declarations.contains(name) -> declarations[name] = value
            tail != null -> tail.assign(name, value)

            else -> {
                throw ReferenceError(name)
            }
        }
    }

    private fun ancestor(depth: Int): Environment {
        var environment = this

        for(i in 0 until depth) {
            environment = environment.tail!!
        }

        return environment
    }

    fun getAt(name: String, depth: Int): Any? {
        return ancestor(depth).get(name)
    }

    fun assignAt(name: String, depth: Int, value: Any?) {
        ancestor(depth).assign(name, value)
    }

    override fun toString(): String = "$declarations"
}
