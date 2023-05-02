package net.usbwire.base

import java.util.UUID
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.minecraft.client.MinecraftClient
import net.minecraft.network.MessageType
import net.minecraft.text.Text
import net.minecraft.client.world.ClientWorld
import net.minecraft.client.util.math.MatrixStack
import net.usbwire.base.access.InGameHudAccess
import net.usbwire.base.commands.BaseCommand
import net.usbwire.base.features.Poi
import net.usbwire.base.features.Compass
import net.usbwire.base.features.Health
import net.usbwire.base.features.HealthHud
import org.slf4j.LoggerFactory
import gg.essential.universal.UMatrixStack

object BaseMod : ClientModInitializer {
  // get this from gradle.properties
  val modid: String = "usb" // base_name
  val name: String = "usb.kt"
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
    // WorldRenderEvents.LAST.register { test -> run  { renderTick(test) }}
    HudRenderCallback.EVENT.register { matrix, ticks -> run  { hudRender(matrix, ticks) }}
  }

  fun worldTick (clientWorld: ClientWorld) {
    Compass.onTick()
  }

  fun renderTick (test : WorldRenderContext) {
    if (mc.world == null) return
    for (entity in mc.world!!.players) {
      Health.testHitbox(entity, test)
    }
  }

  fun hudRender (matrixStack: MatrixStack, ticks: Float) {
    val matrix = UMatrixStack(matrixStack)
    HealthHud.draw(matrix)
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
