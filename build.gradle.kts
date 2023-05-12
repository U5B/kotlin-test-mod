import gg.essential.gradle.util.*

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("gg.essential.multi-version")
  id("gg.essential.defaults")
  id("com.modrinth.minotaur") version "2.+"
}

val mod_id: String by project
val mod_version: String by project
val mod_name: String by project
val mod_description: String by project
val maven_group: String by project

val fabric_loader_version: String by project
val fabric_api_version: String by project
val fabric_kotlin_version: String by project
val mod_menu_version: String by project

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
	modImplementation("net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}")

	// fabric api
	setOf(
		"fabric-api-base",
		"fabric-networking-api-v1",
		"fabric-lifecycle-events-v1",
		"fabric-rendering-v1"
	).forEach {
		// Add each module as a dependency
		modImplementation(fabricApi.module(it, "${fabric_api_version}+${platform.mcVersionStr}"))
	}

  // essential dependencies
	include(modRuntimeOnly("gg.essential:loader-fabric:1.0.0")!!)
	compileOnly("gg.essential:essential-$platform:12328+g551779957")

	// mod menu
	modApi("com.terraformersmc:modmenu:${mod_menu_version}")
}

loom.noServerRunConfigs()

tasks.processResources {
  val expansions = mapOf(
    "minecraft_version" to platform.mcVersionStr,
    "mod_id" to mod_id,
    "mod_version" to mod_version,
    "mod_name" to mod_name,
    "mod_description" to mod_description,
    "maven_group" to maven_group,
    "fabric_loader_version" to fabric_loader_version,
    "fabric_kotlin_version" to fabric_kotlin_version,
    "fabric_api_version" to fabric_api_version
  )
  filesMatching(listOf("fabric.mod.json")) {
    expand(expansions)
  }
}

tasks.jar { from("LICENSE") }

tasks.compileKotlin {
  kotlinOptions.freeCompilerArgs +=
    listOf(
      "-opt-in=kotlin.RequiresOptIn",
      "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
      "-Xno-param-assertions",
      "-Xjvm-default=all"
    )
  kotlinOptions.jvmTarget = "17"
}

modrinth {
  token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
  projectId.set("b6qJY4kH")
  if (System.getenv("PROD") != null) {
    versionType.set("release")
    versionNumber.set(mod_version)
  } else {
    versionType.set("alpha")
    versionNumber.set("${mod_version}+${System.getenv("GITHUB_SHA")}")
  }
  uploadFile.set(tasks.jar.get())
  loaders.add("fabric")
  dependencies {
    embedded.project("essential")
    required.version("fabric-language-kotlin", fabric_kotlin_version)
    required.version("fabric-api", "${fabric_api_version}+${platform.mcVersionStr}")
    optional.version("modmenu", mod_menu_version)
  }
}
