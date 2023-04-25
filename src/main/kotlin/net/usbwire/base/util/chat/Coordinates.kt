package net.usbwire.base.util.chat

import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.minecraft.text.ClickEvent
import net.usbwire.base.BaseMod
import net.usbwire.base.util.ColorUtil

object Coordinates {
  var supportsXaero = true
  var supportsJourneymap = true
  fun coordinateBuilder(name: String, x: Int, y: Int, z: Int, dimension: String): UMessage {
    val message = UMessage().mutable()
    val coordinates = "${x}, ${y}, ${z}"

    // prefix
    val baseCompoment = UTextComponent("'${name}':")
    message.addTextComponent(baseCompoment)

    // copy
    val copyCompoment = UTextComponent(" §a(${coordinates})§r")
    copyCompoment.clickAction = ClickEvent.Action.COPY_TO_CLIPBOARD
    copyCompoment.clickValue = coordinates
    message.addTextComponent(copyCompoment)

    // xaero minimap support
    if (supportsXaero == true) {
      val xaeroCompoment = xaeroBuilder(name, x, y, z, dimension)
      if (xaeroCompoment == null) {
        supportsXaero = false
      } else {
        message.addTextComponent(xaeroCompoment)
      }
    }

    // journeymap support
    if (supportsJourneymap == true) {
      val journeymapCompoment = journeymapBuilder(name, x, y, z, dimension)
      if (journeymapCompoment == null) {
        supportsJourneymap = false
      } else {
        message.addTextComponent(journeymapCompoment)
      }
    }
    return message
  }

  private fun xaeroBuilder(
      name: String,
      x: Int,
      y: Int,
      z: Int,
      dimension: String
  ): UTextComponent? {
    try { // https://github.com/U5B/jsmacros/blob/eb9e5aafa2ac56fef6cd74c432b3b8ac07840d25/scripts/lib/xaero.ts#L69
      Class.forName("xaero.common.XaeroMinimapSession")
      // technically dimension but who cares
      // TODO: map poi.region to dimension and set waypoint in correct dimension
      val currentDimension = dimension.replace(":", "$")
      val xaeroColor = ColorUtil.xaeroColorMap["dark_red"]
      val minecraftColor = ColorUtil.minecraftColorMap["dark_red"]
      val xaeroCompoment = UTextComponent(" ${minecraftColor}[XAERO]§r")
      val waypoint =
          "xaero_waypoint_add:${name}:${name[0].uppercase()}:${x}:${y}:${z}:${xaeroColor}:false:0:Internal_dim%${currentDimension}_waypoints"
      BaseMod.logger.info("Xaero: " + waypoint)
      // val shareableWaypoint =
      // "xaero-waypoint:${poi.name}:${poi.name[0].uppercase()}:${poi.coordinates.x}:${poi.coordinates.y}:${poi.coordinates.z}:${xaeroColor}:false:0:Internal-dim%${currentWorld}-waypoints"
      xaeroCompoment.clickAction = ClickEvent.Action.RUN_COMMAND
      xaeroCompoment.clickValue = waypoint
      return xaeroCompoment
    } catch (e: Exception) {
      return null
    }
  }

  private fun journeymapBuilder(
      name: String,
      x: Int,
      y: Int,
      z: Int,
      dimension: String
  ): UTextComponent? {
    try {
      Class.forName("journeymap.client.JourneymapClient")
      val minecraftColor = ColorUtil.minecraftColorMap["aqua"]
      val journeymapCompoment = UTextComponent(" ${minecraftColor}[JM]§r")
      val waypoint = "/jm wpedit [name:\"${name}\", x:${x}, y:${y}, z:${z}, dim:${dimension}]"
      journeymapCompoment.clickAction = ClickEvent.Action.RUN_COMMAND
      journeymapCompoment.clickValue = waypoint
      return journeymapCompoment
    } catch (e: Exception) {
      return null
    }
  }
}
