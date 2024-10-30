package net.usbwire.usbplus.util.chat;

import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.text.ClickEvent;

public class Clipboard {
	public static UMessage clipboardBuilder(String name, String clipboard) {
		return clipboardBuilder(name, clipboard, new UMessage());
	}

	public static UMessage clipboardBuilder(String name, String clipboard, UMessage message) {
		// copy
		UTextComponent copyComponent = new UTextComponent(name);
		copyComponent.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, clipboard);
		message.addTextComponent(copyComponent);

		return message;
	}
}
