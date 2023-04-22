package net.usbwire.base.features.Poi

import kotlinx.serialization.*
import kotlinx.serialization.json.*

import java.net.URL
import java.io.File
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files
import java.nio.file.Path
import java.io.FileOutputStream
import net.usbwire.base.BaseMod

@Serializable
data class JsonPoi (
  val name: String,
  val shard: String,
  val region: String?,
  val subregion: String?,
  val coordinates: JsonPoiCoordinates?
)

@Serializable
data class JsonPoiCoordinates (
  val x: Int,
  val y: Int,
  val z: Int
)

@kotlinx.serialization.ExperimentalSerializationApi
class Poi {
  fun fetchNewest (): Unit {
    try {
      URL("https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json").openStream().use {
        val project = Json.decodeFromStream<Map<String, JsonPoi>>(it) // read JSON from a URL
        val iter = project.values.iterator()
        while (iter.hasNext()) {
          
        }
  
        FileOutputStream(File("project.json")).use {  // and save to a file
            Json.encodeToStream(project, it)
        }
      }
    }
  }
}