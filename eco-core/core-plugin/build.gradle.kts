group = "com.willfp"
version = rootProject.version

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.willfp:Talismans:6.0.0")
    compileOnly("com.github.ben-manes.caffeine:caffeine:3.0.2")

    implementation("com.willfp:ecomponent:1.3.0")
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(components["java"])
            artifactId = rootProject.name
        }
    }
}

tasks {
    build {
        dependsOn(publishToMavenLocal)
    }
}
