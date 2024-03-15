import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("java")
  id("org.springframework.boot") version "3.2.3"
  id("io.spring.dependency-management") version "1.1.4"
  id("org.hibernate.orm") version "6.4.4.Final"
  id("org.graalvm.buildtools.native") version "0.9.28"
  kotlin("jvm") version "1.9.22"
  kotlin("plugin.spring") version "1.9.22"
  kotlin("plugin.jpa") version "1.9.22"
}

group = "bo.mdia"

version = "0.0.1-SNAPSHOT"

java { sourceCompatibility = JavaVersion.VERSION_21 }

repositories { mavenCentral() }

val kordVersion = "0.13.1"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
    exclude(group = "ch.qos.logback")
  }
  implementation("org.springframework.boot:spring-boot-starter") {
    exclude(group = "ch.qos.logback")
  }

  implementation("io.klogging:klogging-spring-boot-starter:0.5.11")
  implementation("com.discord4j:discord4j-core:3.2.6")
  implementation("com.h2database:h2")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

// Dev dependencies
dependencies { developmentOnly("org.springframework.boot:spring-boot-devtools") }

// Test dependencies
dependencies {
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "ch.qos.logback")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "21"
  }
}

tasks.withType<Test> { useJUnitPlatform() }

hibernate { enhancement { enableAssociationManagement.set(true) } }
