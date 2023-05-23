package net.usbwire.usbplus.util

import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.*
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.*
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.usbwire.usbplus.config.Config
import java.awt.Color

/**
 * How do I draw a box?!?!
 * TODO: fix shaded box rendering when Iris mod is installed
 * ! There is probably a better way of doing things
 */
object RenderUtil {
	fun drawEntityBox(
		entity: Entity,
		color: Color,
		context: WorldRenderContext,
		outline: Boolean = true,
		fill: Boolean = true,
		alphaMultiplier: Float = Config.healthFillPercent,
		expand: Double = 0.0
	) {
		if (!fill && !outline) return
		val camera = context.camera().pos
		val matrix = UMatrixStack(context.matrixStack())

		val x = entity.prevX + (entity.x - entity.prevX) * context.tickDelta() - camera.x
		val y = entity.prevY + (entity.y - entity.prevY) * context.tickDelta() - camera.y
		val z = entity.prevZ + (entity.z - entity.prevZ) * context.tickDelta() - camera.z
		val entityBox = entity.boundingBox.expand(expand, expand, expand)
		val box = Box(
			entityBox.minX - entity.x + x,
			entityBox.minY - entity.y + y,
			entityBox.minZ - entity.z + z,
			entityBox.maxX - entity.x + x,
			entityBox.maxY - entity.y + y,
			entityBox.maxZ - entity.z + z
		)

		if (Config.healthGlowingThroughWalls && entity.isGlowing()) UGraphics.disableDepth()
		else UGraphics.enableDepth()

		if (outline) drawOutlineBox(matrix, context, box, color)
		if (fill && alphaMultiplier > 0f) drawFilledBoundingBox(matrix, box, color, alphaMultiplier)

		UGraphics.enableDepth()
	}

	/**
	 * @author Mojang
	 */
	fun drawOutlineBox(matrix: UMatrixStack, context: WorldRenderContext, box: Box, color: Color) {
		UGraphics.enableBlend()
		UGraphics.disableLighting()
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
		val vertex = context.consumers()!!.getBuffer(RenderLayer.getLines())
		WorldRenderer.drawBox(
			matrix.toMC(),
			vertex,
			box,
			color.red / 255f,
			color.green / 255f,
			color.blue / 255f,
			color.alpha / 255f
		)
		UGraphics.enableLighting()
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
	fun drawFilledBoundingBox(matrixStack: UMatrixStack, box: Box, color: Color, alphaMultiplier: Float = 1f) {
		UGraphics.enableBlend()
		UGraphics.disableLighting()
		UGraphics.depthMask(false)
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
		val wr = UGraphics.getFromTessellator()
		wr.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)
		val adjustedAlpha = (color.alpha * alphaMultiplier).toInt().coerceAtMost(255)

		// vertical
		color.withAlpha(adjustedAlpha).withParts { r, g, b, a ->
			// bottom
			wr.pos(matrixStack, box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
			// top
			wr.pos(matrixStack, box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
		}

		// x axis
		color.withParts { r, g, b, a ->
			Color(
				(r * 0.8f).toInt(),
				(g * 0.8f).toInt(),
				(b * 0.8f).toInt(),
				adjustedAlpha
			)
		}.withParts { r, g, b, a ->
			// west
			wr.pos(matrixStack, box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
			// east
			wr.pos(matrixStack, box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
		}

		// z axis
		color.withParts { r, g, b, a ->
			Color(
				(r * 0.9f).toInt(),
				(g * 0.9f).toInt(),
				(b * 0.9f).toInt(),
				adjustedAlpha
			)
		}.withParts { r, g, b, a ->
			// north
			wr.pos(matrixStack, box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
			// south
			wr.pos(matrixStack, box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
			wr.pos(matrixStack, box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
		}

		wr.drawDirect()
		UGraphics.disableBlend()
		UGraphics.depthMask(true)
		UGraphics.enableLighting()
	}
}