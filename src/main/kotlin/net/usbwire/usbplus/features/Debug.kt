package net.usbwire.usbplus.features

import gg.essential.universal.wrappers.message.*
import net.minecraft.entity.Entity
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
			}
		}
		clicked = click
	}

	fun printEntityInfo(entity: Entity?) {
		if (entity == null) return
		val textCustomName = entity.customName
		Util.chat("Entity Debug Information:")
		val name = UMessage(UTextComponent(entity.name)).formattedText
		val displayName = UMessage(UTextComponent(entity.displayName)).formattedText
		val customName = if (textCustomName == null) "null" else UMessage(UTextComponent(textCustomName)).formattedText
		Util.chat("Name: ${name}")
		Util.chat("DisplayName: ${displayName}")
		Util.chat("CustomName: ${customName}")
		Util.chat("Pos: (${entity.x}, ${entity.y}, ${entity.z})")
		if (entity is VillagerEntity) printVillagerInfo(entity)
	}

	fun printVillagerInfo(entity: VillagerEntity) {
		Util.chat("Offers: ${entity.offers.size}")
	}
}
