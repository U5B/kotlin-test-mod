pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
    maven("https://jitpack.io")
		maven("https://maven.fabricmc.net")
		maven("https://maven.architectury.dev/")
		maven("https://maven.minecraftforge.net")
		maven("https://repo.essential.gg/repository/maven-public")
	}
  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "gg.essential.defaults" -> useModule("com.github.Skytils.essential-gradle-toolkit:essential-gradle-toolkit:a243c8ed83")
        "gg.essential.multi-version" -> useModule("com.github.Skytils.essential-gradle-toolkit:essential-gradle-toolkit:a243c8ed83")
        "gg.essential.multi-version.root" -> useModule("com.github.Skytils.essential-gradle-toolkit:essential-gradle-toolkit:a243c8ed83")
      }
    }
  }
}


val mod_id: String by settings
val mod_version: String by settings
rootProject.name = "${mod_id}-${mod_version}"
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.18.2-fabric",
    "1.19.2-fabric",
    "1.19.3-fabric",
    "1.19.4-fabric",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
