import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

description = "Gradle print coverage status plugin"
group = "jacoco.printcoveragestatus"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.20")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

gradlePlugin {
    plugins {
        register("jacocoStatusPrinter") {
            id = "jacoco.printcoveragestatus"
            implementationClass = "jacoco.printcoveragestatus.PrintCoverageStatusPlugin"
        }
    }
}
