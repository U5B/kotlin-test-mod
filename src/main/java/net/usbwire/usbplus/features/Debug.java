package net.usbwire.usbplus.features;

import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.util.Util;
import net.usbwire.usbplus.util.chat.Clipboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerInventory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Debugging for future NPC scraper
 * TODO: actually create NPC scraper
 */
public class Debug {
	private static boolean clicked = false;

	public static void onWorldTick() {
		if (!Config.debugEnabled)
			return;
		boolean click = USBPlus.mc.mouse.wasMiddleButtonClicked();
		if (click && !clicked) {
			if (USBPlus.mc.targetedEntity != null) {
				printEntityInfo(USBPlus.mc.targetedEntity);
			} else {
				printNearbyPlayers();
			}
		}
		clicked = click;
	}

	public static void printNearbyPlayers() {
		Iterable<Entity> entities = USBPlus.mc.world.getEntities();
		if (entities == null)
			return;
		for (Entity entity : entities) {
			if (entity == USBPlus.mc.player)
				continue;
			if (entity.distanceTo(USBPlus.mc.player) >= 5.0f)
				continue;
			printEntityInfo(entity);
		}
	}

	public static void printEntityInfo(Entity entity) {
		if (entity == null)
			return;
		UTextComponent textCustomName = new UTextComponent(entity.getCustomName());
		Util.chat("Entity Debug Information:");
		String name = new UMessage(new UTextComponent(entity.getName())).getUnformattedText();
		String displayName =
				new UMessage(new UTextComponent(entity.getDisplayName())).getUnformattedText();
		String customName = (textCustomName == null) ? "null"
				: new UMessage(new UTextComponent(textCustomName)).getUnformattedText();
		String entityType = new UMessage(new UTextComponent(entity.getType().getUntranslatedName()))
				.getUnformattedText();
		Util.chat("Name: " + name);
		Util.chat("DisplayName: " + displayName);
		Util.chat("CustomName: " + customName);
		Util.chat("Type: " + entityType);
		Util.chat("Pos: (" + entity.getX() + ", " + entity.getY() + ", " + entity.getZ() + ")");
	}

	public static void openScreen(MinecraftClient client, Screen screen) {
		if (!Config.debugContainer)
			return;
		if (!(screen instanceof HandledScreen))
			return;
		if (!(screen instanceof GenericContainerScreen) && !(screen instanceof ShulkerBoxScreen))
			return;
		ScreenHandler container = ((HandledScreen<?>) screen).getScreenHandler();
		StringBuilder strings = new StringBuilder("```\n");
		String lineSeparator = System.getProperty("line.separator");
		List<ItemStack> itemList = container.slots.stream()
				.filter(slot -> slot.hasStack() && !(slot.inventory instanceof PlayerInventory))
				.map(Slot::getStack).toList();
		Map<ItemStack, Integer> itemMap = condenseItems(itemList);
		String lastItem = "";
		for (Map.Entry<ItemStack, Integer> mappedItem : itemMap.entrySet()) {
			ItemStack item = mappedItem.getKey();
			int count = mappedItem.getValue();
			String name = new UMessage(item.getName()).getUnformattedText();
			String line = "+" + count + " " + name;
			lastItem = line;
			strings.append(line).append(" ").append(lineSeparator);
		}
		strings.append("```");
		Util.chat(
				Clipboard.clipboardBuilder("Container with \"" + lastItem + "\"", strings.toString()));
	}

	public static Map<ItemStack, Integer> condenseItems(List<ItemStack> list) {
		Map<ItemStack, Integer> map = new HashMap<>();
		for (ItemStack newStack : list) {
			boolean combined = false;
			ItemStack newStackCopy = newStack.copy();
			newStackCopy.setCount(1);
			for (ItemStack otherStack : map.keySet()) {
				if (ItemStack.canCombine(otherStack, newStackCopy)) {
					int newCount = map.getOrDefault(otherStack, 0) + newStack.getCount();
					map.remove(otherStack);
					map.put(otherStack, newCount);
					combined = true;
					break;
				}
			}
			if (!combined) {
				map.put(newStackCopy, newStack.getCount());
			}
		}
		return map;
	}
}
