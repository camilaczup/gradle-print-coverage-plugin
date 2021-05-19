import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    `maven`
    id("com.gradle.plugin-publish") version "0.10.1"
    id("com.jfrog.artifactory") version "4.9.10"
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

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

tasks {
    artifacts {
        add("archives", sourcesJar)
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://artifactory.local/artifactory")
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

artifactory {
    setProperty("contextUrl", "https://artifactory.local/artifactory")
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<groovy.lang.GroovyObject> {
            setProperty("repoKey", "libs-release-local")
            setProperty("username", System.getenv("ARTIFACTORY_USERNAME"))
            setProperty("password", System.getenv("ARTIFACTORY_PASSWORD"))
        })
        defaults(delegateClosureOf<groovy.lang.GroovyObject> {
            invokeMethod("publications", publishing.publications.names.toTypedArray())
        })
    })
}
