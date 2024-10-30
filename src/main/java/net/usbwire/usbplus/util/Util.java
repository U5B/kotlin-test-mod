package net.usbwire.usbplus.util;

import gg.essential.universal.UChat;
import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.usbwire.usbplus.USBPlus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Random utilities for debugging
 */
public class Util {
	public static boolean createPath(Path file) {
		createDirectory(file);
		if (!Files.exists(file)) {
			try {
				Files.createFile(file);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean createDirectory(Path file) {
		if (!Files.exists(file.getParent())) {
			try {
				Files.createDirectories(file.getParent());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static String cleanString(String str) {
		String string = str.replaceAll("'", "").replaceAll("\\n", "").replaceAll(" ", "");
		return trimString(string);
	}

	public static String trimString(String str) {
		return str.trim().toLowerCase();
	}

	public static void chat(String str) {
		UChat.chat(UChat.addColor("§7[§a" + USBPlus.name + "§7]§r " + str));
	}

	public static void chat(UMessage message) {
		UTextComponent prefix = new UTextComponent(UChat.addColor("§7[§a" + USBPlus.name + "§7]§r "));
		message.addTextComponent(0, prefix);
		message.chat();
	}

	public static String getDimension() {
		if (USBPlus.mc.world == null) {
			return "";
		}
		return USBPlus.mc.world.getRegistryKey().getValue().toString();
	}

	public static class Color {
		public static final Map<String, String> xaero = Map.ofEntries(Map.entry("black", "0"),
				Map.entry("dark_blue", "1"), Map.entry("dark_green", "2"), Map.entry("dark_aqua", "3"),
				Map.entry("dark_red", "4"), Map.entry("dark_purple", "5"), Map.entry("gold", "6"),
				Map.entry("gray", "7"), Map.entry("dark_gray", "8"), Map.entry("blue", "9"),
				Map.entry("green", "10"), Map.entry("aqua", "11"), Map.entry("red", "12"),
				Map.entry("light_purple", "13"), Map.entry("yellow", "14"), Map.entry("white", "15"));

		public static final Map<String, String> minecraft = Map.ofEntries(Map.entry("black", "§0"),
				Map.entry("dark_blue", "§1"), Map.entry("dark_green", "§2"), Map.entry("dark_aqua", "§3"),
				Map.entry("dark_red", "§4"), Map.entry("dark_purple", "§5"), Map.entry("gold", "§6"),
				Map.entry("gray", "§7"), Map.entry("dark_gray", "§8"), Map.entry("blue", "§9"),
				Map.entry("green", "§a"), Map.entry("aqua", "§b"), Map.entry("red", "§c"),
				Map.entry("light_purple", "§d"), Map.entry("yellow", "§e"), Map.entry("white", "§f"));
	}
}
