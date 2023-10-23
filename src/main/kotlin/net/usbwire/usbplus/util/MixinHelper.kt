package net.usbwire.usbplus.util

import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen
import gg.essential.universal.wrappers.message.*
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
//#if MC>=12000
//$$ import net.minecraft.client.gui.DrawContext
//#endif
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text
import net.usbwire.usbplus.features.*
import net.usbwire.usbplus.features.health.*
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.USBPlus
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.Screens
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.PlayerInventory

/**
 * Technically is an event system, but more cursed.
 */
object MixinHelper {
	var firstRun = false;

	// copied from UScreen
	//#if MC>=12000
	//$$ private var drawContexts = mutableListOf<DrawContext>()
	//$$ private inline fun <R> withDrawContext(matrixStack: UMatrixStack, block: (DrawContext) -> R) {
	//$$   val client = USBPlus.mc!!
	//$$   val context = drawContexts.lastOrNull()
	//$$       ?: DrawContext(client, client.bufferBuilders.entityVertexConsumers)
	//$$   context.matrices.push()
	//$$   val mc = context.matrices.peek()
	//$$   val uc = matrixStack.peek()
	//$$   mc.positionMatrix.set(uc.model)
	//$$   mc.normalMatrix.set(uc.normal)
	//$$   block(context)
	//$$   context.matrices.pop()
	//$$ }
	//#endif

	fun init() {
		ClientTickEvents.START_CLIENT_TICK.register { _ -> run { clientTick() } }
		ClientTickEvents.START_WORLD_TICK.register { clientWorld -> run { worldTick(clientWorld) } }
		WorldRenderEvents.AFTER_ENTITIES.register { context -> run { renderTick(context) } }
		//#if MC>=12000
		//$$ HudRenderCallback.EVENT.register { context, ticks -> run {
		//$$   drawContexts.add(context)
		//$$   hudRender(UMatrixStack(context.getMatrices()), ticks)
		//$$   drawContexts.removeLast()
		//$$ } }
		//#else
		HudRenderCallback.EVENT.register { matrix, ticks -> run { hudRender(UMatrixStack(matrix), ticks) } }
		//#endif
		ScreenEvents.AFTER_INIT.register { client, screen, width, height -> run { openScreen(client, screen, width, height) } }
	}

	fun clientTick() {
		if (!firstRun) {
			Base.configDirty = true
			Poi.configChanged()
			Compass.configChanged()
			firstRun = true
		}
	}

	fun worldTick(clientWorld: ClientWorld) {
		Base.onWorldTick(clientWorld)
		Compass.onWorldTick()
		Debug.onWorldTick()
		Pickup.onWorldTick();
	}

	fun renderTick(context: WorldRenderContext) {
		Glow.onRenderTick(context)
	}

	fun hudRender(matrixStack: UMatrixStack, ticks: Float) {
		HUD.draw(matrixStack)
	}

	fun onMessage(mcText: Text?): Boolean {
		if (mcText == null) return false
		val message = UMessage(UTextComponent(mcText))
		if (message.unformattedText.lowercase().startsWith("[usbplus]")) return false // ignore our debug messages
		if (Config.debugEnabled) {
			USBPlus.logger.warn { "'${message.formattedText.replace("§", "%")}'" }
		}
		Poi.onChat(message)
		return false
	}

	fun openScreen(client: MinecraftClient, screen: Screen, width: Int, height: Int) {
		if (screen !is HandledScreen<*>) return
		ScreenEvents.remove(screen).register { screen1 -> run {
			Debug.openScreen(client, screen1)
		}}
	}
}
