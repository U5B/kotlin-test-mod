package net.usbwire.usbplus.commands

import gg.essential.api.commands.*
import gg.essential.api.utils.GuiUtil
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.features.Compass
import net.usbwire.usbplus.util.chat.Coordinates

object CompassCommand : Command("compass") {
	@DefaultHandler
	fun handle() {
		Compass.createCompass()
	}

	@SubCommand("compass")
	fun handleCompass () {
		
	}
}
