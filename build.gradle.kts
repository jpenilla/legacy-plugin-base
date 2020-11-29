java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

plugins {
    `java-library`
    `maven-publish`
}

val projectName = "jmplib"
group = "xyz.jpenilla"
version = "1.0.1+{BUILDID}-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven(url = "https://jitpack.io")
}

dependencies {
    annotationProcessor("org.projectlombok", "lombok", "1.18.16")

    val adventureVersion = "4.2.0"
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

    compileOnly("org.checkerframework", "checker-qual", "3.5.0")
    compileOnly("org.projectlombok", "lombok", "1.18.12")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.3-R0.1-SNAPSHOT")
    compileOnly("me.clip", "placeholderapi", "2.10.9")
    compileOnly("com.github.DiamondDagger590", "Prisma", "a622d01b80")
}

publishing {
    repositories {
        maven {
            name = "Repo"
            url = uri("https://repo.jpenilla.xyz/snapshots")
            credentials {
                username = System.getenv("REPO_USER")
                password = System.getenv("REPO_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {//todo try putting back github packages
            groupId = project.group.toString()
            artifactId = projectName
            version = project.version.toString()

            from(components["java"])
        }
    }
}