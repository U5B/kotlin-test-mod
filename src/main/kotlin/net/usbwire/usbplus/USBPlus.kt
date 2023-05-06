package net.usbwire.usbplus

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.usbwire.usbplus.commands.ConfigCommand
import net.usbwire.usbplus.features.Poi
import net.usbwire.usbplus.util.MixinHelper
import org.slf4j.LoggerFactory

object USBPlus : ClientModInitializer {
  // get this from gradle.properties
  val fabricmodjson by lazy { FabricLoader.getInstance().getModContainer("usbplus").get() }
  val modid: String by lazy { fabricmodjson.metadata.id }
  val name: String by lazy { fabricmodjson.metadata.name }
  val configPath: String = "./config/${modid}"
  val logger = LoggerFactory.getLogger(modid)
  // https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/Skytils.kt#L122
  @JvmStatic val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }

  override fun onInitializeClient() {
    ConfigCommand.register()
    MixinHelper.init()
  }
}
