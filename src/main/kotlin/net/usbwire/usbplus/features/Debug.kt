package net.usbwire.usbplus.features

import gg.essential.universal.wrappers.message.*
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.passive.VillagerEntity
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.util.Util

/**
 * Debugging for future NPC scraper
 * TODO: actually create NPC scraper
 */
object Debug {
	var clicked = false
	fun onWorldTick() {
		if (Config.debugEnabled == false) return
		val click = USBPlus.mc.mouse.wasMiddleButtonClicked()
		if (click == true && clicked == false) {
			if (USBPlus.mc.targetedEntity != null) {
				printEntityInfo(USBPlus.mc.targetedEntity)
			} else {
				printNearbyPlayers()
			}
		}
		clicked = click
	}

	fun printNearbyPlayers() {
		val entities = USBPlus.mc.world!!.entities
		if (entities == null) return
		for (entity in entities) {
			if (entity == USBPlus.mc.player) continue
			if (entity.distanceTo(USBPlus.mc.player) >= 5.0f) continue
			printEntityInfo(entity)
		}
	}

	fun printEntityInfo(entity: Entity?) {
		if (entity == null) return
		val textCustomName = entity.customName
		Util.chat("Entity Debug Information:")
		val name = UMessage(UTextComponent(entity.name)).unformattedText
		val displayName = UMessage(UTextComponent(entity.displayName)).unformattedText
		val customName = if (textCustomName == null) "null" else UMessage(UTextComponent(textCustomName)).unformattedText
		val entityType = UMessage(UTextComponent(entity.type.untranslatedName)).unformattedText
		Util.chat("Name: ${name}")
		Util.chat("DisplayName: ${displayName}")
		Util.chat("CustomName: ${customName}")
		Util.chat("Type: ${entityType}")
		Util.chat("Pos: (${entity.x}, ${entity.y}, ${entity.z})")
	}
}
