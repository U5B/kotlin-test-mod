package net.usbwire.usbplus.features.health;

import gg.essential.elementa.*;
import gg.essential.elementa.components.*;
import gg.essential.elementa.constraints.*;
import gg.essential.elementa.dsl.*;
import gg.essential.elementa.state.*;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.usbwire.usbplus.config.Config;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.usbwire.usbplus.util.RenderUtil;
import net.usbwire.usbplus.util.MultiVersion.EntityHelper;
import java.awt.Color;

public final class Glow {
	private Glow() {}

	public static void onRenderTick(WorldRenderContext context) {
		if (!Config.healthEnabled && !Config.healthGlowingEnabled)
			return;
		var camera = context.camera();
		for (PlayerEntity player : context.world().getPlayers()) {
			if (player == camera.getFocusedEntity() && !camera.isThirdPerson())
				continue; // don't render hitbox on yourself if in third person
			String name = new UMessage(new UTextComponent(player.getName())).getUnformattedText();
			if (!Base.playerMap.containsKey(name)) {
				EntityHelper.resetGlowingColor(player);
				continue;
			}
			if (Config.healthWhitelistEnabled && Base.playerMap.get(name).glow
					&& !Config.healthWhitelist.toLowerCase().contains(name.toLowerCase())) {
				Base.playerMap.get(name).glow = false;
				EntityHelper.resetGlowingColor(player);
				continue;
			}
			Color color = Base.playerMap.get(name).health.color;
			if (color.getAlpha() > 10) {
				if (Config.healthGlowingEnabled && player.isGlowing()) {
					Base.playerMap.get(name).glow = true;
					EntityHelper.setGlowingColor(player, color);
				} else if (Config.healthEnabled) {
					RenderUtil.drawEntityBox(player, color, context, true, true, 1, 0); // only draw box if player is already not glowing and glowing isn't forced
				}
			}
		}
	}
}
