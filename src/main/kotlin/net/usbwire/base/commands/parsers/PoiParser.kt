package net.usbwire.base.commands.parsers

import gg.essential.api.commands.ArgumentParser
import gg.essential.api.commands.ArgumentQueue
import java.lang.reflect.Parameter
import net.usbwire.base.BaseMod
import net.usbwire.base.features.Poi

class PoiParser : ArgumentParser<PoiName> {
  override fun parse(arguments: ArgumentQueue, param: Parameter): PoiName {
    var name = ""
    while (arguments.isEmpty() == false) {
      name += arguments.poll()
      if (arguments.peek() != null) name += " "
    }
    return PoiName(name)
  }

  override fun complete(arguments: ArgumentQueue, param: Parameter): List<String> {
    var name = ""
    while (arguments.isEmpty() == false) {
      name += arguments.poll()
      if (arguments.peek() != null) name += " "
    }
    val suggestions = getPoiSuggestions()
    return suggestions.filter { it.lowercase().startsWith(name.lowercase()) }
  }

  private fun getPoiSuggestions(): List<String> = Poi.getCommandSuggestions()
}

data class PoiName(val name: String)
