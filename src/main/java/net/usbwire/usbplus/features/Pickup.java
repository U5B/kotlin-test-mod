package net.usbwire.usbplus.features;

import gg.essential.universal.UChat;
import net.minecraft.entity.player.PlayerEntity;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.util.Util;

/**
 * Pickup Filter toggle when sneaking like RLCraft
 */
// I AHTE USB ANMD HIS COBE HE SUCKS
public class Pickup {
	private static int ticksSneaking = 0;
	private static boolean isSneaking = false;

	public static void onWorldTick() {
		PlayerEntity player = USBPlus.mc.player;
		if (player == null || !Config.pickupEnabled)
			return;
		// velocity check
		var velocity = player.getVelocity();
		if (!(player.isSpectator() || player.isCreative()) && player.isSneaking() && player.isOnGround()
				&& player.getPitch() >= 55.0
				&& (Math.abs(velocity.x) <= 0.05 && Math.abs(velocity.z) <= 0.05)) {
			// turn on pickup
			ticksSneaking++;
			if (!isSneaking && ticksSneaking > Config.pickupDelay) {
				Util.say("/pu all");
				isSneaking = true;
			}
		} else if (!player.isSneaking()) { // we only want this code to trigger when they stop sneaking
			// turn off pickup
			if (ticksSneaking > Config.pickupDelay) {
				String pickupMode = getMode(Config.pickupMode);
				Util.say("/pu " + pickupMode);
			}
			isSneaking = false;
			ticksSneaking = 0;
		}
	}

	public static String getMode(int mode) {
		return switch (mode) {
			case 0 -> "interesting";
			case 1 -> "lore";
			case 2 -> "tiered";
			default -> "interesting";
		};
	}
}
