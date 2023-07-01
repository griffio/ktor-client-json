plugins {
    application
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("griffio.client.ApplicationKt")
}

dependencies {
    implementation("io.ktor:ktor-client-core-jvm:2.2.4")
    implementation("io.ktor:ktor-client-java:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
    // a recent version of kotlinx-serialization-json will be pulled in
    // uncomment below to specify exact version
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.22")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
