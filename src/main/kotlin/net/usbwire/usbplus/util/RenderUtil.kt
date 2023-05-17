package net.usbwire.usbplus.util

import gg.essential.universal.*
import gg.essential.elementa.utils.withAlpha
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.minecraft.client.render.*
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config
import java.awt.Color


object RenderUtil {

	/**
	 * Draw a box!
	 * @see box.minZttps://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L163
	 * @see box.minZttps://github.com/Splzh/ClearHitboxes/blob/5439f1e3f789e35371939f4bc72ab0fb4eb7d2aa/src/main/java/splash/utils/BoxUtils.java#L74
	 * @author Mojang
	 */
	fun drawEntityBox(entity: Entity, color: Color, context: WorldRenderContext, outline: Boolean = true, fill: Boolean = true) {
		if (!fill && !outline) return
		val camera = context.camera().pos
		val matrix = UMatrixStack(context.matrixStack())

		UGraphics.enableDepth()

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

		val filledColor = Color(
			color.red,
			color.green,
			color.blue,
			(color.alpha * Config.healthFillPercent).toInt()
		)
		if (outline) drawOutlineBox(matrix, context, box, color)
		if (fill) drawFilledBoundingBox(matrix, box, filledColor)

		UGraphics.disableDepth()
	}

	/**
	 * @author Mojang
	 */
	fun drawOutlineBox (matrix: UMatrixStack, context: WorldRenderContext, box: Box, color: Color) {
		UGraphics.enableBlend()
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
		val vertex = context.consumers()!!.getBuffer(RenderLayer.getLines())
		WorldRenderer.drawBox(matrix.toMC(), vertex, box, color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
		UGraphics.disableBlend()
	}

	/**
	 * @author Skytils
	 * https://github.com/Skytils/SkytilsMod/blob/feaee0ba33f4d933d50cd54d3f6113f2eb0a5269/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L160
	 */
	internal fun <T> Color.withParts(block: (Int, Int, Int, Int) -> T) =
        block(this.red, this.green, this.blue, this.alpha)

	/**
	 * @author Skytils
	 * https://github.com/Skytils/SkytilsMod/blob/feaee0ba33f4d933d50cd54d3f6113f2eb0a5269/src/main/kotlin/gg/skytils/skytilsmod/utils/RenderUtil.kt#L163
	 */
	fun drawFilledBoundingBox(matrixStack: UMatrixStack, aabb: Box, c: Color, alphaMultiplier: Float = 1f) {
		UGraphics.enableBlend()
		UGraphics.disableLighting()
		UGraphics.depthMask(false)
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
		val wr = UGraphics.getFromTessellator()
		wr.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)
		val adjustedAlpha = (c.alpha * alphaMultiplier).toInt().coerceAtMost(255)

		// vertical
		c.withAlpha(adjustedAlpha).withParts { r, g, b, a ->
				// bottom
				wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
				// top
				wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
		}

		// x axis
		c.withParts { r, g, b, a ->
				Color(
						(r * 0.8f).toInt(),
						(g * 0.8f).toInt(),
						(b * 0.8f).toInt(),
						adjustedAlpha
				)
		}.withParts { r, g, b, a ->
				// west
				wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
				// east
				wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
		}

		// z axis
		c.withParts { r, g, b, a ->
				Color(
						(r * 0.9f).toInt(),
						(g * 0.9f).toInt(),
						(b * 0.9f).toInt(),
						adjustedAlpha
				)
		}.withParts { r, g, b, a ->
				// north
				wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
				// south
				wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
				wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
		}

		wr.drawDirect()
		UGraphics.disableBlend()
		UGraphics.depthMask(true)
		UGraphics.enableLighting()
	}
}