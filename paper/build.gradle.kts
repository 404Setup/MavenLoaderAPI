plugins {
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

base {
    archivesName.set("${rootProject.name}-Paper")
}

repositories {
}

dependencies {
    implementation(project(":common"))

    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    implementation("one.tranic:t-base:1.2.7")
    implementation("one.tranic:t-bukkit:1.0.2") {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-text-minimessage")
        exclude("net.kyori", "adventure-platform-bukkit")
        exclude("org.spigotmc", "spigot-api")
    }
    implementation("one.tranic:t-paper:1.0.2")
    implementation("one.tranic:t-utils:1.2.2.1")
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
        exclude("net/kyori/**")
        exclude("org/slf4j/**")
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching(listOf("plugin.yml")) {
        expand(props)
    }
}