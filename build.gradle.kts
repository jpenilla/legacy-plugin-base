plugins {
    `java-library`
    `maven-publish`
}

group = "xyz.jpenilla"
version = "0.0.1+${System.getenv("GITHUB_RUN_NUMBER") ?: "local"}-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    disableAutoTargetJvm()
    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(8)
        options.compilerArgs.addAll(listOf("-Xlint:deprecation"))
    }
}

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.jpenilla.xyz/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi") {
        content { includeGroup("me.clip") }
    }
}

dependencies {
    api(platform("net.kyori:adventure-bom:4.25.0"))
    api("net.kyori", "adventure-api")
    api("net.kyori", "adventure-text-serializer-gson") {
        exclude("com.google.code.gson", "gson")
    }
    api("net.kyori", "adventure-text-serializer-legacy") {
        exclude("com.google.code.gson", "gson")
    }

    api("net.kyori", "adventure-text-minimessage")
    api("net.kyori", "adventure-text-serializer-plain")
    api("net.kyori", "adventure-text-feature-pagination", "4.0.0-SNAPSHOT")
    val adventurePlatformVersion = "4.4.1"
    api("net.kyori", "adventure-platform-bukkit", adventurePlatformVersion) {
        exclude("com.google.code.gson", "gson")
    }
    api("net.kyori", "adventure-text-serializer-bungeecord", adventurePlatformVersion) {
        exclude("com.google.code.gson", "gson")
    }

    compileOnly("org.jspecify:jspecify:1.0.0")
    compileOnly("dev.folia", "folia-api", "1.19.4-R0.1-SNAPSHOT")
    compileOnly("me.clip", "placeholderapi", "2.11.6")
}

publishing {
    repositories.maven {
        name = "Repo"
        url = uri("https://repo.jpenilla.xyz/snapshots")
        credentials {
            username = System.getenv("REPO_USER")
            password = System.getenv("REPO_TOKEN")
        }
    }
    publications.create<MavenPublication>("maven") {
        groupId = project.group.toString()
        artifactId = project.name
        version = project.version.toString()

        from(components["java"])
    }
}
