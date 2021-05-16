plugins {
    `java-library`
    `maven-publish`
}

group = "xyz.jpenilla"
version = "1.0.1+${System.getenv("GITHUB_RUN_NUMBER") ?: "local"}-SNAPSHOT"

repositories {
    //mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi") {
        content { includeGroup("me.clip") }
    }
}

dependencies {
    val adventureVersion = "4.7.0"
    api("net.kyori", "adventure-api", adventureVersion)
    api("net.kyori", "adventure-text-serializer-gson", adventureVersion) {
        exclude("com.google.code.gson", "gson")
    }
    api("net.kyori", "adventure-text-serializer-legacy", adventureVersion) {
        exclude("com.google.code.gson", "gson")
    }

    api("net.kyori", "adventure-text-minimessage", "4.1.0-SNAPSHOT")
    api("net.kyori", "adventure-text-feature-pagination", "4.0.0-SNAPSHOT")
    api("net.kyori", "adventure-platform-bukkit", "4.0.0-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }
    api("net.kyori", "adventure-text-serializer-bungeecord", "4.0.0-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }

    compileOnly("org.checkerframework", "checker-qual", "3.13.0")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("me.clip", "placeholderapi", "2.10.9")
}

java {
    sourceCompatibility = JavaVersion.toVersion(8)
    targetCompatibility = JavaVersion.toVersion(8)
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