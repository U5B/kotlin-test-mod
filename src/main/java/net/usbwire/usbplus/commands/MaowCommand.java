package net.usbwire.usbplus.commands;

import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.processing.CommandContainer;
import gg.essential.vigilance.gui.SettingsGui;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.features.Maow;
import net.usbwire.usbplus.util.Util;

@CommandContainer
public class MaowCommand {
	public MaowCommand() {
	}

	@Command("maow")
	public void handle() {
		Util.chat("maow");
		Maow.init();
	}
}
