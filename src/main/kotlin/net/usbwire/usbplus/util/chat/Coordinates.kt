package net.usbwire.usbplus.util.chat

import gg.essential.universal.wrappers.message.*
import kotlinx.serialization.Serializable
import net.minecraft.text.*
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.util.Util

object Coordinates {
	var supportsXaero = true
	var supportsJourneymap = true
	fun coordinateBuilder(
		name: String,
		x: Int,
		y: Int,
		z: Int,
		dimension: String,
		message: UMessage = UMessage()
	): UMessage {

		// prefix
		val baseCompoment = UTextComponent("'${name}':")
		message.addTextComponent(baseCompoment)

		// copy
		val copyCompoment = UTextComponent(" §a(${x}, ${y}, ${z})§r")
		copyCompoment.clickAction = ClickEvent.Action.COPY_TO_CLIPBOARD
		copyCompoment.clickValue = "${x} ${y} ${z}"
		copyCompoment.hoverAction = HoverEvent.Action.SHOW_TEXT
		copyCompoment.hoverValue = UTextComponent("§aClick to copy coordinates to clipboard!§r")
		message.addTextComponent(copyCompoment)

		// xaero minimap support
		if (supportsXaero == true) {
			val xaeroCompoment = xaeroBuilder(name, x, y, z, dimension)
			if (xaeroCompoment == null) {
				supportsXaero = false
			} else {
				message.addTextComponent(xaeroCompoment)
			}
		}

		// journeymap support
		if (supportsJourneymap == true) {
			val journeymapCompoment = journeymapBuilder(name, x, y, z, dimension)
			if (journeymapCompoment == null) {
				supportsJourneymap = false
			} else {
				message.addTextComponent(journeymapCompoment)
			}
		}
		return message
	}

	private fun xaeroBuilder(
		name: String,
		x: Int,
		y: Int,
		z: Int,
		dimension: String
	): UTextComponent? {
		try { // https://github.com/U5B/jsmacros/blob/eb9e5aafa2ac56fef6cd74c432b3b8ac07840d25/scripts/lib/xaero.ts#L69
			Class.forName("xaero.common.XaeroMinimapSession")
			// technically dimension but who cares
			// TODO: map poi.region to dimension and set waypoint in correct dimension
			val currentDimension = dimension.replace(":", "$")
			val xaeroColor = Util.Color.xaero["dark_red"]
			val minecraftColor = Util.Color.minecraft["dark_red"]
			val xaeroCompoment = UTextComponent(" ${minecraftColor}[XAERO]§r")
			val waypoint =
				"xaero_waypoint_add:${name}:${name[0].uppercase()}:${x}:${y}:${z}:${xaeroColor}:false:0:Internal_dim%${currentDimension}_waypoints"
			USBPlus.logger.info("Xaero: " + waypoint)
			// val shareableWaypoint =
			// "xaero-waypoint:${poi.name}:${poi.name[0].uppercase()}:${poi.coordinates.x}:${poi.coordinates.y}:${poi.coordinates.z}:${xaeroColor}:false:0:Internal-dim%${currentWorld}-waypoints"
			xaeroCompoment.clickAction = ClickEvent.Action.RUN_COMMAND
			xaeroCompoment.clickValue = waypoint

			xaeroCompoment.hoverAction = HoverEvent.Action.SHOW_TEXT
			xaeroCompoment.hoverValue = UTextComponent("${minecraftColor}Click to create a new xaero waypoint!§r")
			return xaeroCompoment
		} catch (e: Exception) {
			return null
		}
	}

	private fun journeymapBuilder(
		name: String,
		x: Int,
		y: Int,
		z: Int,
		dimension: String
	): UTextComponent? {
		try {
			Class.forName("journeymap.client.JourneymapClient")
			val minecraftColor = Util.Color.minecraft["aqua"]
			val journeymapCompoment = UTextComponent(" ${minecraftColor}[JM]§r")
			val waypoint = "/jm wpedit [name:\"${name}\", x:${x}, y:${y}, z:${z}, dim:${dimension}]"
			journeymapCompoment.clickAction = ClickEvent.Action.RUN_COMMAND
			journeymapCompoment.clickValue = waypoint

			journeymapCompoment.hoverAction = HoverEvent.Action.SHOW_TEXT
			journeymapCompoment.hoverValue = UTextComponent("${minecraftColor}Click to create a new journey map waypoint!§r")
			return journeymapCompoment
		} catch (e: Exception) {
			return null
		}
	}

	@Serializable
	data class Coordinates(val x: Int, val y: Int, val z: Int)
}
