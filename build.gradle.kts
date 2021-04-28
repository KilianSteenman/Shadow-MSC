plugins {
    kotlin("jvm") version "1.4.32"
    id("maven")
}

group = "com.github.kiliansteenman"
version = "0.3.2-alpha"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("com.google.truth:truth:1.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}