plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
}

group = "net.craftoriya"
version = "0.12.5-cf-SNAPSHOT" // needs to be changed

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://repo.mineinabyss.com/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.craftoriya.net/repository/public/")
    }
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.anvilgui)
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.mccoroutine.api)
    compileOnly(libs.craftoriya.toolkit.utils)
    compileOnlyApi(libs.runtime.desktop)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])

            groupId = group.toString()
            artifactId = base.archivesName.get()
            version = version.toString()

            pom {
                name.set(base.archivesName.get())
            }

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }

    repositories {
        mavenLocal()
        /* Craftoriya Maven Repo*/
        maven {
            url = if (project.version.toString().endsWith("SNAPSHOT")) {
                System.getenv("NEXUS_SNAPSHOT_REPO_URL") ?: findProperty("craftoriya.nexus.url").toString()
            } else {
                System.getenv("NEXUS_REPO_URL") ?: findProperty("craftoriya.nexus.url").toString()
            }.let(::uri)

            credentials {
                username = System.getenv("NEXUS_USER") ?: findProperty("craftoriya.nexus.username").toString()
                password =
                    System.getenv("NEXUS_USER_PASSWORD") ?: findProperty("craftoriya.nexus.password").toString()
            }
            isAllowInsecureProtocol = true
            metadataSources {
                mavenPom()
                artifact()
            }
        }
    }
}

