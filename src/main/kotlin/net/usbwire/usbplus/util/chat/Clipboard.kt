package net.usbwire.usbplus.util.chat

import gg.essential.universal.wrappers.message.*
import kotlinx.serialization.Serializable
import net.minecraft.text.*
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.util.Util

object ChatUtil {
	fun clipboardBuilder(
		name: String,
		clipboard: String,
		message: UMessage = UMessage()
	): UMessage {
		// copy
		val copyCompoment = UTextComponent("${name}")
		copyCompoment.clickAction = ClickEvent.Action.COPY_TO_CLIPBOARD
		copyCompoment.clickValue = clipboard
		message.addTextComponent(copyCompoment)

		return message
	}
}