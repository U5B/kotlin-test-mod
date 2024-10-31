package net.usbwire.usbplus.commands;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;
import gg.essential.vigilance.gui.SettingsGui;
import net.usbwire.usbplus.USBPlus;

public class ConfigCommand extends Command {
	public ConfigCommand() {
		super(USBPlus.name.toLowerCase(), false);
	}

	@DefaultHandler
	public void handle() {
		SettingsGui gui = USBPlus.config.gui();
		if (gui == null)
			return;
		GuiUtil.open(gui);
	}
}
