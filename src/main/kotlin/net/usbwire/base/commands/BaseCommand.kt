package net.usbwire.base.commands

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil
import net.usbwire.base.BaseMod
import net.usbwire.base.config.Config

object BaseCommand : Command(BaseMod.name.lowercase()) {
  @DefaultHandler
  fun handle() {
    val gui = Config.gui() ?: return
    GuiUtil.open(gui)
  }
}
