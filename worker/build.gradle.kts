import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

val jaxb: Configuration by configurations.creating

val jaxbVersion: String = "4.0.0"
val schemaDir = "src/main/resources"
val xjcOutputDir = "$buildDir/generated/source/xjc/main"

group = "ru.nsu.isachenko"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.googlecode.combinatoricslib:combinatoricslib:2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("javax.activation:activation:1.1.1")
    implementation ("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    jaxb("org.glassfish.jaxb:jaxb-runtime:4.0.2")
    jaxb("org.glassfish.jaxb:jaxb-xjc:4.0.2")

    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework:spring-oxm")
}

val createXjcOutputDir by tasks.register("createXjcOutputDir") {
    doLast {
        mkdir(xjcOutputDir)
    }
}

val xjc by tasks.registering(JavaExec::class) {
    dependsOn(createXjcOutputDir)
    classpath = jaxb
    mainClass.set("com.sun.tools.xjc.XJCFacade")
    args = listOf(
        "-d",
        xjcOutputDir,
        "-p",
        project.group.toString(),
        "-encoding",
        "UTF-8",
        "-no-header",
        "-quiet",
        schemaDir
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(xjc)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.nsu.isachenko.worker.WorkerApplication"
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                files(xjcOutputDir) {
                    builtBy(xjc)
                }
            )
        }
    }
}
