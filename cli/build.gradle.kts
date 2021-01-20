plugins {
    application
    java
    kotlin("jvm")
}


group = "me.otofune.void.cli"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("me.otofune.void.cli.ApplicationKt")
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.1.0")
    api(project(":grammar"))
    api(project(":interpreter"))
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "me.otofune.void.cli.ApplicationKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
}