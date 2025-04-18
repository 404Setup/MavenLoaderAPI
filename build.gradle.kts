plugins {
    java
    id("eclipse")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.9"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

allprojects {
    group = "one.tranic"
    version = "1.6-SNAPSHOT"

    apply(plugin = "java")

    repositories {
        maven("https://maven-central-asia.storage-download.googleapis.com/maven2/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.2")
    }

    val targetJavaVersion = 17

    java {
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        if (JavaVersion.current() < javaVersion) {
            toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
}