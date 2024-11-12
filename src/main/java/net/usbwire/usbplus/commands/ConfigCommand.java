package net.usbwire.usbplus.commands;

import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.processing.CommandContainer;
import gg.essential.vigilance.gui.SettingsGui;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.util.Util;

@CommandContainer
public class ConfigCommand {
	public ConfigCommand() {
	}

	@Command("usbplus")
	public void handle() {
		Config.openConfig();
	}
}
