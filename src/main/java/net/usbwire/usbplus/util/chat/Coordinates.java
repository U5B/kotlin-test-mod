package net.usbwire.usbplus.util.chat;

import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.usbwire.usbplus.util.Util;

/**
 * Creates a message with coordinates and buttons to
 * copy coordinates or add a JM/Xaero waypoint if available.
 * TODO: split each compoment into multiple functions
 * TODO: directly create the waypoint if possible
 */
public final class Coordinates {
	public static boolean supportsXaero = true;
	public static boolean supportsJourneymap = true;

	private Coordinates() {}

	public static UMessage coordinateBuilder(String name, int x, int y, int z, String dimension) {
		return coordinateBuilder(name, x, y, z, dimension, new UMessage());
	}

	public static UMessage coordinateBuilder(String name, int x, int y, int z, String dimension,
			UMessage message) {
		// prefix
		UTextComponent baseComponent = new UTextComponent("'" + name + "':");
		message.addTextComponent(baseComponent);

		// copy
		UTextComponent copyComponent = new UTextComponent(" §a(" + x + ", " + y + ", " + z + ")§r");
		copyComponent.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, x + " " + y + " " + z);
		copyComponent.setHover(HoverEvent.Action.SHOW_TEXT,
				new UTextComponent("§aClick to copy coordinates to clipboard!§r"));
		message.addTextComponent(copyComponent);

		// xaero minimap support
		if (supportsXaero) {
			UTextComponent xaeroComponent = xaeroBuilder(name, x, y, z, dimension);
			if (xaeroComponent == null) {
				supportsXaero = false;
			} else {
				message.addTextComponent(xaeroComponent);
			}
		}

		// journeymap support
		if (supportsJourneymap) {
			UTextComponent journeymapComponent = journeymapBuilder(name, x, y, z, dimension);
			if (journeymapComponent == null) {
				supportsJourneymap = false;
			} else {
				message.addTextComponent(journeymapComponent);
			}
		}
		return message;
	}

	private static UTextComponent xaeroBuilder(String name, int x, int y, int z, String dimension) {
		try {
			Class.forName("xaero.common.XaeroMinimapSession");
			String currentDimension = dimension.replace(":", "$");
			String xaeroColor = Util.Color.xaero.get("dark_red");
			String minecraftColor = Util.Color.minecraft.get("dark_red");
			UTextComponent xaeroComponent = new UTextComponent(" " + minecraftColor + "[XAERO]§r");
			String waypoint = "/xaero_waypoint_add:" + name + ":" + name.charAt(0) + ":" + x + ":" + y
					+ ":" + z + ":" + xaeroColor + ":false:0:Internal_dim%" + currentDimension + "_waypoints";
			// String waypoint =
			// "/xaero-waypoint:${poi.name}:${poi.name[0].uppercase()}:${poi.coordinates.x}:${poi.coordinates.y}:${poi.coordinates.z}:${xaeroColor}:false:0:Internal-dim%${currentWorld}-waypoints"
			xaeroComponent.setClick(ClickEvent.Action.RUN_COMMAND, waypoint);
			xaeroComponent.setHover(HoverEvent.Action.SHOW_TEXT,
					new UTextComponent(minecraftColor + "Click to create a new xaero waypoint!§r"));
			return xaeroComponent;
		} catch (Exception e) {
			return null;
		}
	}

	private static UTextComponent journeymapBuilder(String name, int x, int y, int z,
			String dimension) {
		try {
			Class.forName("journeymap.client.JourneymapClient");
			String minecraftColor = Util.Color.minecraft.get("aqua");
			UTextComponent journeymapComponent = new UTextComponent(" " + minecraftColor + "[JM]§r");
			String waypoint = "/jm wpedit [name:\"" + name + "\", x:" + x + ", y:" + y + ", z:" + z
					+ ", dim:" + dimension + "]";
			journeymapComponent.setClick(ClickEvent.Action.RUN_COMMAND, waypoint);
			journeymapComponent.setHover(HoverEvent.Action.SHOW_TEXT,
					new UTextComponent(minecraftColor + "Click to create a new journey map waypoint!§r"));
			return journeymapComponent;
		} catch (Exception e) {
			return null;
		}
	}

	public static class Coordinate {
		private final int x;
		private final int y;
		private final int z;

		public Coordinate(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}
	}
}
