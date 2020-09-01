# Spring Boot Kotlin API Template

## Stack/Features

- Completely written in Kotlin, but can use Java too when necessary
  - Language level and runtime JDK is 11 (for improved JVM memory handling in containers). 

- Spring Boot 2.3 / Spring Framework 5.3

- Gradle 6
  - gradle-docker-compose plugin to run docker-compose with Gradle
  - grgit Gradle plugin to generate version from git
  - OWASP Dependency Check gradle plugin
  - Liquibase plugin to run Liquibase tasks (e.g. genarete DB diffs)
  - Asciidoctor plugin to generate documentaion

- Persistence:
  - MariaDB 10.5
  - Hibernate 5.4
  - Hibernate Validator 6 to validate entities before persisting
  - Hibernate types
    - Java 8 date/time (provided by Spring Boot) 
    - JSON as string
    - Java Money
  - Base entities (using `@MappedSuperclass`) to generate correct `equals`/`hashCode`/`toString`, and a primary key
  - JPA auditing (automatically set createdBy/modifiedBy/createdAt/modifiedAt)
  - Example entities:
    - An abstract `Account` entity with `Admin` and `Customer` subclasses
    - A `VerificationToken` entity to confirm registration, change the account username, or reset the account password
    - A `Document` entity
  - Annotations to check programatically whether an Entity is deletable

- REST API with Spring Data REST (CRUD endpoints for all entities)
  - Custom permission rules for entities (e.g. only access "own" entities, or entities related to authenticated account)
  
- Logging:
  - Logging with kotlin-logging
  - Logback appender to send mails (send mails for all errors)
  - Logback appender to send Slack messages
  - Application listeners to log application events and handled HTTP requests
  - AOP profiling of all Spring bean methods calls (logging and exucation time measurement)

- Serialization of JSON and other formats
  - Jackson Modules
    - Java 8 date/time (provided by Spring Boot)
    - Kotlin
    - Java Money (JSR-354)
    - Hibernate with FORCE_LAZY_LOADING
  - Jackson DataTypes for XML and CSV

- Spring Security
  - Warn and block after too many failed login attempts per IP address or username
  - Require SSL for all requests
  - CORS configuration
  - CSRF protection
  - Path-based request restrictions (e.g /public paths are public, /api requires authentication)
  - Annotations to restrict endpoints to certain roles
  - CSP reports

- File handling:
  - Thymleaf templates (not used for MVC views, only to generate mails and PDFs in the backend)
  - Flying Saucer HTML to PDF renderer
  - TwelveMonkeys ImageIO extensions to support more image formats
  - Thumbnailator to generate image thumbnails
  - Store files on the filesystem or on S3

- Exception handling with application/problem+json support
- A service to generate sitemap.xml and robots.txt
- Monitoring with JavaMelody
- Java Faker to generate test data
- Slugify to generate slugs
- Jsoup HTML parser/cleaner

- Development tools
  - Receive mails locally with MailDev
  - Locally trusted SSL certificate with mkcert

- Testing with JUnit5
  - Integration testing in an identical docker environment with full Spring application context and the DB in tmpfs
  - Unit testing without Spring application context
  - Functional test data (can be used for development test data and in unit and integration tests)

- docker-compose files for development and testing:
  - `local`: MariaDB and MailDev
  - `testing`: MariaDB (in a tmpfs) and MailDev for integration testing

## Setup

To get started:

 - copy the project
 - search/replace "at.rechnerherz.example", "@example.com", and "example" with the new project name 
 - update the "at.rechnerherz.example" package directories

## Usage

Run with:

  - `./gradlew bootRun`, or
  - `./gradlew bootRun -Dremote-debug=true` to listen to remote debugging connection on default port `5005` 
  - `./gradlew bootRun -Dspring.jpa.hibernate.ddl-auto=create` to create the database.
  - `./gradlew localComposeUp dropCreateSchemas bootRun -Dspring.jpa.hibernate.ddl-auto=create` to drop and create the database.

The application runs in an embedded Tomcat on `https://localhost:8443`.

JavaMelody is available as admin under `https://localhost:8443/monitoring`.
  
The `bootRun` task depends on the `localComposeUp` task, which starts two Docker containers:

- MariaDB listening on `localhost:3306`
- MailDev SMTP on port `localhost:3025`, with web interface on `http://localhost:3080/`.

To re-build without running tests:

  - `./gradlew clean build --exclude-task test`

## Test

Run tests with:

  - `./gradlew test` (or `./gradlew check` which depends on test)
  - To only run unit tests (fast): `./gradlew unitTest` 
  - To only run integration tests (slow): `./gradlew integrationTest` 

The integration tests starts an application in an embedded Tomcat on `https://localhost:4443`.

The `test` task depends on the `testingComposeUp` task, which starts two Docker containers:

- MariaDB listening on `localhost:4306`
- MailDev SMTP on port `localhost:4025`

## Documentation

Run `./gradlew asciidoctor` to generate docs from `.adoc` files (in `./build/asciidoc`).

Run `./gradlew dokkaHtml` to generate JavaDoc/KDoc docs (in `./build/dokka/html/mp-api/index.html`).

## Profiles

The following profiles are defined:

- `development` (default)
- `testing`: unit/integration testing
- `staging`: staging server
- `production`: production server

Tomcat is at /tomcat except local, at localhost:8443. 

## Logging

To obtain a logger, use:

    private val log = KotlinLogging.logger {}

Then log with 

    log.debug { "Some $expensive message!" }
    log.error(exception) { "a $fancy message about the $exception" }

[kotlin-logging](https://github.com/MicroUtils/kotlin-logging)

## Locally trusted SSL certificate

The certificate is signed by a local CA. The certificate was generated with:
   
    mkcert -pkcs12 localhost 127.0.0.1 ::1

To install the CA, install [mkcert](https://github.com/FiloSottile/mkcert) and run:

    cp example-api/src/main/resources/dev/rootCA.pem "$(mkcert -CAROOT)"
    export JAVA_HOME="$(dirname $(dirname $(readlink -f $(which java))))"
    mkcert -install

To check whether it was successfully installed in the Java trust store:

    keytool -cacerts -storepass changeit -list | grep mkcert
    
To check whether it was successfully installed in an NSS DB (Firefox/Chrome):

    certutil -L -d .pki/nssdb | grep mkcert

## Gradle Wrapper

To upgrade the Gradle wrapper use:

    ./gradlew wrapper --gradle-version=<version> --distribution-type=ALL

## Native Memory Tracking

To get the native memory summary from the Tomcat container, run on the instance:

    docker exec -it "$(docker ps -qf name=tomcat)" sh -c 'jcmd "$(pgrep -of /docker-java-home/bin/java)" VM.native_memory summary'

[Native Memory Tracking](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr007.html)
