import gg.essential.gradle.util.noServerRunConfigs

plugins {
  kotlin("jvm") version "1.8.20"
  kotlin("plugin.serialization") version "1.8.20"
  id("gg.essential.defaults.loom") version "0.1.18"
  id("gg.essential.defaults") version "0.1.18"
}

val base_name: String by project
val mod_version: String by project
val maven_group: String by project

val minecraft_version: String by project
val fabric_api_version: String by project
val fabric_kotlin_version: String by project
val fabric_loader_version: String by project

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
  // kotlin dependency (may not need this if I have essential)
  // modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")

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
    val expansions =
        mapOf(
            "mod_version" to mod_version,
            "base_name" to base_name,
            "fabric_loader_version" to fabric_loader_version,
            "fabric_api_version" to fabric_api_version,
            "minecraft_version" to minecraft_version,
        )

    // inputs.property("mod_version_expansions", expansions)
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

loom {
  noServerRunConfigs()
}

/*
 java {
 	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
 	// if it is present.
 	// If you remove this line, sources will not be generated.
 	withSourcesJar()
 }
 */
