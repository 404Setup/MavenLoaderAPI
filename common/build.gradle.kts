repositories {
}

dependencies {
    compileOnly("org.slf4j:slf4j-api:2.0.17")
    compileOnly("com.google.code.gson:gson:2.11.0")
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")

    implementation("org.apache.maven:maven-resolver-provider:3.9.9")
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.22")
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.22")

    implementation("one.tranic:t-utils:1.3.0")
}