plugins {
    kotlin("jvm") version "1.9.23"
}

repositories {
    mavenCentral()
}

val kotlinVersion = "1.9.23"
val jedisVersion = "4.0.0"
val slf4jVersion = "1.7.32"
val mockitoVersion = "4.0.0"
val embeddedRedisVersion = "0.6"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("redis.clients:jedis:$jedisVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
    testImplementation("com.github.kstyrc:embedded-redis:$embeddedRedisVersion")
}
