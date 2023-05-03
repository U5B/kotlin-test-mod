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
import gg.essential.api.utils.GuiUtil
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.world.ClientWorld

import net.usbwire.base.config.Config
import net.usbwire.base.BaseMod
import net.usbwire.base.features.Health
import net.usbwire.base.util.Util

object HealthHud {
  val DEC = DecimalFormat("0.0")
  data class Data (
    val name: String,
    val health: Health.HealthData,
    val text: UIComponent
  )

  val cachedPlayer: MutableMap<String, Data> = mutableMapOf()
  var sortedCompoment: Map<String, Data> = mutableMapOf()

  val xPos: State<Number> = BasicState(Config.healthDrawX)
  val yPos: State<Number> = BasicState(Config.healthDrawY)

  // TODO: Fix x and y not doing something properly
  val window by Window(ElementaVersion.V2).constrain {
    width = FillConstraint(true)
    height = FillConstraint(true)
  }
  val container = UIContainer().constrain {
    x = xPos.percent
    y = yPos.percent
    width = ChildBasedMaxSizeConstraint()
  } childOf window

  fun updatePlayers(world: ClientWorld) {
    val previousPlayer = cachedPlayer.toMap()
    cachedPlayer.clear()
    for (entity in world.players) {
      // if (entity == BaseMod.mc.player) continue // exclude yourself
      // TODO: have bad effects be different colors (wither, poison, etc, )
      // can you even get effects of other players on Monumenta??
      // is that even legal?
      val hp = Health.getHealthProperties(entity)
      if (hp.color == Color.WHITE) continue
      val name = UMessage(UTextComponent(entity.name)).unformattedText
      val maxHp = DEC.format(hp.max)
      val currentHp = DEC.format(hp.current)
      var message = "${name}: ${currentHp}/${maxHp}"
      var alignConstraint: XConstraint
      if (Config.healthDrawAlign == 2) { // right align
        alignConstraint = 0.pixels(true)
      } else if (Config.healthDrawAlign == 1) { // center align
        alignConstraint = CenterConstraint()
      } else { // left align
        alignConstraint = 0.pixels()
      }
      val line = UIText(message).constrain {
        x = alignConstraint
        y = SiblingConstraint(2f)
        color = hp.color.toConstraint()
       }
       // Absorption?
      if (hp.absorption > 0) {
        val abHp = DEC.format(hp.absorption)
        UIText("+${abHp}").constrain {
          x = (line.getWidth() + 2.0f).pixels
          color = Color.ORANGE.toConstraint()
        } childOf line
      }
      // Damage Change
      if (previousPlayer[name] != null) {
        val prevHp = previousPlayer[name]!!.health
        val changeHp = (hp.current + hp.absorption) - (prevHp.current + prevHp.absorption)
        val damageHp = DEC.format(changeHp)
        if (changeHp > 0) {
          UIText("+${damageHp}").constrain {
            x = (line.getWidth() + 4.0f).pixels
            color = Color.GREEN.toConstraint()
          } childOf line
        } else if (changeHp < 0) {
          UIText("${damageHp}").constrain {
            x = (line.getWidth() + 4.0f).pixels
            color = Color.RED.toConstraint()
          } childOf line
        }
      }
      cachedPlayer[name] = Data(name, hp, line)
    }
    sortedCompoment = cachedPlayer.toList().sortedBy { it.second.health.percent }.toMap()
  }

  fun draw (matrix: UMatrixStack) { // ChatScreen
    if (Config.healthDrawEnabled == false) return
    val world = BaseMod.mc.world
    if (world == null || world.players == null) return
    if (BaseMod.mc.currentScreen != null && BaseMod.mc.currentScreen !is ChatScreen) return
    if (sortedCompoment.isNullOrEmpty() || (world.time % Config.healthUpdateTicks).toInt() == 0) {
      container.clearChildren()
      updatePlayers(world)
      sortedCompoment.forEach { container.addChild(it.value.text) }
    }
    if (sortedCompoment.isNotEmpty()) window.draw(matrix)
  }
}