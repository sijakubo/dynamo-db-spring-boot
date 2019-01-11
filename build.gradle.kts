import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    groovy
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("org.springframework.boot") version "2.0.3.RELEASE"
}

repositories {
    mavenCentral()
    maven(url = "https://s3.eu-central-1.amazonaws.com/dynamodb-local-frankfurt/release")
}

description = "DynamoDB / Spring Boot / Spock Sample Application"
group = "de.sja"
version = "0.1 - SNAPSHOT"

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-logging")

    compile("com.amazonaws:aws-java-sdk-dynamodb:1.11.481")

    // just for convenience
    compile("com.google.guava:guava:27.0.1-jre")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("org.spockframework:spock-core:1.1-groovy-2.4")
    testCompile("org.spockframework:spock-spring:1.1-groovy-2.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    getByName<BootRun>("bootRun") {
        System.getProperty("spring.profiles.active", System.getenv("SRING_PROFILES_ACTIVE") ?: "dev")
    }
}
