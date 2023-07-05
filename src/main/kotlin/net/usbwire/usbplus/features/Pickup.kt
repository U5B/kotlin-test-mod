package net.usbwire.usbplus.features

import gg.essential.universal.wrappers.message.*
import gg.essential.universal.wrappers.UPlayer
import gg.essential.universal.UChat
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.passive.VillagerEntity
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.util.Util

/**
 * Pickup Filter toggle when sneaking like RLCraft
 */
object Pickup {
	var ticksSneaking = 0
	var isSneaking = false;
	fun onWorldTick() {
		val player = USBPlus.mc.player;
		if (player == null || !Config.pickupEnabled) return
		if (player.isSpectator || player.isCreative) return
		if (player.isSneaking) {
			ticksSneaking++
			if (isSneaking == false && ticksSneaking > Config.pickupDelay) {
				UChat.say("/pu")
				isSneaking = true
			}
		} else if (isSneaking == true) {
			if (ticksSneaking > Config.pickupDelay) {
				UChat.say("/pu")
			}
			isSneaking = false
			ticksSneaking = 0
		}
	}
}