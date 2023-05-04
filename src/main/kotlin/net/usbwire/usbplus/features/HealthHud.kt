package net.usbwire.usbplus.features

import java.awt.Color
import java.text.DecimalFormat

import gg.essential.elementa.*
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import gg.essential.universal.UResolution
import gg.essential.api.utils.GuiUtil
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.world.ClientWorld

import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.features.Health
import net.usbwire.usbplus.util.Util

val DEC = DecimalFormat("0.0")
object HealthHud {
    data class PlayerHP (
    val name: String,
    val health: Health.HealthData,
    val components: PlayerHPCompoments,
    var tick: Int = 0
  )

  data class PlayerHPCompoments (
    val main: UIComponent,
    val name: UIComponent,
    val health: UIComponent,
    val absorption: UIComponent?,
    val damage: UIComponent?
  )


  val cachedPlayer: MutableMap<String, PlayerHP> = mutableMapOf()
  var sortedCompoment: Map<String, PlayerHP> = mutableMapOf()

  val xPos: State<Number> = BasicState(Config.healthDrawX)
  val yPos: State<Number> = BasicState(Config.healthDrawY)

  val window by Window(ElementaVersion.V2, 60) effect OutlineEffect(Color.BLUE, 2f, true)
  val container = UIContainer().constrain {
    x = xPos.pixels
    y = yPos.pixels
    width = ChildBasedMaxSizeConstraint()
    height = ChildBasedSizeConstraint()
  } childOf window effect OutlineEffect(Color.RED, 2f, true)

  fun updatePlayers(world: ClientWorld) {
    val previousPlayer = cachedPlayer.toMap()
    cachedPlayer.clear()
    for (entity in world.players) {
      // if (entity == USBPlus.mc.player) continue // exclude yourself
      // TODO: have bad effects be different colors (wither, poison, etc, )
      // can you even get effects of other players on Monumenta??
      // is that even legal?
      val hp = Health.getHealthProperties(entity)
      // if (hp.color == Color.WHITE) continue
      // create player container
      val playerContainer = UIContainer().constrain {
        x = when (Config.healthDrawAlign) { // alignment
          0 -> 0.pixels() // left
          1 -> CenterConstraint() // middle
          2 -> 0.pixels(true) // right
          else -> 0.pixels() // left
        }
        y = SiblingConstraint(0f)
        height = ChildBasedMaxSizeConstraint()
        width = ChildBasedSizeConstraint()
      }

      // create name contianer
      val name = UMessage(UTextComponent(entity.name)).unformattedText
      var nameMessage = "${name}:"
      val nameCompoment = UIText(nameMessage).constrain {
        x = SiblingConstraint(1f)
        color = hp.color.toConstraint()
      }

      val maxHp = DEC.format(hp.max)
      val currentHp = DEC.format(hp.current)
      // ❤
      val healthMessage = "${currentHp}❤/${maxHp}❤"
      val healthCompoment = UIText(healthMessage).constrain {
        x = SiblingConstraint(1f)
        color = hp.color.toConstraint()
       }

      var absorptionCompoment: UIComponent? = null
       // Absorption?
      if (hp.absorption > 0) {
        val abHp = DEC.format(hp.absorption)
        absorptionCompoment = UIText("+${abHp}❤").constrain {
          x = SiblingConstraint(1f)
          color = Color.ORANGE.toConstraint()
        }
      }
      // Health Change
      var damageCompoment: UIComponent? = null
      var tick = 0
      if (previousPlayer[name] != null && Config.healthDrawDamageEnabled) {
        // tick
        tick = previousPlayer[name]!!.tick + 1
        val tickDelay = Config.healthDrawDamageDelay + 1

        // previous health check
        val prevHp = previousPlayer[name]!!.health
        val changeHp = (hp.current + hp.absorption) - (prevHp.current + prevHp.absorption)
        val damageHp = DEC.format(changeHp)
        if (changeHp > 0.1) {
          damageCompoment = UIText("+${damageHp}❤").constrain {
            x = SiblingConstraint(1f)
            color = Color.GREEN.toConstraint()
          }
          tick = 0
        } else if (changeHp < -0.1) {
          damageCompoment = UIText("${damageHp}❤").constrain {
            x = SiblingConstraint(1f)
            color = Color.RED.toConstraint()
          }
          tick = 0
        } else if (previousPlayer[name]!!.components.damage != null && tick <= tickDelay) {
          damageCompoment = previousPlayer[name]!!.components.damage
        } else if (tick > tickDelay) {
          tick = 0
          damageCompoment = null
        }
      }

      playerContainer.addChild(nameCompoment)
      playerContainer.addChild(healthCompoment)
      if (absorptionCompoment != null) playerContainer.addChild(absorptionCompoment)
      if (damageCompoment != null) playerContainer.addChild(damageCompoment)
      val compoments = PlayerHPCompoments(playerContainer, nameCompoment, healthCompoment, absorptionCompoment, damageCompoment)

      cachedPlayer[name] = PlayerHP(name, hp, compoments, tick)
    }
    sortedCompoment = cachedPlayer.toList().sortedBy { it.second.health.percent }.toMap()
  }

  fun draw (matrix: UMatrixStack) { // ChatScreen
    if (Config.healthDrawEnabled == false) return
    val world = USBPlus.mc.world
    if (world == null || world.players == null) return
    if (USBPlus.mc.currentScreen != null && USBPlus.mc.currentScreen !is ChatScreen) return
    if (sortedCompoment.isNullOrEmpty() || (world.time % Config.healthUpdateTicks).toInt() == 0) {
      container.clearChildren()
      updatePlayers(world)
      sortedCompoment.forEach { container.addChild(it.value.components.main) }
    }
    if (sortedCompoment.isNotEmpty()) window.draw(matrix)
  }
}