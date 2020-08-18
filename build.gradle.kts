import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val projectName = "jmplib"
group = "xyz.jpenilla"
version = "1.0.0+{BUILDID}-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven(url = "https://jitpack.io")
}

dependencies {
    annotationProcessor("org.projectlombok", "lombok", "1.18.12")
    compileOnly("org.projectlombok", "lombok", "1.18.12")
    compileOnly("org.jetbrains", "annotations", "20.0.0")
    compileOnly("org.spigotmc", "spigot-api", "1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.github.jmanpenilla", "adventure-text-minimessage", "2cb5077c9d")
    compileOnly("net.kyori", "adventure-platform-bukkit", "4.0.0-SNAPSHOT")
    compileOnly("net.kyori", "adventure-text-feature-pagination", "4.0.0-SNAPSHOT")
    compileOnly("me.clip", "placeholderapi", "2.10.9")
    compileOnly("com.github.DiamondDagger590", "Prisma", "a622d01b80")
}

val autoRelocate by tasks.register<ConfigureShadowRelocation>("configureShadowRelocation", ConfigureShadowRelocation::class) {
    target = tasks.getByName("shadowJar") as ShadowJar?
    val packageName = "${project.group}.${project.name.toLowerCase()}"
    prefix = "$packageName.shaded"
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        archiveFileName.set("$projectName-${project.version}.jar")
        dependsOn(autoRelocate)
        minimize()
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jmanpenilla/jmplib")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
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
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = projectName
            version = project.version.toString()

            from(components["java"])
        }
    }
}