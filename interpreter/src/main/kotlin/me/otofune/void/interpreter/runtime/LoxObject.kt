package me.otofune.void.interpreter.runtime

import me.otofune.void.interpreter.exceptions.LoxRuntimeException

class LoxObject(
    private val loxClass: LoxCallable.LoxClass
) {
    private val fields: MutableMap<String, Any?> = mutableMapOf()

    fun get(name: String): Any? {
        val method = loxClass.getMethod(name)

        if (method != null) {
            return method.bind(this)
        }

        return fields[name]?:throw LoxRuntimeException.UndefinedProperty(loxClass.name, name)
    }

    fun set(name: String, value: Any?) {
        fields[name] = value
    }

    override fun toString(): String = "[${loxClass.name} instance]"
}