package net.usbwire.usbplus.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.PropertyType
import gg.essential.universal.UResolution
import java.awt.Color
import java.io.File
import java.nio.file.Path
import net.minecraft.client.gui.screen.Screen
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.features.Poi
import net.usbwire.usbplus.features.HealthHud
import net.usbwire.usbplus.util.Util

val configFile = "${USBPlus.configPath}/config.toml"

object Config : Vigilant(File(configFile)) {
  // *POI*
  var poiEnabled = false
  var poiUrl = "https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json" // github url

  // *Common Health*
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
  var healthDrawX = 0
  var healthDrawY = 0
  var healthDrawAlign = 0
  var healthDrawAlignExtra = 0
  var healthDrawScale = 1.0f
  var healthDrawDamageEnabled = false
  var healthDrawDamageDelay = 40

  init {
    Util.createDirectory(Path.of(configFile))

    category("POI") {
      switch(::poiEnabled, "Toggle POI") { Poi.changeState(it) }
      text(::poiUrl, "Internal POI URL", "Should not be changed unless you know what you are doing!")
      button("Refresh POIs", "Fetches from ${poiUrl} for the latest data") { Poi.fetchPoiData() }
    }

    category("Health") {
      subcategory("Hitbox") {
        switch(::healthEnabled, "Toggle GlowHealth")
        switch(::healthHitboxCancel, "Cancel other entities!")
      }
      subcategory("Draw") {
        switch(::healthDrawEnabled, "Toggle DrawHealth")
        slider(::healthDrawX, "X Position in Pixels", min = 0, max = USBPlus.mc.getWindow().getWidth(), triggerActionOnInitialization = false) {
          HealthHud.xPos.set(it)
        }
        slider(::healthDrawY, "Y Position in Pixels", min = 0, max = USBPlus.mc.getWindow().getHeight(), triggerActionOnInitialization = false) {
          HealthHud.yPos.set(it)
        }
        selector(::healthDrawAlign, "Text Alignment", options = listOf("left", "center", "right"), triggerActionOnInitialization = false) {
          HealthHud.alignPos.set(it)
        }
        selector(::healthDrawAlignExtra, "Extra Alignment", options = listOf("left", "center", "right"), triggerActionOnInitialization = false) {
          HealthHud.alignExtra.set(it)
        }
        decimalSlider(::healthDrawScale, "Text Scale", min = 0.5f, max = 4.0f, decimalPlaces = 2, triggerActionOnInitialization = false) {
           HealthHud.textSize.set(it)
        }
        switch(::healthDrawDamageEnabled, "Display Recent Damage")
        slider(::healthDrawDamageDelay, "Hide Delay in Ticks", min = 1, max = 60)
      }
      subcategory("General") {
        switch(::healthHurtEnabled, "Hurt Toggle")
        switch(::healthEffectEnabled, "Effect Toggle")
        slider(::healthUpdateTicks, "Update Rate In Ticks", min = 1, max = 20)
        percentSlider(::healthGoodPercent, "Good HP percent", "100%% HP")
        percentSlider(::healthLowPercent, "Low HP percent", "70%% HP")
        percentSlider(::healthCriticalPercent, "Critical HP percent", "40%% HP")
      }
      subcategory("Color") {
        color(::healthBaseColor, "Base HP color", "White (#ffffff) doesn't show.")
        color(::healthGoodColor, "Good HP color")
        color(::healthLowColor, "Low HP color")
        color(::healthCriticalColor, "Critical HP color")
        color(::healthHurtColor, "Hurt color")
        color(::healthEffectColor, "Negative effect (Wither/Poison) color")
      }
    }

    initialize() // this needs to be called for whatever reason so that configs actually save
  }
}

class ModMenu : ModMenuApi {
  override fun getModConfigScreenFactory() = ConfigScreenFactory<Screen> { Config.gui() }
}
