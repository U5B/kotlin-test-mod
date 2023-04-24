package net.usbwire.base.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File
import java.nio.file.Path
import net.usbwire.base.BaseMod
import net.usbwire.base.util.Util
import net.usbwire.base.features.Poi

val configFile = "${BaseMod.configPath}/config.toml"

object VigilanceConfig : Vigilant(File(configFile)) {
  @Property(type = PropertyType.CHECKBOX, name = "enabled", category = "POI") var poiEnabled = false

  init {
    Util.createDirectory(Path.of(configFile))

    registerListener("poiEnabled") { value: Boolean -> Poi.changeState(value) }
    initialize() // this needs to be called for whatever reason so that configs actually save
  }
}
