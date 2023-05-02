package net.usbwire.base.features

import java.awt.Color

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

//
object HealthHud {
  data class Help (
    val name: String
    val health: Health.HealthData 
    val text: UIComponent
  )

  val window by Window(ElementaVersion.V2)
  val container = UIContainer().constrain {
    x = Config.healthDrawX.percent()
    y = Config.healthDrawY.percent()
    width = ChildBasedSizeConstraint(padding = 2f)
    height = ChildBasedMaxSizeConstraint()
  } childOf window

  val cachedPlayer: MutableMap<String, Help> = mutableMapOf()
  var sortedCompoment: Map<String, Help> = mutableMapOf()

  init {
    Inspector(window).constrain {
      x = 10.pixels(true)
      y = 10.pixels(true)
    } childOf window
  }

  fun updatePlayers() : Boolean {
    cachedPlayer.clear()
    for (entity in BaseMod.mc.world!!.players) {
      val hp = Health.getHealthProperties(entity)
      val name = entity.name.toString()
      val message = "${name}: ${hp.current}/${hp.max}"
      val uitext = UIText(message).constrain { 
        x = SiblingConstraint(padding = 2f)
       }.setColor(hp.color)
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
      container.clearChildren()
      sortedCompoment.forEach { container.addChild(it.value.text) }
    }
    if (sortedCompoment.isNotEmpty()) window.draw(matrix)
  }
}