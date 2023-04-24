package net.usbwire.base.util

import gg.essential.universal.UMinecraft

class McUtil {
  class mc {
    fun getDimensionName (): String {
      return UMinecraft.getWorld()!!.getRegistryKey().getValue().toString().replace(":", "$")
    }
  }
}