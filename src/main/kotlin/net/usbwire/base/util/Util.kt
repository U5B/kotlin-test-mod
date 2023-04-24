package net.usbwire.base.util

import gg.essential.universal.UChat
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import java.nio.file.Files
import java.nio.file.Path
import net.usbwire.base.BaseMod

object Util {
  fun createPath(file: Path): Boolean {
    createDirectory(file)
    if (Files.exists(file) == false) {
      Files.createFile(file)
      return true
    }
    return false
  }

  fun createDirectory(file: Path): Boolean {
    if (Files.exists(file.parent) == false) {
      Files.createDirectories(file.parent)
      return true
    }
    return false
  }

  fun cleanString(str: String): String {
    var string = str.replace(Regex("/'/g"), "")
    string = string.replace(Regex("/\\n/g"), "")
    string = string.replace(Regex("/ /g"), "")
    string = trimString(string)
    return string
  }

  fun trimString(str: String): String {
    var string = str.trim()
    string = string.lowercase()
    return string
  }

  fun chat(str: String) {
    UChat.chat(UChat.addColor("§7[§a${BaseMod.name}§7]§r ${str}"))
  }

  fun chat (message: UMessage) {
    val prefix = UTextComponent(UChat.addColor("§7[§a${BaseMod.name}§7]§r "))
    message.addTextComponent(0, prefix).chat()
  }
}
