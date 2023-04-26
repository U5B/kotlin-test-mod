package net.usbwire.base.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File
import java.nio.file.Path
import net.usbwire.base.BaseMod
import net.usbwire.base.util.Util
import net.usbwire.base.features.Poi
import gg.essential.universal.UChat
import java.awt.Color
import kotlin.math.PI


val configFile = "${BaseMod.configPath}/config.toml"

object VigilanceConfig : Vigilant(File(configFile)) {
  @Property(type = PropertyType.CHECKBOX, name = "Toggle POI Module", description = "", category = "POI")
  var poiEnabled = false
  @Property(type = PropertyType.BUTTON, name = "Update to latest pois.json", description = "Fetches from https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json for the latest data.", category = "POI")
  fun poiButton() = Poi.fetchPoiData()
  @Property(type = PropertyType.CHECKBOX, name = "Toggle GlowHealth Module", description = "", category = "GlowHealth")
  var healthEnabled = false
  @Property(type = PropertyType.CHECKBOX, name = "Force Hitboxes On", description = "This will cause F3+B to be forced on.", category = "GlowHealth")
  var healthForcedHitbox = false


  @Property(type = PropertyType.COLOR, name = "Base Health Color", description = "By default, white does not show hitbox. Change the color here to make hitboxes show.", category = "GlowHealth")
  var healthBaseColor = Color.WHITE

  @Property(type = PropertyType.COLOR, name = "Good Health Color", description = "", category = "GlowHealth")
  var healthGoodColor = Color.GREEN
  @Property(type = PropertyType.PERCENT_SLIDER, name = "Good Health Percentage", description = "", category = "GlowHealth")
  var healthGoodPercent = 1.0f

  @Property(type = PropertyType.COLOR, name = "Low Health Color", description = "", category = "GlowHealth")
  var healthLowColor = Color.YELLOW
  @Property(type = PropertyType.PERCENT_SLIDER, name = "Low Health Percentage", description = "", category = "GlowHealth")
  var healthLowPercent = 0.7f

  @Property(type = PropertyType.COLOR, name = "Critical Health Color", description = "", category = "GlowHealth")
  var healthCriticalColor = Color.RED
  @Property(type = PropertyType.PERCENT_SLIDER, name = "Critical Health Percentage", description = "", category = "GlowHealth")
  var healthCriticalPercent = 0.4f

  init {
    Util.createDirectory(Path.of(configFile))

    registerListener("poiEnabled") { value: Boolean -> Poi.changeState(value) }
    initialize() // this needs to be called for whatever reason so that configs actually save
  }

  @JvmStatic
  fun healthForcedHitbox(): Boolean {
    return healthForcedHitbox
  }
}
