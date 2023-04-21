package net.usbwire

import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

class TestMod : ClientModInitializer  {
	private val logger = LoggerFactory.getLogger("test")

	override fun onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
	}
}