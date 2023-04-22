package net.usbwire.base.command

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil

import net.usbwire.base.BaseMod
import net.usbwire.base.config.VigilanceConfig

object BaseCommand : Command(BaseMod.modid) {
  @DefaultHandler
  fun handle() {
      val gui = VigilanceConfig.gui() ?: return
      GuiUtil.open(gui)
  }
}