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

object HealthHud {
  val DEC = DecimalFormat("0.0")
  data class Data (
    val name: String,
    val health: Health.HealthData,
    val text: UIComponent
  )

  val xPos = BasicState(Config.healthDrawX)
  val yPos = BasicState(Config.healthDrawY)

  // TODO: Fix x and y not doing something properly
  val window by Window(ElementaVersion.V2)
  val container = UIContainer().constrain {
    x = xPos.get().percent
    y = yPos.get().percent
  } childOf window

  val cachedPlayer: MutableMap<String, Data> = mutableMapOf()
  var sortedCompoment: Map<String, Data> = mutableMapOf()

  fun updatePlayers(world: ClientWorld) {
    val previousPlayer = cachedPlayer
    cachedPlayer.clear()
    for (entity in world.players) {
      if (entity == BaseMod.mc.player) continue // exclude yourself
      // TODO: have bad effects be different colors (wither, poison, etc, )
      // can you even get effects of other players on Monumenta??
      // is that even legal?
      val hp = Health.getHealthProperties(entity)
      if (hp.color == Color.WHITE) continue
      val name = UMessage(UTextComponent(entity.name)).unformattedText
      val maxHp = DEC.format(hp.max)
      val currentHp = DEC.format(hp.current)
      var message = "${name}: ${currentHp}/${maxHp}"
      val line = UIText(message).constrain {
        y = SiblingConstraint(padding = 2f)
       }
       line.setColor(hp.color)
       // Absorption?
      if (hp.absorption > 0) {
        val abHp = DEC.format(hp.absorption)
        val abLine = UIText("+${abHp}").constrain {
          x = SiblingConstraint(padding = 2f)
        }
        abLine.setColor(Color.ORANGE) // hardcoded
        line.addChild(abLine)
      }
      // Damage Change
      if (previousPlayer[name] != null) {
        val prevHp = previousPlayer[name]!!.health
        val changeHp = (hp.current + hp.absorption) - (prevHp.current + prevHp.absorption)
        val damageHp = DEC.format(changeHp)
        if (changeHp > 0) {
          val changeLine = UIText("+${damageHp}").constrain {
            x = SiblingConstraint(padding = 2f)
          }
          changeLine.setColor(Color.GREEN)
          line.addChild(changeLine)
        } else if (changeHp < 0) {
          val changeLine = UIText("-${damageHp}").constrain {
            x = SiblingConstraint(padding = 2f)
          }
          changeLine.setColor(Color.RED)
          line.addChild(changeLine)
        }
      }
      cachedPlayer[name] = Data(name, hp, line)
    }
    sortedCompoment = cachedPlayer.toList().sortedBy { it.second.health.percent }.toMap()
  }

  fun draw (matrix: UMatrixStack) { // ChatScreen
    if (!Config.healthDrawEnabled) return
    val world = BaseMod.mc.world
    if (world == null || world.players == null) return
    if (BaseMod.mc.currentScreen != null && BaseMod.mc.currentScreen !is ChatScreen) return
    if (sortedCompoment.isNullOrEmpty() || (world.time % Config.healthUpdateTicks).toInt() == 0) {
      updatePlayers(world)
      container.clearChildren()
      sortedCompoment.forEach { container.addChild(it.value.text) }
    }
    if (sortedCompoment.isNotEmpty()) window.draw(matrix)
  }
}