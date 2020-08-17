// Run with --info or --debug option to get more log output.
// https://docs.gradle.org/current/userguide/logging.html

// Gradle DSL plugins
// https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block
// https://plugins.gradle.org/
plugins {

	val kotlinVersion = "1.4.0"

	// Kotlin JVM plugin
	// https://kotlinlang.org/docs/reference/using-gradle.html
	kotlin("jvm") version kotlinVersion

	// Kotlin All-open and Spring compiler plugins to make classes open by default
	// https://kotlinlang.org/docs/reference/compiler-plugins.html
	kotlin("plugin.allopen") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion

	// Kotlin No-arg and JPA compiler plugins to generate zero-argument constructors
	// https://kotlinlang.org/docs/reference/compiler-plugins.html
	kotlin("plugin.noarg") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion

	// Kapt: Kotlin annotation processor
	// https://kotlinlang.org/docs/reference/kapt.html
	kotlin("kapt") version kotlinVersion

	// Java plugin
	// https://docs.gradle.org/current/userguide/java_plugin.html
	java

	// Idea plugin
	// https://docs.gradle.org/current/userguide/idea_plugin.html
	idea

	// JaCoCo plugin for test coverage
	// https://docs.gradle.org/current/userguide/jacoco_plugin.html
	jacoco

	// Dokka: Kotlin documentation engine
	// https://github.com/Kotlin/dokka
	// https://kotlinlang.org/docs/reference/kotlin-doc.html
	id("org.jetbrains.dokka") version "1.4.0-rc"

	// Spring Boot plugin to run and package the application
	// https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/
	id("org.springframework.boot") version "2.3.1.RELEASE"

	// gradle-git-properties plugin to generate git.properties (generateGitProperties task)
	// https://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html#howto-git-info
	// https://github.com/n0mer/gradle-git-properties
	id("com.gorylenko.gradle-git-properties") version "2.2.0"

	// grgit plugin to obtain version from git
	// http://ajoberstar.org/grgit/index.html
	// https://github.com/ajoberstar/grgit
	id("org.ajoberstar.grgit") version "4.0.1"

	// gradle-docker-compose plugin to run docker-compose with Gradle
	// https://github.com/avast/gradle-docker-compose-plugin
	id("com.avast.gradle.docker-compose") version "0.10.7"

	// gradle-versions-plugin to check for dependency updates (./gradlew dependencyUpdates -Drevision=release)
	// https://github.com/ben-manes/gradle-versions-plugin
	id("com.github.ben-manes.versions") version "0.27.0"

	// OWASP Dependency Check gradle plugin
	// https://github.com/jeremylong/dependency-check-gradle
	id("org.owasp.dependencycheck") version "5.3.0"

	// Liquibase plugin to run Liquibase tasks
	// https://github.com/liquibase/liquibase-gradle-plugin
	id("org.liquibase.gradle") version "2.0.4"

	// Asciidoctor plugin
	// https://asciidoctor.org/docs/asciidoctor-gradle-plugin/
	// https://github.com/asciidoctor/asciidoctor-gradle-plugin
	id("org.asciidoctor.jvm.convert") version "2.3.0"

}

// Apply the Spring dependency management plugin
// https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#managing-dependencies
// https://github.com/spring-gradle-plugins/dependency-management-plugin
apply(plugin = "io.spring.dependency-management")

repositories {
	// gradle plugin portal need for dokka
	// https://github.com/Kotlin/dokka
	gradlePluginPortal()

	// jcenter repository
	// https://bintray.com/bintray/jcenter
	jcenter()
}

// Dependencies
// https://docs.gradle.org/current/userguide/java_plugin.html#tab:configurations
dependencies {

	// Kotlin standard library
	// https://kotlinlang.org/api/latest/jvm/stdlib/
	implementation(kotlin("stdlib-jdk8"))

	// Kotlin reflection
	// https://kotlinlang.org/docs/reference/reflection.html
	implementation(kotlin("reflect"))

	// Kotlin logging library for easy logger declaration
	// https://github.com/MicroUtils/kotlin-logging
	implementation("io.github.microutils:kotlin-logging:1.7.8")

	// Spring Boot starter dependencies
	// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-gradle
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Spring Security Spring Data integration (SecurityEvaluationContextExtension)
	// https://docs.spring.io/spring-security/site/docs/current/reference/html5/#data
	implementation("org.springframework.security:spring-security-data")

	// HAL Browser
	// https://docs.spring.io/spring-data/rest/docs/current/reference/html/#_the_hal_browser
	implementation("org.springframework.data:spring-data-rest-hal-browser")

	// JSR-354/JavaMoney/Moneta money and currency implementation
	// http://javamoney.github.io/api.html
	// http://javamoney.github.io/ri.html
	implementation("org.javamoney:moneta:1.3")

	// Jadira Hibernate UserTypes for money and currency
	// http://jadira.sourceforge.net/usertype-userguide.html
	implementation("org.jadira.usertype:usertype.core:7.0.0.CR1")

	// Hibernate Types for JSON column mappings
	// https://github.com/vladmihalcea/hibernate-types
	implementation("com.vladmihalcea:hibernate-types-52:2.9.2")

	// Liquibase for database migrations
	// http://www.liquibase.org/
	// https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html#howto-execute-liquibase-database-migrations-on-startup
	implementation("org.liquibase:liquibase-core")

	// Jackson module to serialize/deserialize Kotlin classes
	// https://github.com/FasterXML/jackson-module-kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Jackson module to serialize/deserialize Hibernate specific data types and lazy-loaded relations
	// https://github.com/FasterXML/jackson-datatype-hibernate
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5")

	// Jackson module to serialize/deserialize of JavaMoney classes
	// https://github.com/zalando/jackson-datatype-money
	implementation("org.zalando:jackson-datatype-money:1.1.1")

	// Jackson data format module for reading and writing CSV
	// https://github.com/FasterXML/jackson-dataformats-text
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")

	// Jackson data format module for reading and writing XML
	// https://github.com/FasterXML/jackson-dataformat-xml
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

	// Problem-Spring-Web library to handle errors with application/problem+json
	// https://github.com/zalando/problem-spring-web
	implementation("org.zalando:problem-spring-web:0.25.2")

	// Google Guava
	// https://github.com/google/guava
	implementation("com.google.guava:guava:28.2-jre")

	// Janino for Logback conditionals
	// https://logback.qos.ch/setup.html#janino
	implementation("org.codehaus.janino:janino")

	// Slack webhook
	// https://github.com/gpedro/slack-webhook
	implementation("net.gpedro.integrations.slack:slack-webhook:1.4.0")

	// Jsoup HTML parser/cleaner
	// https://github.com/jhy/jsoup/
	implementation("org.jsoup:jsoup:1.12.1")

	// Thumbnailator for image resizing
	// https://github.com/coobird/thumbnailator
	implementation("net.coobird:thumbnailator:0.4.8")

	// Java Faker to generate test data
	// https://github.com/DiUS/java-faker
	implementation("com.github.javafaker:javafaker:1.0.1")

	// Slugify to generate slugs
	// https://github.com/slugify/slugify
	implementation("com.github.slugify:slugify:2.4")

	// JavaMelody Spring Boot starter
	// https://github.com/javamelody/javamelody/wiki/SpringBootStarter
	implementation("net.bull.javamelody:javamelody-spring-boot-starter:1.85.0")

	// Flying Saucer HTML renderer (using OpenPDF)
	// https://github.com/flyingsaucerproject/flyingsaucer
	implementation("org.xhtmlrenderer:flying-saucer-pdf-openpdf:9.1.16")

	// TwelveMonkeys ImageIO extensions
	// https://github.com/haraldk/TwelveMonkeys
	implementation("com.twelvemonkeys.servlet:servlet:3.5")
	implementation("com.twelvemonkeys.imageio:imageio-jpeg:3.5")

	// Spring Cloud AWS
	// https://cloud.spring.io/spring-cloud-aws/spring-cloud-aws.html
	implementation("org.springframework.cloud:spring-cloud-aws-context:2.2.1.RELEASE")

	// AWS SDK
	// https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-project-gradle.html
	implementation(platform("software.amazon.awssdk:bom:2.5.29"))
	implementation("software.amazon.awssdk:ses")

	// --- Annotation processors ---

	// Spring Annotation Processor
	// https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	// --- Runtime dependencies ---

	// MariaDB connector
	// https://github.com/MariaDB/mariadb-connector-j
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

	// Liquibase
	liquibaseRuntime("org.liquibase:liquibase-core")
	liquibaseRuntime("org.mariadb.jdbc:mariadb-java-client")
	liquibaseRuntime("ch.qos.logback:logback-classic")

	// --- Development dependencies ---

	// Spring Boot Devtools
	// https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// --- Test dependencies ---

	// Spring Boot test starter
	// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
	// https://spring.io/guides/tutorials/spring-boot-kotlin/#_testing_with_junit_5
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	// JUnit 5 (Jupiter)
	// https://junit.org/junit5/
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

	// JUnit 5 Parameterized Tests
	// https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-setup
	testImplementation("org.junit.jupiter:junit-jupiter-params")

	// Spring Security testing support
	// https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test
	testImplementation("org.springframework.security:spring-security-test")

	// Kluent assertion library
	// https://github.com/MarkusAmshove/Kluent
	testImplementation("org.amshove.kluent:kluent:1.59")

	// Apache HttpComponents HttpClient for TestRestTemplate tests over HTTPS
	// https://hc.apache.org/httpcomponents-client-ga/
	testImplementation("org.apache.httpcomponents:httpclient:4.5.11")

}

// --- Build ---

fun gitDescribe(): String {
	// Set currentDir to project.rootDir to locate .git directory
	// https://github.com/ajoberstar/grgit/issues/269
	val git = org.ajoberstar.grgit.Grgit.open(mapOf("currentDir" to project.rootDir))
	val describe = org.ajoberstar.grgit.operation.DescribeOp(git.repository).apply {
		always = true
		tags = true
	}.call()
	val status = org.ajoberstar.grgit.operation.StatusOp(git.repository).call()
	return describe + (if (status.isClean) "" else ".dirty")
}

// Set group identifier
group = "eu.mediprime"

// Set the project version with grgit (unless exit-after-startup is set)
version = if (System.getProperties()["at.rechnerherz.example.base.exit-after-startup"] == "true") "" else gitDescribe()

// Generate build-info.properties metadata (bootBuildInfo task)
// https://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html#howto-build-info
// https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#integrating-with-actuator-build-info
springBoot {
	buildInfo()
}

// --- Compile Java ---

// Java source/target compatibility
// https://docs.gradle.org/current/userguide/java_plugin.html#other_convention_properties

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

// --- Compile Kotlin ---

allOpen {
	// Make classes annotated with @Entity, @MappedSuperclass, and @Embeddable open by default
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

tasks.compileKotlin {
	// Run compileKotlin task to generate configuration metadata
	// Must depend on processResources to pick up additional metadata files
	// https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor
	dependsOn(tasks.processResources)

	// Move the generated metadata file to src/main/resources/META-INF
	doLast {
		file("src/main/resources/META-INF").mkdirs()
		file("build/tmp/kapt3/classes/main/META-INF/spring-configuration-metadata.json")
				.renameTo(file("src/main/resources/META-INF/spring-configuration-metadata.json"))
	}
}

// Set options for all Kotlin compilation tasks
// https://kotlinlang.org/docs/reference/using-gradle.html
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		// Enable strict null-safety for JSR-305 annotations
		// https://blog.jetbrains.com/kotlin/2017/09/kotlin-1-1-50-is-out/
		freeCompilerArgs = listOf("-Xjsr305=strict")

		// Set target version for generate JVM bytecode
		jvmTarget = "11"

		// Generate metadata for Java reflection on method parameters
		javaParameters = true
	}
}

// --- Run ---

// Configure docker-compose tasks
dockerCompose {

	// Create localComposeUp task to start local dependencies
	createNested("local").apply {
		projectName = "example-local"
		useComposeFiles = listOf("./docker/local/docker-compose.yml")

		// Don't stop containers, try to reconnect instead
		stopContainers = false

		// Start before running bootRun
		isRequiredBy(tasks.bootRun.get())
	}

	// Create testingComposeUp task to start testing dependencies
	createNested("testing").apply {
		projectName = "example-testing"
		useComposeFiles = listOf("./docker/testing/docker-compose.yml")

		// Don't stop containers, try to reconnect instead
		stopContainers = false

		// Start before running test
		isRequiredBy(tasks.test.get())
	}

	// Create portainerComposeUp task to start portainer
	createNested("portainer").apply {
		projectName = "example-local"
		useComposeFiles = listOf("./docker/portainer-docker-compose.yml")

		// Don"t stop containers, try to reconnect instead
		stopContainers = false
	}
}

// Create a task to drop and re-create DB schemas as a replacement for dropAll
// With Liquibase 3.8+ and MariaDB 10.3+ dropAll doesn't work
// https://liquibase.jira.com/browse/CORE-3457
tasks.create<Exec>("dropCreateSchemas") {
	commandLine(
			"mysql",
			"--protocol=TCP",
			"--user=root",
			"--password=root",
			"--database=information_schema",
			"--execute=;DROP SCHEMA IF EXISTS `example`;CREATE SCHEMA `example`;"
	)
}

tasks.bootRun {

	// Pass properties from command line to bootRun task
	// https://stackoverflow.com/questions/25079244/how-to-pass-jvm-options-from-bootrun
	System.getProperties()
			// Don't set "endorsed dirs", unsupported since JDK9
			// https://docs.oracle.com/javase/9/migrate/#GUID-D867DCCC-CEB5-4AFA-9D11-9C62B7A3FAB1
			.filter { it.key != "java.endorsed.dirs" }
			.forEach { (key, value) -> systemProperties[key.toString()] = value }

	// Enable flying-saucer logging over SLF4J bridge
	// https://flyingsaucerproject.github.io/flyingsaucer/r8/guide/users-guide-R8.html#xil_49
	// https://stackoverflow.com/questions/9729147/turning-on-flying-saucer-java-util-logging-output
	systemProperties["xr.util-logging.loggingEnabled"] = "true"
	systemProperties["xr.util-logging.handlers"] = "org.slf4j.bridge.SLF4JBridgeHandler"

	// Disable the Hypersistence Optimizer banner
	// https://github.com/vladmihalcea/hibernate-types
	systemProperties["hibernate.types.print.banner"] = "false"

	val jvmArguments = mutableListOf<String>()

	// Listen to remote debugging on port 5005 if remote-debug is set to true
	// https://docs.oracle.com/javase/8/docs/technotes/guides/jpda/conninv.html#Invocation
	if (systemProperties["remote-debug"] == "true")
		jvmArguments.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005")

	// Uncomment to limit JVM memory to 1GB to debug memory issues
	//jvmArguments.add("-Xmx1024m")

	jvmArgs = jvmArguments
}

val liquibaseUsername: String by project
val liquibasePassword: String by project
val liquibaseUrl: String by project
val liquibaseChangeLogFile: String by project
val liquibaseDiffUrl: String by project
val liquibaseDiffChangeLogFile: String by project

// Liquibase plugin configuration
liquibase {

	// main properties for tasks like "status" and "update"
	activities.register("main") {
		arguments = mapOf(
				"changeLogFile" to liquibaseChangeLogFile,
				"url" to liquibaseUrl,
				"username" to liquibaseUsername,
				"password" to liquibasePassword,
				"logLevel" to "info"
		)
	}

	// diff properties for tasks like "diff" and "diffChangeLog"
	activities.register("diff") {
		arguments = mapOf(
				"changeLogFile" to liquibaseDiffChangeLogFile,
				"url" to liquibaseDiffUrl,
				"username" to liquibaseUsername,
				"password" to liquibasePassword,
				"referenceUrl" to liquibaseUrl,
				"referenceUsername" to liquibaseUsername,
				"referencePassword" to liquibasePassword
		)
	}

	// default properties
	runList = "main"
}

tasks.diffChangeLog {
	doFirst {
		// Use diff properties
		liquibase.runList = "diff"

		// Delete old Liquibase diff instead of appending to it
		file(liquibaseDiffChangeLogFile).delete()
	}

	// After creating diff, replace VARCHAR(255 BYTE) with VARCHAR(255)
	// Workaround for https://liquibase.jira.com/browse/CORE-2930
	doLast {
		file(liquibaseDiffChangeLogFile).apply {
			if (exists())
				writeText(
						readText()
								.replace("""VARCHAR\(([0-9]+) BYTE\)""".toRegex(), """VARCHAR($1)""")
				)
		}
	}
}

// --- Package ---

// Create a fully executable jar
// https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-install
tasks.bootJar {
	launchScript()
}

// --- Test ---

// Use JUnit5 when running tests with Gradle
// https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html
tasks.test {
	useJUnitPlatform()
}

// Add a task to run only unit tests
tasks.create<Test>("unitTest") {
	useJUnitPlatform {
		includeTags("unit")
	}
}

// Add a task to run only integration tests
tasks.create<Test>("integrationTest") {
	useJUnitPlatform {
		includeTags("integration")
	}
}

tasks.withType<Test> {
	// Log test result events
	testLogging {
		events("PASSED", "FAILED", "SKIPPED")
	}

	// Print a test summary
	addTestListener(object : TestListener {
		override fun beforeTest(desc: TestDescriptor) {}
		override fun beforeSuite(desc: TestDescriptor) {}
		override fun afterTest(desc: TestDescriptor, result: TestResult) {}
		override fun afterSuite(desc: TestDescriptor, result: TestResult) {
			if (desc.parent != null) {
				println(
						"""
                    |Test result: ${result.resultType}
                    |Test summary:
                    |${result.testCount} tests
                    |${result.successfulTestCount} succeeded
                    |${result.failedTestCount} failed
                    |${result.skippedTestCount} skipped""".trimMargin()
				)
			}
		}
	})
}

jacoco {
	toolVersion = "0.8.2"
}

tasks.jacocoTestReport {
	reports.html.destination = file("$buildDir/reports/jacoco")
}

// --- Documentation ---

// https://github.com/asciidoctor/asciidoctor-gradle-plugin#using-asciidoctorj-diagram
asciidoctorj {
	modules.diagram.setVersion("1.5.4.1")
}

// https://github.com/asciidoctor/asciidoctor-gradle-plugin#the-new-asciidoctorj-plugin
tasks.asciidoctor {
	setSourceDir(file("doc"))
}
