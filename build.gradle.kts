import gg.essential.gradle.util.noServerRunConfigs
plugins {
	kotlin("jvm") version "1.8.20"
	id("gg.essential.defaults.loom") version "0.1.18"
	id("gg.essential.defaults") version "0.1.18"
}

val base_name: String by project
val mod_version: String by project
val maven_group: String by project

val minecraft_version: String by project
val fabric_version: String by project
val fabric_kotlin_version: String by project
val loader_version: String by project

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
	maven("https://repo.essential.gg/repository/maven-public/")
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	// Fabric API. This is technically optional, but you probably want it anyway.
	// modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
	modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")
	// Uncomment the following line to enable the deprecated Fabric API modules.
	// These are included in the Fabric API production distribution and allow you to update your mod to the latest modules at a later more convenient time.

	// modImplementation "net.fabricmc.fabric-api:fabric-api-deprecated:${project.fabric_version}"

	// essential dependencies
	"include"("modRuntimeOnly"("gg.essential:loader-fabric:1.0.0")!!)
	compileOnly("gg.essential:essential-1.18.2-fabric:12328+g551779957")

	// mod menu
	modApi("com.terraformersmc:modmenu:3.2.5")
}
tasks.compileKotlin {
	kotlinOptions {
		freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xno-param-assertions", "-Xjvm-default=all-compatibility")
		jvmTarget = "17"
}
}
tasks {
	processResources {
		val expansions = mapOf(
			"mod_version" to mod_version,
			"base_name" to base_name,
			"loader_version" to loader_version,
			"minecraft_version" to minecraft_version,
		)

		// inputs.property("mod_version_expansions", expansions)
		filesMatching(listOf("mcmod.info", "META-INF/mods.toml", "fabric.mod.json")) {
			expand(expansions)
		}
	}

	jar {
		from("LICENSE")
	}

	compileKotlin {
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
	launchConfigs {
			getByName("client") {
					arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
			}
	}
}


/*
java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()
}
*/
