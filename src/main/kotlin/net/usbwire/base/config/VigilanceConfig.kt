package net.usbwire.base.config

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType

import net.usbwire.base.BaseMod
import net.usbwire.base.util.Util

val configFile = "${BaseMod.configPath}/config.toml"
object VigilanceConfig : Vigilant(File(configFile)) {
  @Property(type = PropertyType.CHECKBOX, name = "poiEnabled", category = "Test")
  var poiEnabled = false

  init {
    Util.createPath(Path.of(configFile))

    registerListener("poiEnabled") { value: Boolean ->
      BaseMod.poi.changeState(value)
    }
  }
}