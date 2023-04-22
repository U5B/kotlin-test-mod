package net.usbwire.base.util

import java.nio.file.Files
import java.nio.file.Path

import net.usbwire.base.BaseMod
import gg.essential.universal.UChat

object Util {
  fun createPath (file: Path): Boolean {
    if (Files.exists(file) == false) {
      Files.createDirectories(file.parent)
      Files.createFile(file)
      return true
    }
    return false
  }

  fun cleanString (str: String): String {
    var string = str.replace(Regex("/'/g"), "")
    string = string.replace(Regex("/\\n/g"), "")
    string = string.replace(Regex("/ /g"), "")
    string = trimString(string)
    return string
  }

  fun trimString (str: String): String {
    var string = str.trim()
    string = string.lowercase()
    return string
  }

  fun chat (str: String) {
    UChat.chat(UChat.addColor("[${BaseMod.name}] ${str}"))
  }
}