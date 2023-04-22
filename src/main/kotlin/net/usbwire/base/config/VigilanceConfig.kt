package net.usbwire.base.config

import java.io.File
import gg.essential.vigilance.Vigilant

import net.usbwire.base.BaseMod

object VigilanceConfig : Vigilant(File("${BaseMod.configPath}/config.toml"), "${BaseMod.name}") {
  var testValue = false

  init {
    category("foxgirl!") {
      checkbox(::testValue, "Test value", "This is a test!")
    }
  }
}