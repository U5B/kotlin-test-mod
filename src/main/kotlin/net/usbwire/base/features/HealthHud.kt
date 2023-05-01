package net.usbwire.base.features

import gg.essential.elementa.*
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.universal.UMatrixStack

import net.usbwire.base.config.Config

//
object HealthHud {
  val window by Window(ElementaVersion.V2)
  init {
    val container = UIContainer().constrain {
      x = 0.pixels
      y = 0.pixels
    }
    Inspector(window).constrain {
      x = 10.pixels(true)
      y = 10.pixels(true)
    } childOf window

  }
  fun draw (matrix: UMatrixStack) {
    window.draw(matrix)
  }
}