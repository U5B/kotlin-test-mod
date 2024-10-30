import gg.essential.gradle.util.*

plugins {
  id("gg.essential.multi-version")
  id("gg.essential.defaults")
  id("com.modrinth.minotaur") version "2.+"
  id("java")
}

val mod_id: String by project
val mod_version: String by project
val mod_name: String by project
val mod_description: String by project
val maven_group: String by project

val fabric_loader_version: String by project
val fabric_api_version: String by project
val mod_menu_version: String by project

val version = if (System.getenv("PROD") == null) {
  if (System.getenv("GITHUB_SHA") != null) {
    "${mod_version}+${System.getenv("GITHUB_SHA").substring(0, 8)}"
  } else "${mod_version}+local"
} else "${mod_version}"

val baseJarName = if (System.getenv("PROD") == null) {
  if (System.getenv("GITHUB_SHA") != null) {
    "${mod_id}-${mod_version}+${platform.mcVersionStr}+${System.getenv("GITHUB_SHA").substring(0, 8)}"
  } else "${mod_id}-${mod_version}+${platform.mcVersionStr}+local"
} else "${mod_id}-${mod_version}+${platform.mcVersionStr}"

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from
	// automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
	maven("https://repo.essential.gg/repository/maven-public/")
	maven("https://maven.terraformersmc.com/releases/")
  maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

dependencies {
	// fabric api
	setOf(
		"fabric-api-base",
		"fabric-networking-api-v1",
		"fabric-lifecycle-events-v1",
		"fabric-rendering-v1",
    "fabric-screen-api-v1",
    "fabric-screen-handler-api-v1"
	).forEach {
		// Add each module as a dependency
		modImplementation(fabricApi.module(it, "${fabric_api_version}+${platform.mcVersionStr}"))
	}

  // essential dependencies
	include(modRuntimeOnly("gg.essential:loader-fabric:1.0.0")!!)
  // https://repo.essential.gg/repository/maven-releases/gg/essential/essential-1.18.2-fabric/maven-metadata.xml
	modCompileOnly("gg.essential:essential-$platform:17141+gd6f4cfd3a8")

	// mod menu
	modApi("com.terraformersmc:modmenu:${mod_menu_version}")

  // devauth
  modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:1.1.2")
}

loom.noServerRunConfigs()

tasks.processResources {
  val expansions = mapOf(
    "minecraft_version" to platform.mcVersionStr,
    "mod_id" to mod_id,
    "mod_version" to version,
    "mod_name" to mod_name,
    "mod_description" to mod_description,
    "maven_group" to maven_group,
    "fabric_loader_version" to fabric_loader_version,
    "fabric_api_version" to fabric_api_version
  )
  filesMatching(listOf("fabric.mod.json")) {
    expand(expansions)
  }
}

tasks.jar {
  archiveBaseName.set(baseJarName)
  from("LICENSE")
}

tasks.remapJar {
  archiveBaseName.set(baseJarName)
}

// loom {
//   launchConfigs {
//     getByName("client") {
//       property("devauth.enabled", "true")
//     }
//   }
// }

modrinth {
  token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
  projectId.set("b6qJY4kH")
  if (System.getenv("PROD") != null) {
    versionType.set("release")
  } else {
    versionType.set("alpha")
  }
  versionName.set("[${platform.mcVersionStr}] ${mod_name} ${version}")
  versionNumber.set(version)
  if (System.getenv("DEBUG") != null) {
    debugMode.set(true)
  }
  // modrinth can't find the file properly
  uploadFile.set(file("build/libs/${baseJarName}.jar"))
  loaders.add("fabric")
  // should be whatever the base version is
  if (platform.mcVersionStr == "1.19.4" && System.getenv("CHANGELOG") != null) {
    changelog.set(System.getenv("CHANGELOG"))
  } else {
    changelog.set("")
  }
  dependencies {
    embedded.project("essential")
    required.project("fabric-api")
    optional.project("modmenu")
  }
}
