package net.usbwire.usbplus.commands;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.Greedy;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.commands.parsers.PoiParser.PoiName;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.features.Poi;
import net.usbwire.usbplus.features.Poi.JsonPoi;
import net.usbwire.usbplus.util.Util;
import java.util.List;

public class PoiCommand extends Command {
	public PoiCommand() {
		super("poi", false);
	}

	@DefaultHandler
	public void handle(PoiName name) {
		if (!Config.poiEnabled)
			return;
		if (name == null) {
			Util.chat("No arguments provided! Input a POI to search for...");
			return;
		}
		String poiString = name.getName();
		String string = Util.trimString(poiString);
		if (string.length() <= 3) {
			Util.chat("'" + poiString + "': Too short!");
			return;
		}
		List<JsonPoi> validPoi = Poi.searchPoi(poiString);
		if (validPoi == null) {
			Util.chat("'" + poiString + "': No POI found!");
			return;
		}
		for (JsonPoi poi : validPoi) {
			Poi.responsePoi(poi);
		}
	}
}
