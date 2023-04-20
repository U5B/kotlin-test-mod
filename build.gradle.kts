// Thanks to: https://github.com/myoun/fabric-example-mod-kotlin-with-kotlin-dsl/blob/master/build.gradle.kts
// https://github.com/EssentialGG/essential-gradle-toolkit

plugins {
	println("c!")
	// id("fabric-loom") version "1.1-SNAPSHOT"
	id("org.jetbrains.kotlin.jvm") version "1.8.20"
	// id("gg.essential.defaults.maven-publish") version "+"
	id("gg.essential.multi-version.root")
	id("gg.essential.multi-version")
	id("gg.essential.defaults")
}

val base_name: String by project
val mod_version: String by project
val maven_group: String by project

val minecraft_version: String by project
val fabric_version: String by project
val fabric_kotlin_version: String by project
val yarn_mappings: String by project
val loader_version: String by project

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
}

dependencies {
	// To change the versions see the gradle.properties file
	// minecraft("com.mojang:minecraft:$minecraft_version")
	// mappings("net.fabricmc:yarn:$yarn_mappings:v2")
	// modImplementation("net.fabricmc:fabric-loader:$loader_version")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
	modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")
	// Uncomment the following line to enable the deprecated Fabric API modules. 
	// These are included in the Fabric API production distribution and allow you to update your mod to the latest modules at a later more convenient time.

	// modImplementation "net.fabricmc.fabric-api:fabric-api-deprecated:${project.fabric_version}"

	// essential dependencies
	modImplementation("gg.essential:vigilance-$minecraft_version-fabric:284")
	modImplementation("gg.essential:elementa-$minecraft_version-fabric:580")
	modImplementation("gg.essential:universalcraft-$minecraft_version-fabric:262")
}

/*
tasks {
	processResources {
		inputs.property("version", mod_version)
		filesMatching("fabric.mod.json") {
				expand(mutableMapOf("version" to mod_version))
		}
	}

	jar {
		from("LICENSE")
	}

	compileKotlin {
		kotlinOptions.jvmTarget = "17"
	}

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
*/

loom {

}

/*
java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()
}
*/
