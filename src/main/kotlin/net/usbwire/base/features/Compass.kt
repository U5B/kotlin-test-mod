package net.usbwire.base.features

import gg.essential.universal.UMouse
import gg.essential.universal.wrappers.UPlayer
import net.minecraft.util.registry.Registry

import net.usbwire.base.util.chat.Coordinates
import net.usbwire.base.BaseMod
import net.usbwire.base.util.Util

object Compass {
  fun getCompass (): Coordinates.Coordinates {
    val spawnPos = BaseMod.mc.world!!.spawnPos
    return Coordinates.Coordinates(spawnPos.x, spawnPos.y, spawnPos.z)
  }

  fun createCompass (compass : Coordinates.Coordinates = getCompass()) {
    val dimension = BaseMod.mc.world!!.registryKey.value.toString()
    val name = "Compass"
    val x = compass.x
    val y = compass.y
    val z = compass.z
    val message = Coordinates.coordinateBuilder(name, x, y, z, dimension)
    Util.chat(message)
  }

  var clicked = false

  fun onTick () {
    val click = BaseMod.mc.mouse.wasLeftButtonClicked()
    if (click == true && clicked == false) {
      val mainItem = Registry.ITEM.getId(BaseMod.mc.player?.mainHandStack?.item).toString()
      if (mainItem == "minecraft:compass") {
        createCompass()
      }
    }
    clicked = click
  }
}