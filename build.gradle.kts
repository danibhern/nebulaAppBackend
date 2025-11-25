import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20" // Para @Entity en Kotlin
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    // AÑADIDO: Configuración de Toolchain para forzar el uso de Java 17,
    // resolviendo la incompatibilidad con JDK 25
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Core y Web (para crear APIs REST)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JPA (para la ORM y persistencia en DB)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Base de datos en memoria H2 (ideal para desarrollo y pruebas)
    runtimeOnly("com.h2database:h2")

    // Módulos de Kotlin necesarios para Spring
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Pruebas
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(kotlin("test")) // Añadir esta línea es una buena práctica para pruebas
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}