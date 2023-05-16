package net.usbwire.usbplus.util

import gg.essential.universal.*
import gg.essential.elementa.utils.withAlpha
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.usbwire.usbplus.USBPlus
import java.awt.Color


object RenderUtil {

	/**
	 * Draw a box!
	 * @see box.minZttps://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L163
	 * @see box.minZttps://github.com/Splzh/ClearHitboxes/blob/5439f1e3f789e35371939f4bc72ab0fb4eb7d2aa/src/main/java/splash/utils/BoxUtils.java#L74
	 * @author Mojang
	 */
	fun drawEntityBox(entity: Entity, color: Color, context: WorldRenderContext) {
		val camera = context.camera().pos
		val matrix = UMatrixStack(context.matrixStack())

		UGraphics.enableBlend()
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
		// UGraphics.depthMask(false)
		UGraphics.disableLighting()
		UGraphics.disableDepth()

		val x = entity.prevX + (entity.getX() - entity.prevX) * context.tickDelta() - camera.x
		val y = entity.prevY + (entity.getY() - entity.prevY) * context.tickDelta() - camera.y
		val z = entity.prevZ + (entity.getZ() - entity.prevZ) * context.tickDelta() - camera.z
		val entityBox = entity.boundingBox
		val box = Box(
			entityBox.minX - entity.getX() + x,
			entityBox.minY - entity.getY() + y,
			entityBox.minZ - entity.getZ() + z,
			entityBox.maxX - entity.getX() + x,
			entityBox.maxY - entity.getY() + y,
			entityBox.maxZ - entity.getZ() + z
		)

		drawFilledBox(matrix, box, color)
		drawOutlineBox(matrix, box, color)

		UGraphics.disableBlend()
		// UGraphics.depthMask(true)
		UGraphics.enableLighting()
		UGraphics.enableDepth()
	}

	fun drawOutlineBox (matrix: UMatrixStack, box: Box, color: Color) {
		val vb = UGraphics.getFromTessellator()
		vb.beginWithDefaultShader(UGraphics.DrawMode.LINES, UGraphics.CommonVertexFormats.POSITION)
		vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).norm(matrix, 1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.minY, box.minZ).color(color).norm(matrix, 1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).norm(matrix, 0.0f, 1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).norm(matrix, 0.0f, 1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.minY, box.minZ).color(color).norm(matrix, 0.0f, 0.0f, 1.0f).endVertex()
		vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).norm(matrix, 0.0f, 0.0f, 1.0f).endVertex()
		vb.pos(matrix, box.maxY, box.minY, box.minZ).color(color).norm(matrix, 0.0f, 1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.maxY, box.minZ).color(color).norm(matrix, 0.0f, 1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.maxY, box.minZ).color(color).norm(matrix, -1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).norm(matrix, -1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.maxY, box.minZ).color(color).norm(matrix, 0.0f, 0.0f, 1.0f).endVertex()
		vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).norm(matrix, 0.0f, 0.0f, 1.0f).endVertex()
		vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).norm(matrix, 0.0f, -1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).norm(matrix, 0.0f, -1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.minX, box.minY, box.maxZ).color(color).norm(matrix, 1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.minY, box.maxZ).color(color).norm(matrix, 1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.minY, box.maxZ).color(color).norm(matrix, 0.0f, 0.0f, -1.0f).endVertex()
		vb.pos(matrix, box.maxY, box.minY, box.minZ).color(color).norm(matrix, 0.0f, 0.0f, -1.0f).endVertex()
		vb.pos(matrix, box.minX, box.maxY, box.maxZ).color(color).norm(matrix, 1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.maxY, box.maxZ).color(color).norm(matrix, 1.0f, 0.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.minY, box.maxZ).color(color).norm(matrix, 0.0f, 1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.maxY, box.maxZ).color(color).norm(matrix, 0.0f, 1.0f, 0.0f).endVertex()
		vb.pos(matrix, box.maxY, box.maxY, box.minZ).color(color).norm(matrix, 0.0f, 0.0f, 1.0f).endVertex()
		vb.pos(matrix, box.maxY, box.maxY, box.maxZ).color(color).norm(matrix, 0.0f, 0.0f, 1.0f).endVertex()
		vb.drawDirect()
	}

	fun drawFilledBox (matrix: UMatrixStack, box: Box, color: Color) {
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
	}
}