package net.usbwire.usbplus.util;

import gg.essential.universal.UMatrixStack;
import gg.essential.universal.UScreen;
import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.features.health.*;
import net.usbwire.usbplus.features.Compass;
import net.usbwire.usbplus.features.Debug;
import net.usbwire.usbplus.features.Pickup;
import net.usbwire.usbplus.features.Poi;
import net.usbwire.usbplus.features.health.HUD;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MixinHelper {
	private static boolean firstRun = false;

	// Initialize event listeners
	public static void init() {
		ClientTickEvents.START_CLIENT_TICK.register(client -> clientTick());
		ClientTickEvents.START_WORLD_TICK.register(MixinHelper::worldTick);
		WorldRenderEvents.AFTER_ENTITIES.register(MixinHelper::renderTick);
		//#if MC>=12000
		//$$ HudRenderCallback.EVENT.register((context, ticks) -> hudRender(new UMatrixStack(context.getMatrices()), ticks));
		//#else
		HudRenderCallback.EVENT
				.register((matrixStack, ticks) -> hudRender(new UMatrixStack(matrixStack), ticks));
		//#endif
		ScreenEvents.AFTER_INIT.register(MixinHelper::openScreen);
	}

	// Handle client tick events
	private static void clientTick() {
		if (!firstRun) {
			Base.configDirty = true;
			Poi.configChanged();
			Compass.configChanged();
			firstRun = true;
		}
	}

	// Handle world tick events
	private static void worldTick(ClientWorld clientWorld) {
		Base.onWorldTick(clientWorld);
		Compass.onWorldTick();
		Debug.onWorldTick();
		Pickup.onWorldTick();
	}

	// Handle render tick events
	private static void renderTick(WorldRenderContext context) {
		Glow.onRenderTick(context);
	}

	// Handle HUD render events
	private static void hudRender(UMatrixStack matrixStack, float ticks) {
		HUD.draw(matrixStack);
	}

	// Handle chat messages
	public static boolean onMessage(Text mcText) {
		if (mcText == null)
			return false;
		UMessage message = new UMessage(new UTextComponent(mcText));
		if (message.getUnformattedText().toLowerCase().startsWith("[usbplus]"))
			return false; // ignore our debug messages
		if (Config.debugEnabled) {
			USBPlus.logger.warn("'" + message.getFormattedText().replace("§", "%") + "'");
		}
		Poi.onChat(message);
		return false;
	}

	// Handle screen initialization events
	private static void openScreen(MinecraftClient client, Screen screen, int width, int height) {
		if (!(screen instanceof HandledScreen<?>))
			return;
		ScreenEvents.remove(screen).register(screen1 -> Debug.openScreen(client, screen1));
	}
}
