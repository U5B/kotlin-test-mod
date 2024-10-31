package net.usbwire.usbplus.commands;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.SubCommand;
import gg.essential.api.commands.Greedy;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.features.Compass;
import net.usbwire.usbplus.util.chat.Coordinates;
import net.usbwire.usbplus.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompassCommand extends Command {
	private static final Pattern coordinatesRegex =
			Pattern.compile("\\(?(-?\\d{1,4}),? (-?\\d{1,4}),? (-?\\d{1,4})\\)?");

	public CompassCommand() {
		super("compass");
	}

	@DefaultHandler
	public void handle() {
		Compass.createCompass();
	}

	@SubCommand("help")
	public void handleHelp() {
		Util.chat("Usage for /compass:\n"
				+ "/compass - Generate a JM/Xaero waypoint for the position your compass is currently pointing at\n"
				+ "/compass pos <x> <y> <z> - Generate a JM/Xaero waypoint for these coordinates\n"
				+ "/compass wiki (x, y, z) - Generate a JM/Xaero waypoint for wiki-formatted coordinates");
	}

	@SubCommand("pos")
	public void handlePos(int x, int y, int z) {
		Coordinates.Coordinate coordinates = new Coordinates.Coordinate(x, y, z);
		Compass.createCompass(coordinates);
	}

	@SubCommand("wiki")
	public void handleWiki(@Greedy String input) {
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
