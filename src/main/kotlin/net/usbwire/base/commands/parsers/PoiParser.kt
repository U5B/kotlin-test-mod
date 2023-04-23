package net.usbwire.base.commands.parsers

import gg.essential.api.commands.ArgumentParser
import gg.essential.api.commands.ArgumentQueue
import java.lang.reflect.Parameter

import net.usbwire.base.BaseMod

class PoiParser : ArgumentParser<PoiName> {
    override fun parse(arguments: ArgumentQueue, param: Parameter): PoiName {
        val name = arguments.poll()
        return PoiName(name)
    }

    override fun complete(arguments: ArgumentQueue, param: Parameter): List<String> {
        val nameStart = arguments.poll()
        val suggestions = getPoiSuggestions()
        return suggestions.map { it }.filter { it.startsWith(nameStart) }
    }

    private fun getPoiSuggestions() = BaseMod.poi.getCommandSuggestions()
}
data class PoiName(val name: String)