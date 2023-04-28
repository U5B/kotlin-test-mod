package net.usbwire.base

import java.util.UUID
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.network.MessageType
import net.minecraft.text.Text
import net.minecraft.client.world.ClientWorld
import net.usbwire.base.access.InGameHudAccess
import net.usbwire.base.commands.BaseCommand
import net.usbwire.base.features.Poi
import net.usbwire.base.features.Compass
import org.slf4j.LoggerFactory
import gg.essential.universal.UMatrixStack

object BaseMod : ClientModInitializer {
  // get this from gradle.properties
  val modid: String = "usb_base" // base_name
  val name: String = "BaseMod"
  val configPath: String = "./config/${modid}"
  val logger = LoggerFactory.getLogger(modid)
  // https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/Skytils.kt#L122
  @JvmStatic val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }

  override fun onInitializeClient() {
    logger.info("Hello Fabric world!")
    BaseCommand.register()
    Poi.changeState()
    // ClientLifecycleEvents.CLIENT_STARTED.register { client -> run { initChat(client) } }
    ClientTickEvents.START_WORLD_TICK.register { clientWorld -> run { worldTick(clientWorld) }}
  }

  fun worldTick (clientWorld: ClientWorld) {
    Compass.onTick()
  }

  fun initChat(client: MinecraftClient) {
    val inGameHud: InGameHudAccess = client.inGameHud as InGameHudAccess
    inGameHud.registerChatListener(
        MessageType.CHAT,
        { type: MessageType, message: Text, sender: UUID -> run { onChat(type, message, sender) } }
    )
    inGameHud.registerChatListener(
        MessageType.SYSTEM,
        { type: MessageType, message: Text, sender: UUID -> run { onChat(type, message, sender) } }
    )
    inGameHud.registerChatListener(
      MessageType.GAME_INFO,
      { type: MessageType, message: Text, sender: UUID -> run { onChat(type, message, sender) } }
  )
  }

  fun onChat(type: MessageType, message: Text, sender: UUID) {
    logger.info(String.format("%s:: %s", sender.toString(), message.toString()))
    if (message.style?.clickEvent != null) logger.info(message.style.clickEvent.toString())
  }
}

open class BaseFeature {
  fun onTick () {}

  fun onWorldTick () {}

  fun onRenderTick () {}

  fun init () {}

  fun onChat () {}
}
