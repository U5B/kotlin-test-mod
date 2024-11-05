package net.usbwire.usbplus.commands;

import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.features.Compass;
import net.usbwire.usbplus.util.chat.Coordinates;
import net.usbwire.usbplus.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.processing.CommandContainer;

@CommandContainer
public final class CompassCommand {
	private static final Pattern coordinatesRegex =
			Pattern.compile("\\(?(-?\\d{1,4}),? (-?\\d{1,4}),? (-?\\d{1,4})\\)?");

	public CompassCommand() {
		// super("compass");
	}

	@Command("compass")
	public void handle() {
		Compass.createCompass();
	}

	@Command("compass help")
	public void handleHelp() {
		Util.chat("Usage for /compass:\n"
				+ "/compass - Generate a JM/Xaero waypoint for the position your compass is currently pointing at\n"
				+ "/compass pos <x> <y> <z> - Generate a JM/Xaero waypoint for these coordinates\n"
				+ "/compass wiki (x, y, z) - Generate a JM/Xaero waypoint for wiki-formatted coordinates");
	}

	@Command("compass pos <x> <y> <z>")
	public void handlePos(int x, int y, int z) {
		Coordinates.Coordinate coordinates = new Coordinates.Coordinate(x, y, z);
		Compass.createCompass(coordinates);
	}

	@Command("compass wiki <input>")
	public void handleWiki(String input) {
		Matcher matcher = coordinatesRegex.matcher(input);
		if (!matcher.matches()) {
			Util.chat("Invalid String! Should be formatted like 'x, y, z' or '(x, y, z)'");
			return;
		}
		matcher.find();
		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		int z = Integer.parseInt(matcher.group(3));
		Coordinates.Coordinate coordinates = new Coordinates.Coordinate(x, y, z);
		Compass.createCompass(coordinates);
	}
}
