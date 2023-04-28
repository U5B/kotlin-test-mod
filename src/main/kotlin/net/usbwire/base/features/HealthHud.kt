package net.usbwire.base.features

import gg.essential.elementa.*
import gg.essential.elementa.components.*
import gg.essential.universal.UMatrixStack

class HealthHud {
  val hud = Window(ElementaVersion.V2)

  val healthText = UIText("a")
  fun draw (matrix: UMatrixStack) {
    hud.draw(matrix)
  }
}