buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.7.7")
        classpath("org.flywaydb:flyway-database-postgresql:11.12.0")
    }
}


plugins {
    id("org.flywaydb.flyway") version "11.12.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}


flyway {
    url = "jdbc:postgresql://localhost:11432/pocwebframeworks_db"
    user = "pocwebframeworks_user"
    password = "pocwebframeworks_password"
    schemas = Array<String>(1) { "pocwebframeworks_schema" }
    validateMigrationNaming = true
}