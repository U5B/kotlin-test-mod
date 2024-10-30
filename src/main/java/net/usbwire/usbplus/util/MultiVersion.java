package net.usbwire.usbplus.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import java.awt.Color;

import net.usbwire.usbplus.interfaces.EntityMixinInterface;
import net.usbwire.usbplus.USBPlus;

/**
 * For when there are differences between versions
 * Inspired by JsMacros
 */
public final class MultiVersion {
	private MultiVersion() {}

	public final class ItemHelper {

		private ItemHelper() {}

		/**
		 * returns item identifier (minecraft:item)
		 */
		public static String getItemId(Item item) {
			return Registries.ITEM.getId(item).toString();
		}
	}

	public final class EntityHelper {

		private EntityHelper() {}

		public static int getGlowingColor(Entity entity) {
			return entity.getTeamColorValue();
		}

		public static void setGlowingColor(Entity entity, Color color) {
			EntityMixinInterface special = (EntityMixinInterface) entity;
			int intColor = color.getRGB();
			if (entity.getTeamColorValue() == intColor)
				return;
			special.usbplus_setGlowingColor(intColor);
		}

		public static void resetGlowingColor(Entity entity) {
			EntityMixinInterface special = (EntityMixinInterface) entity;
			special.usbplus_resetColor();
		}

		public static void setGlowing(Entity entity, boolean value) {
			EntityMixinInterface special = (EntityMixinInterface) entity;
			special.usbplus_setForceGlowing(value ? 2 : 0);
		}

		public static boolean isGlowingForced(Entity entity) {
			EntityMixinInterface special = (EntityMixinInterface) entity;
			return special.usbplus_getForceGlowing() != 1;
		}

		public static void resetGlowing(Entity entity) {
			EntityMixinInterface special = (EntityMixinInterface) entity;
			special.usbplus_setForceGlowing(1);
		}
	}
}
