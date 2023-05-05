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
    var health: Health.HealthData,
    val components: PlayerHPCompoments,
    val states: PlayerHPStates,
    var tick: PlayerHPTicks
  )

  data class PlayerHPCompoments (
    val root: UIComponent,
    val name: UIComponent,
    val health: UIComponent,
    val absorption: UIComponent,
    val damage: UIComponent
  )

  data class PlayerHPStates (
    val name: State<String>,
    val health: State<String>,
    val absorption: State<String>,
    val damage: State<String>
  )

  data class PlayerHPTicks (
    var main: Int = -1,
    var damage: Int = -1
  )

  val playerMap: MutableMap<String, PlayerHP> = mutableMapOf()
  var sortedPlayerMap: Map<String, PlayerHP> = emptyMap()

  val xPos: State<Number> = BasicState(Config.healthDrawX)
  val yPos: State<Number> = BasicState(Config.healthDrawY)
  val textSize: State<Number> = BasicState(Config.healthDrawScale)
  val alignPos: State<Number> = BasicState(Config.healthDrawAlign)

  val window by Window(ElementaVersion.V2, 60)
  val container = UIContainer().constrain {
    x = xPos.pixels
    y = yPos.pixels
    width = ChildBasedMaxSizeConstraint()
    height = ChildBasedSizeConstraint()
  } childOf window

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

        // create containers
        val rootC = UIContainer().constrain {
          x = returnAlignConstraint()
          y = SiblingConstraint(0f)
          height = ChildBasedMaxSizeConstraint()
          width = ChildBasedSizeConstraint()
          textScale = textSize.pixels
        }

        // create children
        val nameC = UIText().bindText(nameS).constrain {
          x = SiblingConstraint(2f)
        }
        val healthC = UIText().bindText(healthS).constrain {
          x = SiblingConstraint(2f)
        }
        val absorptionC = UIText().bindText(absorptionS).constrain {
          x = SiblingConstraint(2f)
        }
        val damageC = UIText().bindText(damageS).constrain {
          x = SiblingConstraint(2f)
        }

        rootC.addChild(nameC)
        rootC.addChild(healthC)
        rootC.addChild(absorptionC)
        val compoments = PlayerHPCompoments(rootC, nameC, healthC, absorptionC, damageC)
        val states = PlayerHPStates(nameS, healthS, absorptionS, damageS)
        playerMap[name] = PlayerHP(name, hp, compoments, states, PlayerHPTicks())
      }

      // increase tick
      playerMap[name]!!.tick.main++

      // name
      // cachedPlayer[name]!!.states.name.set(name)
      playerMap[name]!!.components.name.setColor(hp.color.toConstraint())

      // health
      val maxHp = DEC.format(hp.max)
      val currentHp = DEC.format(hp.current)
      playerMap[name]!!.states.health.set("${currentHp}/${maxHp} ❤")
      playerMap[name]!!.components.health.setColor(hp.color.toConstraint())

      // absorption
      if (hp.absorption > 0) {
        val abHp = DEC.format(hp.absorption)
        playerMap[name]!!.states.absorption.set("+${abHp}")
        playerMap[name]!!.components.absorption.setColor(Color.ORANGE.toConstraint())
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
            playerMap[name]!!.components.damage.setColor(Config.healthGoodColor)
            playerMap[name]!!.tick.damage = -1
          } else if (changeHp < -0.1) {
            playerMap[name]!!.states.damage.set("${damageHp}") // no negative sign needed
            playerMap[name]!!.components.damage.setColor(Config.healthCriticalColor)
            playerMap[name]!!.tick.damage = -1
          }
        } else if (playerMap[name]!!.tick.damage > Config.healthDrawDamageDelay) {
          playerMap[name]!!.states.damage.set("")
        } else {
          playerMap[name]!!.tick.damage++
        }
      }

      playerMap[name]!!.health = hp // this must be after damage/heal change
      currentPlayers.add(name) // required for removal
    }
    // remove out of range players players
    for (player in previousPlayerMap) {
      if (currentPlayers.contains(player.key)) continue
      playerMap.remove(player.key)
      container.removeChild(player.value.components.root)
    }

    // sort map by health
    sortedPlayerMap = playerMap.toList().sortedBy { it.second.health.percent }.toMap()
    for (player in sortedPlayerMap) {
      if (playerMap.containsKey(player.key) && player.value.components.root.hasParent == false) {
        container.addChild(player.value.components.root)
      }
    }
  }

  var configDirty = true
  fun onWorldTick (world: ClientWorld) {
    if (Config.healthDrawEnabled == false) return
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

  fun returnAlignConstraint (): XConstraint {
    return when (alignPos.get()) {
      0 -> 0.pixels() // left
      1 -> CenterConstraint() // middle
      2 -> 0.pixels(true) // right
      else -> 0.pixels() // left
    }
  }
}