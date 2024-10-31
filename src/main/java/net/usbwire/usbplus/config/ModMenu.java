package net.usbwire.usbplus.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.usbwire.usbplus.USBPlus;

public class ModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		return parent -> USBPlus.config.gui();
	}
}
