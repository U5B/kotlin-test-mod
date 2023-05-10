import gg.essential.gradle.util.*

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("gg.essential.multi-version")
  id("gg.essential.defaults")
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
	// modImplementation("net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}")

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
	"include"("modRuntimeOnly"("gg.essential:loader-fabric:1.0.0")!!)
	compileOnly("gg.essential:essential-$platform:12328+g551779957")

	// mod menu
	modApi("com.terraformersmc:modmenu:3.2.5")
}

java.withSourcesJar()
loom.noServerRunConfigs()

tasks {
	processResources {
		val expansions = project.properties
		filesMatching(listOf("fabric.mod.json")) {
			"minecraft_version" to platform.mcVersionStr
			expand(expansions)
		}
	}

	jar { from("LICENSE") }

	compileKotlin {
		kotlinOptions.freeCompilerArgs +=
			listOf(
				"-opt-in=kotlin.RequiresOptIn",
				"-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
				"-Xno-param-assertions",
				"-Xjvm-default=all"
			)
		kotlinOptions.jvmTarget = "17"
	}
}

/*
loom {
	launchConfigs {
		getByName("client") {
			property("elementa.dev", "true")
			property("elementa.debug", "true")
			property("elementa.invalid_usage", "warn")
			property("mixin.debug.verbose", "true")
			property("mixin.debug.export", "true")
			property("mixin.dumpTargetOnFailure", "true")
			property("legacy.debugClassLoading", "true")
			property("legacy.debugClassLoadingSave", "true")
			property("legacy.debugClassLoadingFiner", "true")
		}
	}
	runConfigs {
		getByName("client") {
			isIdeConfigGenerated = true
		}
		remove(getByName("server"))
	}
}
*/
