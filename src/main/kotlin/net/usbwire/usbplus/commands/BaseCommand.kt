package net.usbwire.usbplus.commands

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.config.Config

object BaseCommand : Command(USBPlus.name.lowercase()) {
  @DefaultHandler
  fun handle() {
    val gui = Config.gui() ?: return
    GuiUtil.open(gui)
  }
}
