package net.usbwire.usbplus.features

import java.awt.Color
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.effect.StatusEffects
import net.usbwire.usbplus.config.Config

object Health {
  data class HealthData(val current: Float, val max: Float, val absorption: Float, val percent: Float, val color: Color)

  fun renderHitbox(matrix: MatrixStack, vertex: VertexConsumer, entity: Entity): Boolean {
    if (Config.healthEnabled == false) return false
    if (entity !is PlayerEntity) return Config.healthHitboxCancel
    val color = getHealthProperties(entity).color
    if (color == Color.WHITE) return Config.healthHitboxCancel
    val box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ())
    val red = color.red / 255.0F
    val green = color.green / 255.0F
    val blue = color.blue / 255.0F
    val alpha = color.alpha / 255.0F
    WorldRenderer.drawBox(matrix, vertex, box, red, green, blue, alpha)
    return true
  }

  fun getHealthProperties(entity: PlayerEntity): HealthData {
    val current = entity.health
    val max = entity.maxHealth
    val absorption = entity.absorptionAmount // not factored into health equation?
    val percent = current / max
    var color: Color
    if (Config.healthHurtEnabled == true && entity.hurtTime != 0) { // hurt damage visible
      color = hasBadEffect(entity) ?: Config.healthHurtColor
    } else {
      color = getHealthColor(percent)
    }
    return HealthData(current, max, absorption, percent, color)
  }

  fun getHealthColor(percent: Float): Color {
    if (percent >= Config.healthGoodPercent) {
      return Config.healthBaseColor
    } else if (percent >= Config.healthLowPercent && percent <= Config.healthGoodPercent) {
      return Config.healthGoodColor
    } else if (percent >= Config.healthCriticalPercent && percent <= Config.healthLowPercent) {
      return Config.healthLowColor
    } else if (percent <= Config.healthCriticalPercent) { // assuming its red
      return Config.healthCriticalColor
    }
    return Config.healthBaseColor // fallback if something went wrong!
  }

  // https://github.com/TeamMonumenta/monumenta-plugins-public/blob/f4891b2ffd0c238a40125277cb8240dbad96f641/plugins/paper/src/main/java/com/playmonumenta/plugins/utils/PotionUtils.java#L279
  // Effects that we can actually clear
  final val badEffects = setOf(
    // StatusEffects.BLINDNESS,
		StatusEffects.POISON,
		// StatusEffects.NAUSEA,
		// StatusEffects.SLOWNESS,
		// StatusEffects.MINING_FATIGUE,
		StatusEffects.WITHER,
		// StatusEffects.WEAKNESS,
		StatusEffects.INSTANT_DAMAGE,
		// StatusEffects.HUNGER,
		// StatusEffects.LEVITATION,
		// StatusEffects.UNLUCK
  )
  fun hasBadEffect(entity: PlayerEntity): Color? {
    if (Config.healthEffectEnabled == false) return null
    // Player on fire?
    if (entity.isOnFire == true && entity.isFireImmune == false) return Config.healthEffectColor
    // Effects that deal damage
    for (type in badEffects) {
      if (entity.hasStatusEffect(type) == true) {
        val effect = entity.getStatusEffect(type)
        if (effect != null && effect.getDuration() < 36000 /* 30 min */) {
          return Config.healthEffectColor
        }
      }
    }
    return null
  }
}
