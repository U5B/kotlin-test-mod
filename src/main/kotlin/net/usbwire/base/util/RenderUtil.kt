package net.usbwire.base.util

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.vertex.UVertexConsumer

import net.minecraft.util.math.Box

import java.awt.Color
import net.usbwire.base.BaseMod
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext


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
      wr.beginWithDefaultShader(UGraphics.DrawMode.LINES, UGraphics.CommonVertexFormats.POSITION_COLOR)

      UGraphics.enableBlend()
      UGraphics.disableLighting()
      UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)

      matrix.push()
      matrix.translate(-camera.x, -camera.y, -camera.z)

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
      matrix.pop()
      // matrix.applyToGlobalState()
      UGraphics.disableBlend()
      UGraphics.enableLighting()
    } catch (e: Exception) {
      BaseMod.logger.info("Error when trying to draw a box!")
      BaseMod.logger.error(e.stackTraceToString())
    }
  }
}