import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.taskTriggers

plugins {
    id("eclipse")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.9"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

base {
    archivesName.set("${rootProject.name}-Velocity")
}

repositories {
}

dependencies {
    implementation(project(":common"))
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    implementation("one.tranic:t-base:1.2.8")
    implementation("one.tranic:t-velocity:1.0.2")
}

tasks.build {
    dependsOn("shadowJar")
}

val libPackage = "one.tranic.mavenloader.libs"

tasks.shadowJar {
    exclude("org/bson/codecs/pojo/**")

    relocate("one.tranic.t", "${libPackage}.tlib")

    minimize {
        exclude("META-INF/**")
        exclude("com/google/j2objc/**")
        exclude("com/sun/jna/**")
        exclude("com/google/gson/**")
        exclude("com/google/errorprone/**")
        exclude("org/jetbrains/annotations/**")
        exclude("org/checkerframework/**")
        exclude("com/velocitypowered/**")
        exclude("net/kyori/**")
        exclude("org/slf4j/**")
    }
}

val templateSource = file("src/main/templates")
val templateDest = layout.buildDirectory.dir("generated/sources/templates")
val generateTemplates = tasks.register<Copy>("generateTemplates") {
    val props = mapOf("version" to project.version)
    inputs.properties(props)

    from(templateSource)
    into(templateDest)
    expand(props)
}

sourceSets.main.configure { java.srcDir(generateTemplates.map { it.outputs }) }
rootProject.idea.project.settings.taskTriggers.afterSync(generateTemplates)
rootProject.eclipse.synchronizationTasks(generateTemplates)