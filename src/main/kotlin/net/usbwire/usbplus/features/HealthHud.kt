package net.usbwire.usbplus.features

import java.awt.Color
import java.text.DecimalFormat

import gg.essential.elementa.*
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.*
import gg.essential.elementa.utils.*
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import gg.essential.universal.UResolution
import gg.essential.universal.UMouse
import gg.essential.universal.UGraphics
import gg.essential.api.utils.GuiUtil
import gg.essential.api.EssentialAPI
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
    var health: Health.HealthData,
    val root: UIComponent,
    val states: PlayerHPStates,
    var tick: PlayerHPTicks
  )
  data class PlayerHPStates (
    val name: State<String>,
    val health: State<String>,
    val absorption: State<String>,
    val damage: State<String>,
    val healthColor: State<Color>,
    val damageColor: State<Color>
  )
  data class PlayerHPTicks (
    var main: Int = -1,
    var damage: Int = -1
  )

  val playerMap: MutableMap<String, PlayerHP> = mutableMapOf()
  var sortedPlayerMap: Map<String, PlayerHP> = emptyMap()

  val xPos: State<Float> = BasicState(Config.healthDrawX)
  val yPos: State<Float> = BasicState(Config.healthDrawY)
  val textSize: State<Number> = BasicState(Config.healthDrawScale)

  val alignXConstraints: List<XConstraint> = listOf(0.pixels, CenterConstraint(), 0.pixels(true))
  val alignXSiblings: List<SiblingConstraint> = listOf(SiblingConstraint(2f), SiblingConstraint(2f), SiblingConstraint(2f, true))
  val invertedXSiblings: List<SiblingConstraint> = listOf(SiblingConstraint(2f, true), SiblingConstraint(2f), SiblingConstraint(2f))
  val alignRight: State<Boolean> = BasicState(Config.healthDrawAlignRight)
  val alignRightExtra: State<Boolean> = BasicState(Config.healthDrawAlignExtraRight)

  val window by Window(ElementaVersion.V2, 60)
  val container = UIContainer().constrain { // hardcoded for now!
    x = CenterConstraint()
    y = 0.pixels
    width = ChildBasedMaxSizeConstraint()
    height = ChildBasedSizeConstraint()
  }

  fun updatePlayers(world: ClientWorld) {
    val worldPlayers = world.players
    if (worldPlayers.isNullOrEmpty()) return

    val previousPlayerMap = playerMap.toMap()
    val currentPlayers: MutableList<String> = mutableListOf()
    for (player in worldPlayers) {
      val name = UMessage(UTextComponent(player.name)).unformattedText
      val hp = Health.getHealthProperties(player)

      if (playerMap[name] == null) {
        // create states
        val nameS = BasicState("${name}:")
        val healthS = BasicState("0/0 ❤")
        val absorptionS = BasicState("")
        val damageS = BasicState("")
        val hpColorS = BasicState(hp.color)
        val damageColorS = BasicState(Color.WHITE)

        // root container (contains everything)
        val rootC = UIContainer().constrain {
          x = 0.pixels(alignRight.get())
          y = SiblingConstraint(0f)
          width = ChildBasedSizeConstraint()
          height = ChildBasedMaxSizeConstraint()
        }

        val nameC = UIText().bindText(nameS).constrain {
          x = SiblingConstraint("l".width(textSize.get().toFloat()))
          color = hpColorS.constraint
          textScale = textSize.pixels
        }
        val healthC = UIText().bindText(healthS).constrain {
          x = SiblingConstraint("l".width(textSize.get().toFloat()))
          color = hpColorS.constraint
          textScale = textSize.pixels
        }
        val absorptionC = UIText().bindText(absorptionS).constrain {
          x = SiblingConstraint("l".width(textSize.get().toFloat()))
          color = Color.ORANGE.toConstraint()
          textScale = textSize.pixels
        }

        val damageC = UIText().bindText(damageS).constrain {
          x = SiblingConstraint("l".width(textSize.get().toFloat()))
          color = damageColorS.constraint
          textScale = textSize.pixels
        }

        if (alignRightExtra.get()) { // reverse order
          rootC.addChild(damageC)
          rootC.addChild(nameC)
          rootC.addChild(healthC)
          rootC.addChild(absorptionC)
        }
        else {
          rootC.addChild(nameC)
          rootC.addChild(healthC)
          rootC.addChild(absorptionC)
          rootC.addChild(damageC)
        }
        val states = PlayerHPStates(nameS, healthS, absorptionS, damageS, hpColorS, damageColorS)
        playerMap[name] = PlayerHP(name, hp, rootC, states, PlayerHPTicks())
      }

      // increase tick
      playerMap[name]!!.tick.main++

      // set color
      playerMap[name]!!.states.healthColor.set(hp.color)

      // health
      val maxHp = DEC.format(hp.max)
      val currentHp = DEC.format(hp.current)
      playerMap[name]!!.states.health.set("${currentHp}/${maxHp} ❤")

      // absorption
      if (hp.absorption > 0) {
        val abHp = DEC.format(hp.absorption)
        playerMap[name]!!.states.absorption.set("+${abHp}")
      } else {
        playerMap[name]!!.states.absorption.set("")
      }

      // damage/heal change
      if (Config.healthDrawDamageEnabled == true) {
        if (playerMap[name]!!.health.current != hp.current) {
          val changeHp = (hp.current + hp.absorption) - (playerMap[name]!!.health.current + playerMap[name]!!.health.absorption)
          val damageHp = DEC.format(changeHp)
          if (changeHp > 0.1) {
            playerMap[name]!!.states.damage.set("+${damageHp}")
            playerMap[name]!!.states.damageColor.set(Config.healthGoodColor)
            playerMap[name]!!.tick.damage = -1
          } else if (changeHp < -0.1) {
            playerMap[name]!!.states.damage.set("${damageHp}") // no negative sign needed
            playerMap[name]!!.states.damageColor.set(Config.healthCriticalColor)
            playerMap[name]!!.tick.damage = -1
          }
        } else if (playerMap[name]!!.tick.damage > Config.healthDrawDamageDelay) {
          playerMap[name]!!.states.damage.set("")
        } else {
          playerMap[name]!!.tick.damage++
        }
      } else {
        playerMap[name]!!.states.damage.set("")
      }

      playerMap[name]!!.health = hp // this must be after damage/heal change
      currentPlayers.add(name) // required for removal
    }
    // remove out of range players players
    for (player in previousPlayerMap) {
      if (currentPlayers.contains(player.key)) continue
      playerMap.remove(player.key)
      container.removeChild(player.value.root)
    }

    // sort map by health
    sortedPlayerMap = playerMap.toList().sortedBy { it.second.health.percent }.toMap()
    for (player in sortedPlayerMap) {
      if (playerMap.containsKey(player.key) && player.value.root.hasParent == false) {
        container.addChild(player.value.root)
      }
    }
  }

  var configDirty = true
  fun onWorldTick (world: ClientWorld) {
    if (Config.healthDrawEnabled == false) {
      configDirty = true
      return
    }
    if (configDirty == true) {
      playerMap.clear()
      container.clearChildren()
      configDirty = false
    }
    if ((world.time % Config.healthUpdateTicks).toInt() == 0) {
      updatePlayers(world)
    }
  }

  fun draw (matrix: UMatrixStack) { // ChatScreen
    if (Config.healthDrawEnabled == false) return
    val world = USBPlus.mc.world
    if (world == null || world.players == null) return
    if (USBPlus.mc.currentScreen != null && USBPlus.mc.currentScreen !is ChatScreen) return
    if (playerMap.isNotEmpty()) window.draw(matrix)
  }
}