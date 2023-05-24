package net.usbwire.usbplus.util

import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.*
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text
import net.usbwire.usbplus.features.*
import net.usbwire.usbplus.features.health.*
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.USBPlus

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
		Base.onWorldTick(clientWorld)
		Compass.onWorldTick()
		Debug.onWorldTick()
	}

	fun renderTick(context: WorldRenderContext) {
		Glow.onRenderTick(context)
	}

	fun hudRender(matrixStack: MatrixStack, ticks: Float) {
		val matrix = UMatrixStack(matrixStack)
		HUD.draw(matrix)
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
}
