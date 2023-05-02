package net.usbwire.base

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.usbwire.base.commands.BaseCommand
import net.usbwire.base.features.Poi
import net.usbwire.base.util.MixinHelper
import org.slf4j.LoggerFactory

object BaseMod : ClientModInitializer {
  // get this from gradle.properties
  val fabricmodjson by lazy { FabricLoader.getInstance().getModContainer("usb").get() }
  val modid: String by lazy { fabricmodjson.metadata.id }
  val name: String by lazy { fabricmodjson.metadata.name }
  val configPath: String = "./config/${modid}"
  val logger = LoggerFactory.getLogger(modid)
  // https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/Skytils.kt#L122
  @JvmStatic val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }

  override fun onInitializeClient() {
    logger.info("Hello Fabric world!")
    BaseCommand.register()
    Poi.changeState()
    MixinHelper.init()
  }
}
