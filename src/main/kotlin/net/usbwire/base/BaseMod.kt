package net.usbwire.base

import net.fabricmc.api.ClientModInitializer
import net.usbwire.base.commands.BaseCommand
import net.usbwire.base.features.Poi
import org.slf4j.LoggerFactory

object BaseMod : ClientModInitializer {
  // get this from gradle.properties
  public val modid: String = "usb_base" // base_name
  public val name: String = "BaseMod"
  public val configPath: String = "./config/${modid}"
  public val logger = LoggerFactory.getLogger(modid)

  override fun onInitializeClient() {
    logger.info("Hello Fabric world!")
    BaseCommand.register()
    Poi.changeState()
  }
}
