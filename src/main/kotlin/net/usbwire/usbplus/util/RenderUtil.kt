package net.usbwire.usbplus.util

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.vertex.UVertexConsumer

import net.minecraft.util.math.Box

import java.awt.Color
import net.usbwire.usbplus.USBPlus
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.entity.Entity


object RenderUtil {

  /**
   * Draw a box!
   * @see https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L163
   * @see https://github.com/Splzh/ClearHitboxes/blob/5439f1e3f789e35371939f4bc72ab0fb4eb7d2aa/src/main/java/splash/utils/BoxUtils.java#L74
   * @author Mojang
   */
  fun drawBox (x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: Color, context: WorldRenderContext ) {
    try {
      val matrix = UMatrixStack(context.matrixStack())
      val camera = context.camera().pos
      val wr = UGraphics.getFromTessellator()
      
      UGraphics.enableBlend()
      UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)

      wr.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)

      wr.pos(matrix, x1, y1, z1).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
      wr.pos(matrix, x2, y1, z1).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
      wr.pos(matrix, x1, y1, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
      wr.pos(matrix, x1, y2, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()

      wr.pos(matrix, x1, y1, z1).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
      wr.pos(matrix, x1, y1, z2).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
      wr.pos(matrix, x2, y1, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
      wr.pos(matrix, x2, y2, z1).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()

      wr.pos(matrix, x2, y2, z1).color(color).norm(matrix, -1.0F, 0.0F, 0.0F).endVertex()
      wr.pos(matrix, x1, y2, z1).color(color).norm(matrix, -1.0F, 0.0F, 0.0F).endVertex()
      wr.pos(matrix, x1, y2, z1).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
      wr.pos(matrix, x1, y2, z2).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()

      wr.pos(matrix, x1, y2, z2).color(color).norm(matrix, 0.0F, -1.0F, 0.0F).endVertex()
      wr.pos(matrix, x1, y1, z2).color(color).norm(matrix, 0.0F, -1.0F, 0.0F).endVertex()
      wr.pos(matrix, x1, y1, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
      wr.pos(matrix, x2, y1, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()

      wr.pos(matrix, x2, y1, z2).color(color).norm(matrix, 0.0F, 0.0F, -1.0F).endVertex()
      wr.pos(matrix, x2, y1, z1).color(color).norm(matrix, 0.0F, 0.0F, -1.0F).endVertex()
      wr.pos(matrix, x1, y2, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()
      wr.pos(matrix, x2, y2, z2).color(color).norm(matrix, 1.0F, 0.0F, 0.0F).endVertex()

      wr.pos(matrix, x2, y1, z2).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
      wr.pos(matrix, x2, y2, z2).color(color).norm(matrix, 0.0F, 1.0F, 0.0F).endVertex()
      wr.pos(matrix, x2, y2, z1).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
      wr.pos(matrix, x2, y2, z2).color(color).norm(matrix, 0.0F, 0.0F, 1.0F).endVertex()
      wr.drawDirect()
      UGraphics.disableBlend()
    } catch (e: Exception) {
      USBPlus.logger.info("Error when trying to draw a box!")
      USBPlus.logger.error(e.stackTraceToString())
    }
  }

  fun drawBox (entity: Entity, color: Color, context: WorldRenderContext) {
    val camera = context.camera().pos
    val x = (entity.prevX + ((entity.getX() - entity.prevX) * context.tickDelta())) - camera.x
    val y = (entity.prevY + ((entity.getY() - entity.prevY) * context.tickDelta())) - camera.y
    val z = (entity.prevZ + ((entity.getZ() - entity.prevZ) * context.tickDelta())) - camera.z
    val box = entity.boundingBox.offset(-entity.getX(), -entity.getY(), -entity.getZ()).offset(x, y, z)
    val matrix = UMatrixStack(context.matrixStack())
    UGraphics.enableBlend()
    UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
    UGraphics.depthMask(false)
    UGraphics.disableLighting()
    val vb = UGraphics.getFromTessellator()
    vb.beginWithDefaultShader(UGraphics.DrawMode.fromGl(7), UGraphics.CommonVertexFormats.POSITION_COLOR)
    vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).endVertex()
    vb.drawDirect()
    vb.beginWithDefaultShader(UGraphics.DrawMode.fromGl(7), UGraphics.CommonVertexFormats.POSITION_COLOR)
    vb.pos(matrix, box.maxX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.maxZ).color(color).endVertex()
    vb.drawDirect()
    vb.beginWithDefaultShader(UGraphics.DrawMode.fromGl(7), UGraphics.CommonVertexFormats.POSITION_COLOR)
    vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.minZ).color(color).endVertex()
    vb.drawDirect()
    vb.beginWithDefaultShader(UGraphics.DrawMode.fromGl(7), UGraphics.CommonVertexFormats.POSITION_COLOR)
    vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.minZ).color(color).endVertex()
    vb.drawDirect()
    vb.beginWithDefaultShader(UGraphics.DrawMode.fromGl(7), UGraphics.CommonVertexFormats.POSITION_COLOR)
    vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.minZ).color(color).endVertex()
    vb.drawDirect()
    vb.beginWithDefaultShader(UGraphics.DrawMode.fromGl(7), UGraphics.CommonVertexFormats.POSITION_COLOR)
    vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.minZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.maxY, box.maxZ).color(color).endVertex()
    vb.pos(matrix, box.maxX, box.minY, box.maxZ).color(color).endVertex()
    vb.drawDirect()

    UGraphics.disableBlend()
    UGraphics.depthMask(true)
    UGraphics.enableLighting()

  }
}