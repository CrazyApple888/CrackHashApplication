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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework:spring-oxm")

	implementation("org.springframework.boot:spring-boot-starter-amqp")

	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("javax.activation:activation:1.1.1")
	implementation ("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
	jaxb("org.glassfish.jaxb:jaxb-runtime:4.0.2")
	jaxb("org.glassfish.jaxb:jaxb-xjc:4.0.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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
