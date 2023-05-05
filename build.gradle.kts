plugins {
  kotlin("jvm") version "1.8.21"
  kotlin("plugin.serialization") version "1.8.21"
  id("gg.essential.loom") version "0.10.0.2"
}

val minecraft_version: String by project
val yarn_mappings: String by project
val fabric_api_version: String by project
val fabric_loader_version: String by project
val fabric_kotlin_version: String by project

repositories {
  // Add repositories to retrieve artifacts from in here.
  // You should only use this when depending on other mods because
  // Loom adds the essential maven repositories to download Minecraft and libraries from
  // automatically.
  // See https://docs.gradle.org/current/userguide/declaring_repositories.html
  // for more information about repositories.
  maven("https://repo.essential.gg/repository/maven-public/")
  maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
  minecraft("com.mojang:minecraft:$minecraft_version")
  mappings("net.fabricmc:yarn:$yarn_mappings:v2")
  // fabric loader
  modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
  // kotlin dependency (may not need this if I have essential)
  modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")

  // fabric api
  setOf(
          "fabric-api-base",
          "fabric-networking-api-v1",
          "fabric-lifecycle-events-v1",
          "fabric-rendering-v1"
      )
      .forEach {
        // Add each module as a dependency
        modImplementation(fabricApi.module(it, "$fabric_api_version"))
      }

  // essential dependencies
  "include"("modRuntimeOnly"("gg.essential:loader-fabric:1.0.0")!!)
  compileOnly("gg.essential:essential-1.18.2-fabric:12328+g551779957")

  // mod menu
  modApi("com.terraformersmc:modmenu:3.2.5")
}

tasks {
  processResources {
    val expansions = project.properties

    filesMatching(listOf("fabric.mod.json")) { expand(expansions) }
  }

  jar { from("LICENSE") }

  compileKotlin {
    kotlinOptions.freeCompilerArgs +=
        listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xno-param-assertions",
            "-Xjvm-default=all-compatibility"
        )
    kotlinOptions.jvmTarget = "17"
  }
  /*
  publishing {
  	publications {
  			create<MavenPublication>("mavenJava") {
  					artifact(remapJar) {
  							builtBy(remapJar)
  					}
  					artifact(kotlinSourcesJar) {
  							builtBy(remapSourcesJar)
  					}
  			}
  	}

  	// select the repositories you want to publish to
  	repositories {
  			// uncomment to publish to the local maven
  			// mavenLocal()
  	}
  }
  */
}

/*
 java {
 	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
 	// if it is present.
 	// If you remove this line, sources will not be generated.
 	withSourcesJar()
 }
 */
