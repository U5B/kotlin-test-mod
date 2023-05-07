pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		maven("https://maven.fabricmc.net")
		maven("https://maven.architectury.dev/")
		maven("https://maven.minecraftforge.net")
		maven("https://repo.essential.gg/repository/maven-public")
		maven("https://repo.sk1er.club/repository/maven-releases/")
	}
}

val mod_id: String by settings
val mod_version: String by settings
rootProject.name = "${mod_id}-${mod_version}"
