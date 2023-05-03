package net.usbwire.base.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.PropertyType
import java.awt.Color
import java.io.File
import java.nio.file.Path
import net.minecraft.client.gui.screen.Screen
import net.usbwire.base.BaseMod
import net.usbwire.base.features.Poi
import net.usbwire.base.features.HealthHud
import net.usbwire.base.util.Util

val configFile = "${BaseMod.configPath}/config.toml"

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
  var healthHurtColor = Color.PINK
  var healthEffectEnabled = false
  var healthEffectColor = Color.DARK_GRAY

  // *GlowHealth*
  var healthEnabled = false
  var healthHitboxCancel = false

  // *DrawHealth*
  var healthDrawEnabled = false
  var healthDrawX = 0.0f
  var healthDrawY = 0.0f
  var healthDrawAlign = 0

  init {
    Util.createDirectory(Path.of(configFile))

    category("POI") {
      switch(::poiEnabled, "Toggle POI") { Poi.changeState(it) }
      button("Refresh POIs", "Fetches from ${poiUrl} for the latest data") { Poi.fetchPoiData() }
      text(::poiUrl, "Internal POI URL", "Should not be changed unless you know what you are doing!")
    }

    category("Health") {
      subcategory("Hitbox") { 
        switch(::healthEnabled, "Toggle GlowHealth")
        switch(::healthHitboxCancel, "Cancel other entities!")
      }
      subcategory("Draw") {
        switch(::healthDrawEnabled, "Toggle DrawHealth")
        percentSlider(::healthDrawX, "X Position in Pixels") { HealthHud.xPos.set(it) }
        percentSlider(::healthDrawY, "Y Position in Pixels") { HealthHud.yPos.set(it) }
        selector(::healthDrawAlign, "Text Alignment", options = listOf("left", "center", "right"))
      }
      subcategory("General") {
        switch(::healthHurtEnabled, "Hurt Toggle")
        switch(::healthEffectEnabled, "Effect Toggle")
        slider(::healthUpdateTicks, "Update Rate In Ticks", min = 1, max = 20) // really?
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
