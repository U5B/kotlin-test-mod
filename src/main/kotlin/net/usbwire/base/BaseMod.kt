package net.usbwire.base

import java.util.UUID
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.network.MessageType
import net.minecraft.text.Text
import net.usbwire.base.access.InGameHudAccess
import net.usbwire.base.commands.BaseCommand
import net.usbwire.base.features.Poi
import org.slf4j.LoggerFactory

object BaseMod : ClientModInitializer {
  // get this from gradle.properties
  public val modid: String = "usb_base" // base_name
  public val name: String = "BaseMod"
  public val configPath: String = "./config/${modid}"
  public val logger = LoggerFactory.getLogger(modid)
  @JvmStatic val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }

  override fun onInitializeClient() {
    logger.info("Hello Fabric world!")
    BaseCommand.register()
    Poi.changeState()
    // ClientLifecycleEvents.CLIENT_STARTED.register { client -> run { initChat(client) } }
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