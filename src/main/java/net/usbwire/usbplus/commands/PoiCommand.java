package net.usbwire.usbplus.commands;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.features.Poi;
import net.usbwire.usbplus.features.Poi.JsonPoi;
import net.usbwire.usbplus.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.processing.CommandContainer;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;

@CommandContainer
public final class PoiCommand {
	public PoiCommand() {
		// super("poi", false);
	}

	@Command("poi <name>")
	public void handle(@Argument(suggestions = "poi-suggestions", value = "name") @Greedy String name) {
		if (!Config.poiEnabled)
			return;
		if (name == null) {
			Util.chat("No arguments provided! Input a POI to search for...");
			return;
		}
		String string = Util.trimString(name);
		if (string.length() <= 2) {
			Util.chat("'" + name + "': Too short!");
			return;
		}
		List<JsonPoi> validPoi = Poi.searchPoi(name);
		if (validPoi == null) {
			Util.chat("'" + name + "': No POI found!");
			return;
		}
		for (JsonPoi poi : validPoi) {
			Poi.responsePoi(poi);
		}
	}

	@Suggestions("poi-suggestions")
	public Stream<String> containerSuggestions(
					final CommandContext<FabricClientCommandSource> context,
					final String input
	) {
		List<String> pois = new ArrayList<>(Poi.getCommandSuggestions());
		List<String> suggestions = new ArrayList<>();
		for (String poi : pois) {
			String[] words = poi.split(" ");
			for (String word : words) {
				if (word.toLowerCase().startsWith(input.toLowerCase())) {
					suggestions.add(poi);
					break;
				}
			}
		}

		return suggestions.stream();
	}
}
