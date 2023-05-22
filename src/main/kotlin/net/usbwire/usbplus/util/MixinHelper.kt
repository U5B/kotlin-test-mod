package net.usbwire.usbplus.util

import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.UTextComponent
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text
import net.usbwire.usbplus.features.*
import net.usbwire.usbplus.config.Config

/**
 * Technically is an event system, but more cursed.
 */
object MixinHelper {
	fun init() {
		ClientTickEvents.START_WORLD_TICK.register { clientWorld -> run { worldTick(clientWorld) } }
		WorldRenderEvents.AFTER_ENTITIES.register { context -> run { renderTick(context) } }
		HudRenderCallback.EVENT.register { matrix, ticks -> run { hudRender(matrix, ticks) } }
	}

	fun worldTick(clientWorld: ClientWorld) {
		HealthHud.onWorldTick(clientWorld)
		Compass.onWorldTick()
		Debug.onWorldTick()
	}

	fun renderTick(context: WorldRenderContext) {
		Health.onRenderTick(context)
	}

	fun hudRender(matrixStack: MatrixStack, ticks: Float) {
		val matrix = UMatrixStack(matrixStack)
		HealthHud.draw(matrix)
	}

	fun onMessage(mcText: Text): Boolean {
		val message = UTextComponent(mcText)
		if (message.unformattedText.lowercase().startsWith("[USBPlus]")) return false
		if (Config.debugEnabled) {
			Util.chat("Unformatted:" + message.unformattedText)
			Util.chat("Formatted:" + message.formattedText.replace('§', '&'))
		}
		return false
	}
}
