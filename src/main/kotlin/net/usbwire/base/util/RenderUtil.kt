package net.usbwire.base.util

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.vertex.UVertexConsumer

import net.minecraft.util.math.Box

import java.awt.Color
import net.usbwire.base.BaseMod


object RenderUtil {

  /**
   * Draw a box!
   * @see https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L163
   * @see https://github.com/Splzh/ClearHitboxes/blob/5439f1e3f789e35371939f4bc72ab0fb4eb7d2aa/src/main/java/splash/utils/BoxUtils.java#L74
   * @author Mojang
   */
  fun drawBox (x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: Color, ticks: Float) {
    try {
      val matrix = UMatrixStack()
      val camera = getViewerPos(ticks)
      matrix.push()
      UGraphics.enableBlend()
      UGraphics.disableLighting()
      UGraphics.disableAlpha()
      UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
      matrix.translate(-camera.first, -camera.second, -camera.third)
      val wr = UGraphics.getFromTessellator()
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
  
      UGraphics.disableBlend()
      UGraphics.enableAlpha()
      wr.drawDirect()
      matrix.pop()
      matrix.applyToGlobalState()    
    } catch (e: Exception) {
      BaseMod.logger.error(e.stackTraceToString())
    }
  }

  fun getViewerPos(partialTicks: Float): Triple<Double, Double, Double> {
    val viewer = BaseMod.mc.cameraEntity
    val viewerX = viewer!!.lastRenderX + (viewer.getX() - viewer.lastRenderX) * partialTicks
    val viewerY = viewer.lastRenderY + (viewer.getY() - viewer.lastRenderY) * partialTicks
    val viewerZ = viewer.lastRenderZ + (viewer.getZ() - viewer.lastRenderZ) * partialTicks
    return Triple(viewerX, viewerY, viewerZ)
}
}