package me.otofune.void.cli

fun main(args: Array<String>) {
    if (args.size > 1 || args.isEmpty()) {
        return println("Usage: void [script]")
    }

    CodeExecutor.findAndExecuteFile(args[0])
}
