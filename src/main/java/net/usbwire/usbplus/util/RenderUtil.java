package net.usbwire.usbplus.util;

import gg.essential.universal.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.usbwire.usbplus.config.Config;

import java.awt.Color;

public class RenderUtil {
	public static void drawEntityBox(Entity entity, Color color, WorldRenderContext context,
			boolean outline, boolean fill, float alphaMultiplier, double expand) {
		if (!fill && !outline)
			return;
		var camera = context.camera().getPos();
		var matrix = new UMatrixStack(context.matrixStack());

		double x = entity.prevX + (entity.getX() - entity.prevX) * context.tickDelta() - camera.getX();
		double y = entity.prevY + (entity.getY() - entity.prevY) * context.tickDelta() - camera.getY();
		double z = entity.prevZ + (entity.getZ() - entity.prevZ) * context.tickDelta() - camera.getZ();
		var entityBox = entity.getBoundingBox().expand(expand, expand, expand);
		var box = new Box(entityBox.minX - entity.getX() + x, entityBox.minY - entity.getY() + y,
				entityBox.minZ - entity.getZ() + z, entityBox.maxX - entity.getX() + x,
				entityBox.maxY - entity.getY() + y, entityBox.maxZ - entity.getZ() + z);

		if (Config.healthGlowingEnabled && entity.isGlowing())
			UGraphics.disableDepth();
		else
			UGraphics.enableDepth();

		if (outline)
			drawOutlineBox(matrix, context, box, color);
		if (fill && alphaMultiplier > 0f)
			drawFilledBoundingBox(matrix, box, color, alphaMultiplier);

		UGraphics.enableDepth();
	}

	public static void drawOutlineBox(UMatrixStack matrix, WorldRenderContext context, Box box,
			Color color) {
		UGraphics.enableBlend();
		UGraphics.disableLighting();
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0);
		var vertex = context.consumers().getBuffer(RenderLayer.getLines());
		WorldRenderer.drawBox(matrix.toMC(), vertex, box, color.getRed() / 255f,
				color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		UGraphics.enableLighting();
		UGraphics.disableBlend();
	}

	public static <T> T withParts(Color color,
			QuadFunction<Integer, Integer, Integer, Integer, T> block) {
		return block.apply(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static void drawFilledBoundingBox(UMatrixStack matrixStack, Box box, Color color,
			float alphaMultiplier) {
		UGraphics.enableBlend();
		UGraphics.disableLighting();
		UGraphics.depthMask(false);
		UGraphics.tryBlendFuncSeparate(770, 771, 1, 0);
		var wr = UGraphics.getFromTessellator();
		wr.beginWithDefaultShader(UGraphics.DrawMode.QUADS,
				UGraphics.CommonVertexFormats.POSITION_COLOR);
		int adjustedAlpha = Math.min((int) (color.getAlpha() * alphaMultiplier), 255);

		// vertical
		withParts(new Color(color.getRed(), color.getGreen(), color.getBlue(), adjustedAlpha),
				(r, g, b, a) -> {
					// bottom
					wr.pos(matrixStack, box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
					// top
					wr.pos(matrixStack, box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
					return null;
				});

		// x axis
		withParts(new Color((int) (color.getRed() * 0.8f), (int) (color.getGreen() * 0.8f),
				(int) (color.getBlue() * 0.8f), adjustedAlpha), (r, g, b, a) -> {
					// west
					wr.pos(matrixStack, box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
					// east
					wr.pos(matrixStack, box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
					return null;
				});

		// z axis
		withParts(new Color((int) (color.getRed() * 0.9f), (int) (color.getGreen() * 0.9f),
				(int) (color.getBlue() * 0.9f), adjustedAlpha), (r, g, b, a) -> {
					// north
					wr.pos(matrixStack, box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
					// south
					wr.pos(matrixStack, box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
					wr.pos(matrixStack, box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
					return null;
				});

		wr.drawDirect();
		UGraphics.disableBlend();
		UGraphics.depthMask(true);
		UGraphics.enableLighting();
	}

	@FunctionalInterface
	public interface QuadFunction<A, B, C, D, R> {
		R apply(A a, B b, C c, D d);
	}
}
