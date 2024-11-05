package net.usbwire.usbplus.features;

import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.util.MultiVersion;
import net.usbwire.usbplus.util.Util;
import net.usbwire.usbplus.util.chat.Coordinates;
import net.usbwire.usbplus.commands.CompassCommand;

public class Compass {
	private static boolean clicked = false;
	/**
	 * Monumenta compass helper! Gets where compass is pointing on left click
	 * Ported directly from U5B/jsmacros
	 * TODO: add command /compass to get position easily
	 * TODO: make a configurable keybind
	 */
	// private static final Command compassCommand = new CompassCommand();

	public static Coordinates.Coordinate getCompass() {
		var spawnPos = USBPlus.mc.world.getSpawnPos();
		return new Coordinates.Coordinate(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
	}

	public static void createCompass() {
		createCompass(getCompass());
	}

	public static void createCompass(Coordinates.Coordinate compass) {
		var dimension = USBPlus.mc.world.getRegistryKey().getValue().toString();
		var name = "Compass";
		var x = compass.getX();
		var y = compass.getY();
		var z = compass.getZ();
		var message = Coordinates.coordinateBuilder(name, x, y, z, dimension);
		Util.chat(message);
	}

	public static void configChanged() {
		// meh
	}

	public static void onWorldTick() {
		if (!Config.compassEnabled)
			return;
		var click = USBPlus.mc.mouse.wasLeftButtonClicked();
		if (click && !clicked) {
			var mainItem =
					MultiVersion.ItemHelper.getItemId(USBPlus.mc.player.getMainHandStack().getItem());
			if ("minecraft:compass".equals(mainItem)) {
				createCompass();
			}
		}
		clicked = click;
	}
}
