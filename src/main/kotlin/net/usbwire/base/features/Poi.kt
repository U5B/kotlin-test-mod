package net.usbwire.base.features

import gg.essential.api.EssentialAPI
import gg.essential.universal.UMinecraft
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.minecraft.text.ClickEvent
import net.usbwire.base.BaseMod
import net.usbwire.base.util.XaeroPoi
import net.usbwire.base.commands.PoiCommand
import net.usbwire.base.commands.parsers.PoiName
import net.usbwire.base.commands.parsers.PoiParser
import net.usbwire.base.util.Util
import net.usbwire.base.config.VigilanceConfig

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
      val project = Json.decodeFromStream<Map<String, JsonPoi>>(it) // read JSON from a URL
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
    val coordinates = "${poi.coordinates.x}, ${poi.coordinates.y}, ${poi.coordinates.z}"
    val message = UMessage().mutable()
    // prefix
    val baseCompoment = UTextComponent("'${poi.name}':")
    message.addTextComponent(baseCompoment)
    // copy
    val copyCompoment = UTextComponent(" [COPY]")
    copyCompoment.clickAction = ClickEvent.Action.COPY_TO_CLIPBOARD
    copyCompoment.clickValue = "($coordinates)"
    message.addTextComponent(copyCompoment)
    // xaero minimap support
    try {
      Class.forName("xaero.common.XaeroMinimapSession")
      val xaeroCompoment = UTextComponent(" [XAERO]")
      val world = UMinecraft.getMinecraft().world
      if (world == null) throw Throwable("World not loaded!")
      // technically dimension but who cares
      // TODO: map poi.region to dimension and set waypoint in correct dimension
      val currentWorld = world.registryKey.value.toString().replace(":", "$")
      val xaeroColor = XaeroPoi.xaeroColorMap.get("dark_red")
      val waypoint =
          "xaero_waypoint_add:${poi.name}:${poi.name.uppercase()}:${poi.coordinates.x}:${poi.coordinates.y}:${poi.coordinates.z}:${xaeroColor}:false:0:Internal_dim%${currentWorld}_waypoints"
      xaeroCompoment.clickAction = ClickEvent.Action.RUN_COMMAND
      xaeroCompoment.clickValue = waypoint
      message.addTextComponent(xaeroCompoment)
    } catch (e: Exception) {}
    message.chat()
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
