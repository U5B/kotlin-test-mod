package net.usbwire.base.util

import java.awt.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.client.render.WorldRenderer

import net.usbwire.base.config.VigilanceConfig

object Hitbox {
  fun renderHitbox(
      matrices: MatrixStack,
      vertices: VertexConsumer,
      entity: Entity,
      tickDelta: Float
  ): Boolean {
    val box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());
    val red = 255.toFloat()
    val green = 255.toFloat()
    val blue = 255.toFloat()
    val alpha = 1.toFloat()
    WorldRenderer.drawBox(matrices, vertices, box, red, green, blue, alpha)
    return true
  }
}
