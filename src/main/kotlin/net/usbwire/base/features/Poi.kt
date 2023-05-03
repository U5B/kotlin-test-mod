package net.usbwire.base.features

import gg.essential.api.EssentialAPI
import gg.essential.api.utils.WebUtil
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.usbwire.base.BaseMod
import net.usbwire.base.commands.PoiCommand
import net.usbwire.base.commands.parsers.PoiName
import net.usbwire.base.commands.parsers.PoiParser
import net.usbwire.base.config.Config
import net.usbwire.base.util.Util
import net.usbwire.base.util.chat.Coordinates

object Poi {
  @Serializable
  data class JsonPoi(
      val name: String,
      val shard: String,
      val region: String?,
      val subregion: String?,
      val coordinates: Coordinates.Coordinates?
  )

  val poiPath = Path.of("${BaseMod.configPath}/pois.json")
  var poiMap: Map<String, JsonPoi> = emptyMap()
  var poiSuggestions: List<String> = emptyList()
  var firstRun = true

  fun fetchPoiData() {
    URL(Config.poiUrl).openStream().use {
      val project = Json.decodeFromStream<Map<String, JsonPoi>>(it) // read JSON from a URL
      updatePoiData(project)
      savePoiData()
    }
  }

  fun loadPoiData() {
    if (Files.notExists(poiPath) == true) return fetchPoiData() // don't use file if it doesn't exist
    Files.newInputStream(poiPath).use {
      val project = Json.decodeFromStream<Map<String, JsonPoi>>(it) // read JSON from file
      updatePoiData(project)
    }
  }

  private fun updatePoiData (project: Map<String, JsonPoi>) {
    poiMap = project
    makeCommandSuggestions()
  }

  private fun savePoiData() {
    if (poiMap.isEmpty() == true) return
    Util.createPath(poiPath)
    Files.newOutputStream(poiPath).use { Json.encodeToStream(poiMap, it) }
  }

  fun makeCommandSuggestions(): List<String> {
    val suggestions = ArrayList<String>()
    poiMap.values.forEach { poi -> suggestions.add(poi.name) }
    poiSuggestions = suggestions.toList()
    return poiSuggestions
  }

  fun getCommandSuggestions(): List<String> {
    if (poiSuggestions.isEmpty() == true) return makeCommandSuggestions()
    return poiSuggestions
  }

  fun searchPoi(input: String): ArrayList<JsonPoi>? {
    // acutal logic
    val response = ArrayList<JsonPoi>()
    poiMap.forEach { poi ->
      if (input == poi.value.name) {
        response.add(poi.value)
        return response
      }
    }
    poiMap.forEach { poi ->
      val tags = Util.trimString(poi.value.name).split(' ')
      if (tags.contains(Util.trimString(input)) == true) response.add(poi.value)
    }
    if (response.size == 0) {
      poiMap.forEach { poi ->
        if (Util.cleanString(poi.value.name).contains(Util.cleanString(input)) == true)
            response.add(poi.value)
      }
    }
    if (response.size > 0) return response
    return null
  }

  fun responsePoi(input: String, poi: JsonPoi) {
    if (poi.coordinates == null) {
      Util.chat("'${input}': No POI found.")
      return
    }
    val dimension = BaseMod.mc.world!!.registryKey.value.toString()
    val name = poi.name
    val x = poi.coordinates.x
    val y = poi.coordinates.y
    val z = poi.coordinates.z
    val message = Coordinates.coordinateBuilder(name, x, y, z, dimension)
    Util.chat(message)
  }

  fun changeState(value: Boolean = Config.poiEnabled) {
    if (value == true && firstRun == true) {
      EssentialAPI.getCommandRegistry().registerParser(PoiName::class.java, PoiParser())
      firstRun = false
    }
    if (value == true) {
      loadPoiData()
      PoiCommand.register()
    } else if (value == false) {
      EssentialAPI.getCommandRegistry().unregisterCommand(PoiCommand)
    }
  }
}
