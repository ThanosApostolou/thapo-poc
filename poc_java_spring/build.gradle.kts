import com.google.protobuf.gradle.id

plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.bjornvester.xjc") version "1.9.0"
    id("com.netflix.dgs.codegen") version "8.4.3"
    id("com.google.protobuf") version "0.10.0"
}

group = "thapo"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

val grpcVersion = "1.74.0"
val protobufVersion = "4.33.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springGrpcVersion"] = "1.0.3"

dependencies {
    // Helpful
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-devtools")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // HTML templating
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // REST
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-resttestclient")
    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    // WebSocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    testImplementation("org.springframework.boot:spring-boot-starter-websocket-test")
    // GraphQL
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    // gRPC
    implementation("io.grpc:grpc-services")
    implementation("org.springframework.grpc:spring-grpc-server-spring-boot-starter")
    testImplementation("org.springframework.grpc:spring-grpc-test")
    implementation("io.grpc:grpc-servlet-jakarta")
//    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
//    implementation("io.grpc:grpc-protobuf:$grpcVersion")
//    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
//    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    // SOAP
    implementation("org.springframework.boot:spring-boot-starter-webservices")
    testImplementation("org.springframework.boot:spring-boot-starter-webservices-test")
    runtimeOnly("wsdl4j:wsdl4j")
    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")
    // Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-core")
    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql:42.7.7")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask>("generateJava") {
    schemaPaths = mutableListOf("${projectDir}/src/main/resources/graphql")
    packageName = "thapo.pocspring.application.graphql.generated"
    generateJSpecifyAnnotations = true
}

xjc {
    xsdDir.set(layout.projectDirectory.dir("src/main/resources/soap"))
    defaultPackage.set("thapo.pocspring.application.soap.dto")
    addCompilationDependencies.set(false)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.33.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.80.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("@generated=omit")
                }
            }
        }
    }
}