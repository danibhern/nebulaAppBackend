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
    // 1. Establecemos la compatibilidad del código fuente y destino a Java 21 (LTS)
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    // 2. CONFIGURACIÓN CRÍTICA: Definir el Toolchain
    // Esto le dice a Gradle que descargue y use automáticamente un JDK 21
    // para compilar, ignorando la versión 25 instalada en el sistema.
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
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

    // JJWT API
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")

    // JJWT Implementation (Necesario en tiempo de ejecución)
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")

    // JJWT Jackson (Necesario para el parseo de Claims)
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    runtimeOnly("mysql:mysql-connector-java:8.0.33")


    // Pruebas
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(kotlin("test")) // Añadir esta línea es una buena práctica para pruebas
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.3.0")

    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        // 3. Establecemos el target JVM también a 21
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}