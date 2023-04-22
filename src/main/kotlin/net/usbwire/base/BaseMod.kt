package net.usbwire.base

import gg.essential.universal.UMinecraft
import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

import net.usbwire.base.command.BaseCommand
import net.usbwire.base.config.VigilanceConfig

object BaseMod : ClientModInitializer  {
	// get this from gradle.properties
	public val modid: String = "usb_base" // base_name
	public val name: String = "BaseMod"
	public val configPath: String = "./config/${modid}"
	private val logger = LoggerFactory.getLogger(modid)

	override fun onInitializeClient() {
		logger.info("Hello Fabric world!")
		UMinecraft.getMinecraft().send {
			VigilanceConfig.preload()
			BaseCommand.register()
		}
	}
}