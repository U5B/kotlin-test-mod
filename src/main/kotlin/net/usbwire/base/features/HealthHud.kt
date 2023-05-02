package net.usbwire.base.features

import java.awt.Color
import java.text.DecimalFormat

import gg.essential.elementa.*
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.gui.screen.ChatScreen

import net.usbwire.base.config.Config
import net.usbwire.base.BaseMod
import net.usbwire.base.features.Health
import kotlin.math.roundToInt

object HealthHud {
  val singleDecimalPlace = DecimalFormat("0.0")
  data class Help (
    val name: String,
    val health: Health.HealthData,
    val text: UIComponent
  )

  // TODO: Fix x and y not doing something properly
  val window by Window(ElementaVersion.V2)
  val container = UIContainer().constrain {
    x = Config.healthDrawX.pixels()
    y = Config.healthDrawY.pixels()
  } childOf window

  val cachedPlayer: MutableMap<String, Help> = mutableMapOf()
  var sortedCompoment: Map<String, Help> = mutableMapOf()

  fun updatePlayers() : Boolean {
    cachedPlayer.clear()
    for (entity in BaseMod.mc.world!!.players) {
      // TODO: have bad effects be different colors (wither, poison, etc, )
      // can you even get effects of other players on Monumenta??
      // is that even legal?
      val hp = Health.getHealthProperties(entity)
      if (hp.color == Color.WHITE) continue
      val name = UMessage(UTextComponent(entity.name)).unformattedText
      val maxHp = singleDecimalPlace.format(hp.max)
      val currentHp = singleDecimalPlace.format(hp.current)
      val message = "${name}: ${currentHp}/${maxHp}"
      val uitext = UIText(message).constrain {
        y = SiblingConstraint(padding = 2f)
       }
       uitext.setColor(hp.color)
       cachedPlayer[name] = Help(name, hp, uitext)
    }
    sortedCompoment = cachedPlayer.toList().sortedBy { it.second.health.percent }.toMap() // may not work
    return true
  }

  fun draw (matrix: UMatrixStack) { // ChatScreen
    val world = BaseMod.mc.world
    if (world == null) return
    if (BaseMod.mc.currentScreen != null && BaseMod.mc.currentScreen !is ChatScreen) return
    if (sortedCompoment.isNullOrEmpty() || (world.time % Config.healthUpdateTicks).toInt() == 0) {
      updatePlayers()
      container.setX(Config.healthDrawX.percent())
      container.setY(Config.healthDrawX.percent())
      container.clearChildren()
      sortedCompoment.forEach { container.addChild(it.value.text) }
    }
    if (sortedCompoment.isNotEmpty()) window.draw(matrix)
  }
}