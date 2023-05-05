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
    val main: UIComponent,
    val name: UIComponent,
    val health: UIComponent,
    val absorption: UIComponent,
    val damage: UIComponent
  )

  data class PlayerHPStates (
    val name: State<String>
    val health: State<String>
    val absorption: State<String>
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
  val alignState: State<Number> = BasicState(Config.healthDrawAlign)
  val alignXConstraint: MappedStateDelegator<State<Number>, XConstraint> = map(::alignState) { 
    when (it.get()) {
      0 -> 0.pixels() // left
      1 -> CenterConstraint() // middle
      2 -> 0.pixels(true) // right
      else -> 0.pixels() // left
    }
  }
  val alignBoolean: MappedStateDelegator<State<Number>, Boolean> = map(::alignState) {
    when (it.get()) {
      2 -> true // right
      else -> false // left & center
    }
  }
  val xSpacing: State<Float> = BasicState(1f)
  val xSiblingConstraint: MappedStateDelegator<State<Float>, SiblingConstraint> = map(::xSpacing) {
    SiblingConstraint(it.get())
  }
  val xSiblingAlignConstraint: MappedStateDelegator<State<Float>, SiblingConstraint> = map(::xSpacing) {
    SiblingConstraint(it.get(), alignBoolean.state.get())
  }

  val window by Window(ElementaVersion.V2, 60)
  val container = UIContainer().constrain {
    x = xPos.pixels
    y = yPos.pixels
    width = ChildBasedMaxSizeConstraint()
    height = ChildBasedSizeConstraint()
  } childOf window effect OutlineEffect(Color.RED, 2f, true)

  fun updatePlayers(world: ClientWorld) {
    val worldPlayers = world.players
    if (worldPlayers.isNullOrEmpty()) return

    val previousPlayerMap = playerMap.toMap()
    for (player in worldPlayers) {
      val name = UMessage(UTextComponent(player.name)).unformattedText
      if (name.isNullOrBlank()) continue
      val hp = Health.getHealthProperties(player)

      if (playerMap[name] == null) {
        val playerContainer = UIContainer().constrain {
          x = alignXConstraint.state.get()
          y = SiblingConstraint(0f)
          height = ChildBasedMaxSizeConstraint()
          width = ChildBasedSizeConstraint()
          textScale = textSize.pixels 
        }
        // create states
        val nameS = BasicState("${name}:")
        val healthS = BasicState("0/0 ❤")
        val absorptionS = BasicState("")
        val damageS = BasicState("")
        // create constraints
        val nameC = UIText().bindText(nameS).constrain {
          x = xSiblingConstraint.state.get()
        }
        val healthC = UIText().bindText(healthS).constrain {
          x = xSiblingConstraint.state.get()
        }
        val absorptionC = UIText().bindText(absorptionS).constrain {
          x = xSiblingConstraint.state.get()
        }
        val damageC = UIText().bindText(damageS).constrain {
          x = xSiblingAlignConstraint.state.get()
        }
        playerContainer.addChildren(nameC, healthC, absorptionC, damageC)
        val compoments = PlayerHPCompoments(playerContainer, nameC, healthC, absorptionC, damageC)
        val states = PlayerHPStates(nameS, healthS, absorptionS, damageS)
        playerMap[name] = PlayerHP(name, hp, compoments, states, PlayerHPTicks())
        playerMap[name]!!.components.absorption.hide(true)
        playerMap[name]!!.components.damage.hide(true)
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
        playerMap[name]!!.components.absorption.unhide(true)
        val abHp = DEC.format(hp.absorption)
        playerMap[name]!!.states.absorption.set("+${abHp}")
        playerMap[name]!!.components.absorption.setColor(Color.ORANGE.toConstraint())
      } else {
        playerMap[name]!!.states.absorption.set("")
        playerMap[name]!!.components.absorption.hide(true)
      }

      // damage/heal change
      if (Config.healthDrawDamageEnabled == true) {
        if (playerMap[name]!!.health.current != hp.current) {
          val changeHp = (hp.current + hp.absorption) - (playerMap[name]!!.health.current + playerMap[name]!!.health.absorption)
          val damageHp = DEC.format(changeHp)
          if (changeHp > 0.1) {
            playerMap[name]!!.components.damage.unhide(true)
            playerMap[name]!!.states.damage.set("+${damageHp}")
            playerMap[name]!!.components.damage.setColor(Config.healthGoodColor)
            playerMap[name]!!.tick.damage = -1
          } else if (changeHp < -0.1) {
            playerMap[name]!!.components.damage.unhide(true)
            playerMap[name]!!.states.damage.set("${damageHp}") // no negative sign needed
            playerMap[name]!!.components.damage.setColor(Config.healthCriticalColor)
            playerMap[name]!!.tick.damage = -1
          }
        } else if (playerMap[name]!!.tick.damage > Config.healthDrawDamageDelay) {
          playerMap[name]!!.states.damage.set("")
          playerMap[name]!!.components.damage.hide(true)
        } else {
          playerMap[name]!!.tick.damage++
        }
      }

      playerMap[name]!!.health = hp // this must be after damage/heal change
    }
    // the purge!
    for (player in playerMap) {
      if (previousPlayerMap.isNotEmpty() && previousPlayerMap.containsKey(player.key)) {
        val prevPlayer = previousPlayerMap[player.key]!!
        if (prevPlayer.tick.main == player.value.tick.main) { // if tick is the same - then player isn't in range
          playerMap.remove(player.key)
          container.removeChild(prevPlayer.components.main)
        }
        //  ! This might be necessary for compoments to update 
        // else { // otherwise player exists
        //   container.replaceChild(player.value.components.main, prevPlayer.components.main)
        // }
      } else {
        container.addChild(player.value.components.main)
      }
    }
  }

  var updating = false
  fun onWorldTick (world: ClientWorld) {
    if (Config.healthDrawEnabled == false) return
    if ((world.time % Config.healthUpdateTicks).toInt() == 0) {
      updating = true
      updatePlayers(world)
      updating = false
    }
  }

  fun draw (matrix: UMatrixStack) { // ChatScreen
    if (Config.healthDrawEnabled == false) return
    val world = USBPlus.mc.world
    if (world == null) return
    if (USBPlus.mc.currentScreen != null && USBPlus.mc.currentScreen !is ChatScreen) return
    if (updating == false) window.draw(matrix)
  }
}