package net.usbwire.usbplus.config

import com.terraformersmc.modmenu.api.*
import gg.essential.vigilance.Vigilant
import net.minecraft.client.gui.screen.Screen
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.features.*
import net.usbwire.usbplus.util.Util
import java.awt.Color
import java.io.File
import java.nio.file.Path

val configFile = "${USBPlus.configPath}/config.toml"

object Config : Vigilant(File(configFile)) {
	// *POI*
	var poiEnabled = false
	var poiUrl = "https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json" // github url

	// *Common Health*
	var healthWhitelistEnabled = false
	var healthWhitelist = ""
	var healthUpdateTicks = 1
	var healthBaseColor = Color.WHITE
	var healthGoodColor = Color.GREEN
	var healthGoodPercent = 1.0f
	var healthLowColor = Color.YELLOW
	var healthLowPercent = 0.7f
	var healthCriticalColor = Color.RED
	var healthCriticalPercent = 0.4f

	var healthHurtEnabled = false
	var healthHurtColor = Color.ORANGE
	var healthEffectEnabled = false
	var healthEffectColor = Color.GRAY

	// *GlowHealth*
	var healthEnabled = false
	var healthHitboxCancel = false

	// *DrawHealth*
	var healthDrawEnabled = false
	var healthDrawX = 0.0f
	var healthDrawY = 0.0f
	var healthDrawAlign = 0.0f
	var healthDrawAlignExtraRight = false
	var healthDrawScale = 1.0f
	var healthDrawDamageEnabled = false
	var healthDrawDamageDelay = 10
	var healthDrawSort = 0

	init {
		Util.createDirectory(Path.of(configFile))

		category("POI") {
			switch(::poiEnabled, "Toggle POI") { Poi.changeState(it) }
			text(::poiUrl, "Internal POI URL", "Should not be changed unless you know what you are doing!")
			button("Refresh POIs", "Fetches from ${poiUrl} for the latest data") { Poi.fetchPoiData() }
		}

    category("Health Colors") {
      subcategory("Color") {
				color(::healthBaseColor, "Base HP color", "White (#ffffff) doesn't show.")
				color(::healthGoodColor, "Good HP color")
        percentSlider(::healthGoodPercent, "Good HP percent", "100%% HP")
				color(::healthLowColor, "Low HP color")
        percentSlider(::healthLowPercent, "Low HP percent", "70%% HP")
				color(::healthCriticalColor, "Critical HP color")
        percentSlider(::healthCriticalPercent, "Critical HP percent", "40%% HP")
        switch(::healthHurtEnabled, "Hurt Color Toggle")
				color(::healthHurtColor, "Hurt color")
        switch(::healthEffectEnabled, "Fire Color Toggle")
				color(::healthEffectColor, "Fire Color")
			}
    }

		category("Health Draw") {
      subcategory("Draw") {
				switch(::healthDrawEnabled, "Toggle DrawHealth")
				percentSlider(::healthDrawX, "X Position Percent", triggerActionOnInitialization = false) {
					HealthHud.xPos.set(it)
					HealthHud.configDirty = true
				}
				percentSlider(::healthDrawY, "Y Position Percent", triggerActionOnInitialization = false) {
					HealthHud.yPos.set(it)
					HealthHud.configDirty = true
				}
				percentSlider(::healthDrawAlign, "Text Alignment Percent", triggerActionOnInitialization = false) {
					HealthHud.alignPos.set(it)
					HealthHud.configDirty = true
				}
				decimalSlider(::healthDrawScale, "Text Scale", min = 0.5f, max = 4.0f, decimalPlaces = 2, triggerActionOnInitialization = false) {
					HealthHud.textSize.set(it)
					HealthHud.configDirty = true
				}
				switch(::healthDrawDamageEnabled, "Display Recent Damage")
				slider(::healthDrawDamageDelay, "Damage Hide Delay in Ticks", min = 1, max = 60)
				switch(::healthDrawAlignExtraRight, "Recent Damage Alignment", triggerActionOnInitialization = false) {
					HealthHud.alignRightExtra.set(it)
					HealthHud.configDirty = true
				}
				selector(::healthDrawSort, "Sort player list by", options = listOf("alphabetical", "health", "time"))
			}
		}

		category("Health General") {
			subcategory("Hitbox") {
				switch(::healthEnabled, "Toggle BoxHealth", "F3+B Hitboxes must be ON and color must not be white!")
				switch(::healthHitboxCancel, "Disable rendering hitboxes on entities other than players!")
			}
			subcategory("General") {
				slider(::healthUpdateTicks, "Update Rate In Ticks", min = 1, max = 20)
				switch(::healthWhitelistEnabled, "Toggle Whitelist")
				paragraph(::healthWhitelist, "Player names separated by spaces to allow")
			}
		}

		initialize() // this needs to be called for whatever reason so that configs actually save
	}
}

class ModMenu : ModMenuApi {
	override fun getModConfigScreenFactory() = ConfigScreenFactory<Screen> { Config.gui() }
}
