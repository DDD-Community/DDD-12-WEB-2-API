import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.3"
}

group = "com.moyorak.api"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

tasks.bootJar {
    layered {}
}

repositories {
	mavenCentral()
}

dependencies {
	//spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

    //spring security
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// db
	runtimeOnly("com.h2database:h2")
    implementation("mysql:mysql-connector-java:8.0.33")

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
