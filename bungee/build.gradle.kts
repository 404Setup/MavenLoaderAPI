plugins {
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

base {
    archivesName.set("${rootProject.name}-Bungee")
}

repositories {
}

dependencies {
    implementation(project(":common"))
    compileOnly("net.md-5:bungeecord-api:1.18-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bungeecord:4.3.4")
    implementation("net.kyori:adventure-text-minimessage:4.21.0")

    implementation("one.tranic:t-base:1.2.8")
    implementation("one.tranic:t-bungee:1.0.2")
    implementation("one.tranic:t-utils:1.3.0")

    compileOnly("org.slf4j:slf4j-api:2.0.17")
}

val libPackage = "one.tranic.mavenloader.libs"

tasks.shadowJar {
    exclude("org/bson/codecs/pojo/**")

    relocate("one.tranic.t", "${libPackage}.tlib")

    minimize {
        exclude("META-INF/**")
        exclude("com/sun/jna/**")
        exclude("com/google/gson/**")
        exclude("com/google/errorprone/**")
        exclude("org/jetbrains/annotations/**")
        exclude("org/checkerframework/**")
        exclude("org/slf4j/**")
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching(listOf("bungee.yml")) {
        expand(props)
    }
}