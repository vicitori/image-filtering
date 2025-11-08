import org.gradle.internal.classpath.Instrumented.systemProperty

plugins {
    java
    application
    id("com.github.spotbugs") version "6.0.17"
    id("me.champeau.jmh") version "0.7.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("info.picocli:picocli:4.7.5")
    
    // Lincheck for concurrency testing
    testImplementation("org.jetbrains.kotlinx:lincheck:2.24")
    
    // JUnit for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JMH for benchmarking
    jmhImplementation("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

jmh {
    jmhVersion = "1.37"
    fork = 1
    warmupIterations = 2
    iterations = 5
    timeUnit = "ms"
    resultFormat = "JSON"
    resultsFile = project.file("src/jmh/java/com/vicitori/results.json")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.vicitori.app.Main"
}

tasks.register<Jar>("fatJar") {
    group = "build"
    archiveClassifier.set("all")
    archiveBaseName.set("image-filtering")

    manifest {
        attributes["Main-Class"] = "com.vicitori.app.Main"
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}

// SpotBugs configuration
spotbugs {
    ignoreFailures = false
}