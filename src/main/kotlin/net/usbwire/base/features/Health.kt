package net.usbwire.base.features

import java.awt.Color
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import com.mojang.blaze3d.systems.RenderSystem

import net.usbwire.base.config.VigilanceConfig
import net.usbwire.base.BaseMod
import net.usbwire.base.util.RenderUtil
import gg.essential.universal.UMatrixStack
import gg.essential.universal.vertex.UVertexConsumer
import gg.essential.universal.UGraphics

object Health {
  fun renderHitbox(
      matrix: MatrixStack,
      vertex: VertexConsumer,
      entity: Entity
  ) : Boolean {
    if (VigilanceConfig.healthEnabled == false) return false
    if (entity !is PlayerEntity) return true
    val color = checkHealth(entity)
    if (color == Color.WHITE) return true
    val box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ())
    val red = color.red / 255.0F
    val green = color.green / 255.0F
    val blue = color.blue / 255.0F
    val alpha = color.alpha / 255.0F
    WorldRenderer.drawBox(matrix, vertex, box, red, green, blue, alpha)
    return true
  }

  fun testHitbox (entity: Entity, ticks: Float) {
    if (entity !is PlayerEntity) return
    val color = checkHealth(entity)
    if (color == Color.WHITE) return
    val box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ())
    RenderUtil.drawBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color, ticks)
  }

  fun checkHealth (entity: PlayerEntity) : Color {
    val health = entity.health
    val maxHealth = entity.maxHealth
    val percentHealth = health / maxHealth
    if (percentHealth >= VigilanceConfig.healthGoodPercent) {
      return VigilanceConfig.healthBaseColor
    } else if (percentHealth >= VigilanceConfig.healthLowPercent && percentHealth <= VigilanceConfig.healthGoodPercent) {
      return VigilanceConfig.healthGoodColor
    } else if (percentHealth >= VigilanceConfig.healthCriticalPercent && percentHealth <= VigilanceConfig.healthLowPercent) {
      return VigilanceConfig.healthLowColor
    } else if (percentHealth <= VigilanceConfig.healthCriticalPercent) { // assuming its red
      return VigilanceConfig.healthCriticalColor
    }
    return VigilanceConfig.healthBaseColor // fallback if something went wrong!
  }
}
