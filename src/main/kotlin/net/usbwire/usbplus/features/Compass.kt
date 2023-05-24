package net.usbwire.usbplus.features

import ItemHelper
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.util.Util
import net.usbwire.usbplus.util.chat.Coordinates
import net.usbwire.usbplus.commands.CompassCommand
import gg.essential.api.EssentialAPI

/**
 * Monumenta compass helper! Gets where compass is pointing on left click
 * Ported directly from U5B/jsmacros
 * TODO: add command /compass to get position easily
 * TODO: make a configurable keybind
 */
object Compass {
	fun getCompass(): Coordinates.Coordinates {
		val spawnPos = USBPlus.mc.world!!.spawnPos
		return Coordinates.Coordinates(spawnPos.x, spawnPos.y, spawnPos.z)
	}

	fun createCompass(compass: Coordinates.Coordinates = getCompass()) {
		val dimension = USBPlus.mc.world!!.registryKey.value.toString()
		val name = "Compass"
		val x = compass.x
		val y = compass.y
		val z = compass.z
		val message = Coordinates.coordinateBuilder(name, x, y, z, dimension)
		Util.chat(message)
	}

	fun configChanged(value: Boolean = Config.poiEnabled) {
		if (value == true) {
			CompassCommand.register()
		} else if (value == false) {
			EssentialAPI.getCommandRegistry().unregisterCommand(CompassCommand)
		}
	}

	var clicked = false
	fun onWorldTick() {
		if (Config.compassEnabled == false) return
		val click = USBPlus.mc.mouse.wasLeftButtonClicked()
		if (click == true && clicked == false) {
			val mainItem = ItemHelper.getItemId(USBPlus.mc.player?.mainHandStack?.item)
			if (mainItem == "minecraft:compass") {
				createCompass()
			}
		}
		clicked = click
	}
}