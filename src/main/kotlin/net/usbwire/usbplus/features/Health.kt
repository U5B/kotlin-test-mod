package net.usbwire.usbplus.features

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.entity.player.PlayerEntity
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.util.RenderUtil
import java.awt.Color

/**
 * Main selling point of this mod. Displays a colored box dependent on the player's health
 * TODO: don't recalculate health every frame
 */
object Health {
	data class HealthData(val current: Float, val max: Float, val absorption: Float, val percent: Float, val color: Color)

	fun onRenderTick(context: WorldRenderContext) {
		if (Config.healthEnabled == false) return
		val camera = context.camera()
		for (player in context.world().players) {
			if (player == camera.focusedEntity && !camera.isThirdPerson) continue
			val color = getHealthProperties(player).color
			if (color.alpha <= 10) continue
			if (Config.healthFillPercent == 0f) {
				RenderUtil.drawEntityBox(player, color, context, true, false)
			} else {
				RenderUtil.drawEntityBox(player, color, context, true, true)
			}
		}
	}

	fun getHealthProperties(entity: PlayerEntity): HealthData {
		val current = entity.health
		val max = entity.maxHealth
		val absorption = entity.absorptionAmount // not factored into health equation?
		val percent = current / max
		var color: Color
		if (Config.healthHurtEnabled == true && entity.hurtTime != 0) { // hurt damage visible
			color = hasBadEffect(entity) ?: Config.healthHurtColor
		} else {
			color = getHealthColor(percent)
		}
		return HealthData(current, max, absorption, percent, color)
	}

	fun getHealthColor(percent: Float): Color {
		if (percent >= Config.healthGoodPercent) {
			return Config.healthBaseColor
		} else if (percent >= Config.healthLowPercent && percent <= Config.healthGoodPercent) {
			return Config.healthGoodColor
		} else if (percent >= Config.healthCriticalPercent && percent <= Config.healthLowPercent) {
			return Config.healthLowColor
		} else if (percent <= Config.healthCriticalPercent) { // assuming its red
			return Config.healthCriticalColor
		}
		return Config.healthBaseColor // fallback if something went wrong!
	}

	/**
	 * Unfortunately you cannot determine if another player has a specific effect on multiplayer servers
	 * You could get particle colors but that takes way too much work
	 */
	fun hasBadEffect(entity: PlayerEntity): Color? {
		if (Config.healthEffectEnabled == false) return null
		// Player on fire?
		if (entity.isOnFire == true && entity.isFireImmune == false) return Config.healthEffectColor
		return null
	}
}
