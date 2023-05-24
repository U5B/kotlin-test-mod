package net.usbwire.usbplus.commands

import gg.essential.api.commands.*
import gg.essential.api.utils.GuiUtil
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.features.Compass
import net.usbwire.usbplus.util.chat.Coordinates
import net.usbwire.usbplus.util.Util

object CompassCommand : Command("compass") {
	@DefaultHandler
	fun handle () {
		Compass.createCompass()
	}

	@SubCommand("help")
	fun handleHelp () {
		Util.chat("""Usage for /compass:
		|/compass - Generate a JM/Xaero waypoint for the position your compass is currently pointing at
		|/compass pos <x> <y> <z> - Generate a JM/Xaero waypoint for these coordinates
		|/compass wiki (x, y, z) - Generate a JM/Xaero waypoint for wiki-formatted coordinates
		""".trimMargin("|"))
	}

	@SubCommand("pos")
	fun handlePos (x: Int, y: Int, z: Int) {
		val coordinates = Coordinates.Coordinates(x, y, z)
		Compass.createCompass(coordinates)
	}

	val coordinatesRegex = """\(?(-?[\d]{1,4}),? (-?[\d]{1,4}),? (-?[\d]{1,4})\)?""".toRegex()
	@SubCommand("wiki")
	fun handleWiki (@Greedy input: String) {
		if (!coordinatesRegex.matches(input)) {
			Util.chat("Invalid String! Should be formatted like 'x, y, z' or '(x, y, z)'")
			return
		}
		val result = coordinatesRegex.find(input)
		val (x, y, z) = result!!.destructured

		val coordinates = Coordinates.Coordinates(x.toInt(), y.toInt(), z.toInt())
		Compass.createCompass(coordinates)
	}
}
