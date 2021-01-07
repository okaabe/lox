import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.otofune"
version = "1.0-SNAPSHOT"

allprojects {
    plugins.apply("kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        testImplementation("org.junit.jupiter:junit.jupiter-api:5.6.0")
        testRuntimeOnly("org.junit.jupiter:junit-junit-jupiter-engine")
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}