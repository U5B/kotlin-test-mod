package net.usbwire.base.util

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.vertex.UVertexConsumer

import java.awt.Color


object RenderUtil {

  /**
   * Draw a box!
   * @see https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L163
   * @see https://github.com/Splzh/ClearHitboxes/blob/5439f1e3f789e35371939f4bc72ab0fb4eb7d2aa/src/main/java/splash/utils/BoxUtils.java#L74
   * @author Mojang
   */
  fun drawBox (x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: Color) {
    val matrix = UMatrixStack()
    val vertexConsumer = UGraphics.getFromTessellator()

    vertexConsumer.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)
    matrix.push()
  
    vertexConsumer.pos(matrix, x1, y1, z1).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y1, z1).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y1, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y2, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()

    vertexConsumer.pos(matrix, x1, y1, z1).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y1, z2).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y1, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y2, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()

    vertexConsumer.pos(matrix, x2, y2, z1).color(color).norm(matrix, -1.0F, 0.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y2, z1).color(color).norm(matrix, -1.0F, 0.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y2, z1).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y2, z2).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()

    vertexConsumer.pos(matrix, x1, y2, z2).color(color).norm(matrix, 0.0F, -1.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y1, z2).color(color).norm(matrix, 0.0F, -1.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y1, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y1, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()

    vertexConsumer.pos(matrix, x2, y1, z2).color(color).norm(matrix, 0.0F, 0.0F, -1.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y1, z1).color(color).norm(matrix, 0.0F, 0.0F, -1.0F).endVertex()
    vertexConsumer.pos(matrix, x1, y2, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y2, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()

    vertexConsumer.pos(matrix, x2, y1, z2).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y2, z2).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y2, z1).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
    vertexConsumer.pos(matrix, x2, y2, z2).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()

    matrix.pop()
    matrix.applyToGlobalState()
  }
}