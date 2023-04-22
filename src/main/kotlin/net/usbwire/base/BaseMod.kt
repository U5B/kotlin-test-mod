package net.usbwire.base

import gg.essential.universal.UMinecraft
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.LoggerFactory


import net.usbwire.base.command.BaseCommand
import net.usbwire.base.config.VigilanceConfig
import net.usbwire.base.features.Poi
object BaseMod : ClientModInitializer  {
	// get this from gradle.properties
	public val modid: String = "usb_base" // base_name
	public val name: String = "BaseMod"
	public val configPath: String = "./config/${modid}"
	public val logger = LoggerFactory.getLogger(modid)

	public val poi: Poi = Poi()

	override fun onInitializeClient() {
		logger.info("Hello Fabric world!")
		BaseCommand.register()
	}
}