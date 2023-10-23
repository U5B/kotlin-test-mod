package net.usbwire.usbplus.util.chat

import gg.essential.universal.wrappers.message.*
import net.minecraft.text.*

object ChatUtil {
	fun clipboardBuilder(
		name: String,
		clipboard: String,
		message: UMessage = UMessage()
	): UMessage {
		// copy
		val copyComponent = UTextComponent("${name}")
		copyComponent.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, clipboard);
		message.addTextComponent(copyComponent)

		return message
	}
}