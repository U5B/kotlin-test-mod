package net.usbwire.base.util

import java.awt.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

import net.usbwire.base.config.VigilanceConfig
import net.usbwire.base.features.Health
import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.text.Text

object MixinHelper {
  fun renderHitbox(
      matrix: MatrixStack,
      vertex: VertexConsumer,
      entity: Entity
  ) : Boolean {
    return Health.renderHitbox(matrix, vertex, entity)
  }

  fun onMessage(
    mcText: Text, 
    id: Int,
    ticks: Int,
    refresh: Boolean
  ) : Boolean {
    val message = UTextComponent(mcText)
    return false
  }
}
