plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // REST Assured
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:json-schema-validator:5.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    // Hamcrest для проверок
    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperties(System.getProperties().mapKeys { it.key.toString() })

    testLogging {
        lifecycle {
            events("started", "skipped", "failed", "standard_out", "standard_error")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }
}