package net.usbwire.base.features

import gg.essential.api.EssentialAPI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.usbwire.base.BaseMod
import net.usbwire.base.commands.PoiCommand
import net.usbwire.base.commands.parsers.PoiName
import net.usbwire.base.commands.parsers.PoiParser
import net.usbwire.base.config.VigilanceConfig
import net.usbwire.base.util.Util
import net.usbwire.base.util.chat.Coordinates

object Poi {
  @Serializable
  data class JsonPoi(
      val name: String,
      val shard: String,
      val region: String?,
      val subregion: String?,
      val coordinates: JsonPoiCoordinates?
  )

  @Serializable data class JsonPoiCoordinates(val x: Int, val y: Int, val z: Int)

  val poiPath = Path.of("${BaseMod.configPath}/pois.json")
  var poiMap: Map<String, JsonPoi> = emptyMap()
  var poiSuggestions: List<String> = emptyList()
  var firstRun: Boolean = true

  @kotlinx.serialization.ExperimentalSerializationApi
  fun fetchPoiData() {
    URL("https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json").openStream().use {
      val project = Json.decodeFromStream<Map<String, JsonPoi>>(it) // read JSON from a URL
      poiMap = project
      savePoiData()
    }
  }

  @kotlinx.serialization.ExperimentalSerializationApi
  fun loadPoiData() {
    if (Files.notExists(poiPath)) return fetchPoiData() // don't use file if it doesn't exist
    Files.newInputStream(poiPath).use {
      val project = Json.decodeFromStream<Map<String, JsonPoi>>(it) // read JSON from file
      poiMap = project
    }
  }

  @kotlinx.serialization.ExperimentalSerializationApi
  private fun savePoiData() {
    if (poiMap.isEmpty()) return
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
    if (poiSuggestions.isEmpty()) return makeCommandSuggestions()
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
      if (tags.contains(Util.trimString(input))) response.add(poi.value)
    }
    if (response.size == 0) {
      poiMap.forEach { poi ->
        if (Util.cleanString(poi.value.name).contains(Util.cleanString(input)))
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

  fun changeState(value: Boolean = VigilanceConfig.poiEnabled) {
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
