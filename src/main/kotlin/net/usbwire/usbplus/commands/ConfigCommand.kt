package net.usbwire.usbplus.commands

import gg.essential.api.commands.*
import gg.essential.api.utils.GuiUtil
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config

object ConfigCommand : Command(USBPlus.name.lowercase(), false) {
	@DefaultHandler
	fun handle() {
		val gui = Config.gui() ?: return
		GuiUtil.open(gui)
	}
}
