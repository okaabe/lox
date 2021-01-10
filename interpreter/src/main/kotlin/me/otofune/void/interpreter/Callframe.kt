package me.otofune.void.interpreter

class Callframe {
    private val frames: MutableMap<String, Any?> = mutableMapOf()

    fun declareVar(name: String, value: Any?) {
        if (frames.contains(name)) {
            throw VoidRuntimeException.HasAlreadyBeenDeclared(name)
        }

        frames[name] = value
    }

    fun getAndCheckExistence(name: String): Any? {
        if (!frames.contains(name)) {
            throw VoidRuntimeException.ReferenceError(name)
        }

        return frames[name]
    }

    fun assignAndCheckExistence(name: String, newValue: Any?) {
        getAndCheckExistence(name)
        frames[name] = newValue
    }
}