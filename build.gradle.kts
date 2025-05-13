import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.3"
}

group = "com.ddd.api"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// db
	runtimeOnly("com.h2database:h2")

	// swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<Copy>("initGitHooks") {
    from(file("$rootDir/githooks")) {
        include("pre-commit")
        rename("pre-commit", "pre-commit")
    }

    into(file("$rootDir/.git/hooks"))

    doLast {
        val hookFile = file("$rootDir/.git/hooks/pre-commit")
        if (hookFile.exists()) {
            hookFile.setExecutable(true)
        }
    }
}

tasks.named("clean") {
    dependsOn("initGitHooks")
}

configure<SpotlessExtension> {
    java {
        googleJavaFormat().aosp()

        trimTrailingWhitespace()
        endWithNewline()
        removeUnusedImports()
    }
}
