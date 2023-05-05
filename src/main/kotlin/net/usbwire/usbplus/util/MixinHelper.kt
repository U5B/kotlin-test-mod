package net.usbwire.usbplus.util

import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.UTextComponent
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.text.Text
import net.usbwire.usbplus.features.Compass
import net.usbwire.usbplus.features.Health
import net.usbwire.usbplus.features.HealthHud

object MixinHelper {
  fun init() {
    ClientTickEvents.START_WORLD_TICK.register { clientWorld -> run { worldTick(clientWorld) } }
    // WorldRenderEvents.LAST.register { test -> run  { renderTick(test) }}
    HudRenderCallback.EVENT.register { matrix, ticks -> run { hudRender(matrix, ticks) } }
  }

  fun worldTick(clientWorld: ClientWorld) {
    HealthHud.onWorldTick(clientWorld)
    Compass.onWorldTick()
  }

  fun renderTick(test: WorldRenderContext) {}

  fun hudRender(matrixStack: MatrixStack, ticks: Float) {
    val matrix = UMatrixStack(matrixStack)
    HealthHud.draw(matrix)
  }

  fun renderHitbox(matrix: MatrixStack, vertex: VertexConsumer, entity: Entity): Boolean {
    return Health.renderHitbox(matrix, vertex, entity)
  }

  fun onMessage(mcText: Text, id: Int, ticks: Int, refresh: Boolean): Boolean {
    val message = UTextComponent(mcText)
    return false
  }
}
