plugins {
    java
    kotlin("jvm")
}

group = "me.otofune.void.interpreter"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":grammar"))
}