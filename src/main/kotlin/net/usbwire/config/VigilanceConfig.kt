package net.usbwire.config

import java.io.File
import gg.essential.vigilance.Vigilant

import net.usbwire.Settings

val base_name = Settings().base_name

object VigilanceConfig : Vigilant(File("${base_name}.toml", "${base_name}.toml")) {
  var testValue = false

  init {
    category("foxgirl!") {
      checkbox(::testValue, "Test value", "This is a test!")
    }
  }
}