import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // 🚀 Поднимаем версию Kotlin до 2.3.0, чтобы соответствовать jda-ktx 0.14.0
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "8.3.5"
    application
}

group = "dodia.novshield.discordbot"
version = "1.0.0"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:6.4.1") {
        exclude(module = "opus-java")
    }
    implementation("club.minnced:jda-ktx:0.14.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
}

application {
    mainClass.set("dodia.novshield.discordbot.MainKt")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("novshield-bot")
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())
    mergeServiceFiles()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}