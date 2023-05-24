package net.usbwire.usbplus.features.health

import gg.essential.elementa.*
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.*
import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.player.PlayerEntity
import net.usbwire.usbplus.config.Config
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.usbwire.usbplus.util.RenderUtil
import java.awt.Color

object Base {
	data class HealthData(val current: Float, val max: Float, val absorption: Float, val percent: Float, val color: Color)
	data class PlayerHP (
		val name: String,
		var health: HealthData,
		var tick: Int = -1,
		var draw: PlayerHPDraw? = null,
		var glow: Boolean = false
	)

	data class PlayerHPDraw(
		val root: UIComponent,
		val states: PlayerHPStates,
		var damageTick: Int = -1, 
	)

	data class PlayerHPStates(
		val name: State<String>,
		val health: State<String>,
		val absorption: State<String>,
		val damage: State<String>,
		val healthColor: State<Color>,
		val damageColor: State<Color>
	)

	data class PlayerHPTicks(
		var main: Int = -1,
		var damage: Int = -1
	)

	val playerMap: MutableMap<String, PlayerHP> = mutableMapOf()
	val currentPlayers: MutableList<String> = mutableListOf()

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

	fun updatePlayers (world: ClientWorld) {
		val worldPlayers = world.players
		if (worldPlayers.isNullOrEmpty()) return

		val previousPlayerMap = playerMap.toMap()
		currentPlayers.clear()

		for (player in worldPlayers) {
			val name = UMessage(UTextComponent(player.name)).unformattedText

			if (Config.healthWhitelistEnabled && !Config.healthWhitelist.lowercase().contains(name.lowercase())) continue

			val hp = getHealthProperties(player)

			// create new player if it exists
			if (playerMap[name] == null) playerMap[name] = PlayerHP(name, hp)

			// hud updates
			if (Config.healthDrawEnabled) {
				if (playerMap[name]!!.draw == null) playerMap[name]!!.draw = HUD.createPlayer(name)
				HUD.updatePlayer(playerMap[name]!!, hp)
			}

			// set custom glow color
			if (Config.healthGlowingEnabled && player.isGlowing) {
				playerMap[name]!!.glow = true
				EntityHelper.setGlowingColor(player, hp.color)
			} else if (playerMap[name]!!.glow) {
				playerMap[name]!!.glow = false
				EntityHelper.resetGlowingColor(player)
			}

			playerMap[name]!!.tick++
			playerMap[name]!!.health = hp
			currentPlayers.add(name)
		}

		for (player in previousPlayerMap) {
			if (currentPlayers.contains(player.key)) continue
			playerMap.remove(player.key)
			val draw = player.value.draw
			if (draw != null) HUD.remove(draw)
		}

		// hud post-processing
		if (Config.healthDrawEnabled) {
			for (player in playerMap) {
				// post processing
				val draw = player.value.draw
				if (draw != null) HUD.add(draw)
			}
			HUD.sort(playerMap)
		}
	}


	var configDirty = true
	fun onWorldTick(world: ClientWorld) {
		if (Config.healthDrawEnabled == false) {
			configDirty = true
			return
		}
		if (configDirty == true) {
			playerMap.clear()
			if (Config.healthDrawEnabled) HUD.clear()
			configDirty = false
		}
		if ((world.time % Config.healthUpdateTicks).toInt() == 0) {
			updatePlayers(world)
		}
	}
}