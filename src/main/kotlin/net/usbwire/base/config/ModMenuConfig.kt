package net.usbwire.base.config

import com.terraformersmc.modmenu.api.ModMenuApi
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import net.minecraft.client.gui.screen.Screen

import net.usbwire.base.config.VigilanceConfig

class ModMenuConfig : ModMenuApi {
  override fun getModConfigScreenFactory() =
    ConfigScreenFactory<Screen> { VigilanceConfig.gui() }
}