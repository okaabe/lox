plugins {
    application
    kotlin("jvm")
    java
}

group = "me.otofune.void.cli"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":front"))
    implementation(project(":interpreter"))
}