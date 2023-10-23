
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import java.awt.Color

import net.usbwire.usbplus.interfaces.EntityMixinInterface
import net.usbwire.usbplus.USBPlus

/**
 * For when there are differences between versions
 * Inspired by JsMacros
 */
object ItemHelper {

	/**
	 * returns item identifier (minecraft:item)
	 */
	fun getItemId(item: Item?): String {
		return Registries.ITEM.getId(item).toString()
	}
}

object EntityHelper {
	fun getGlowingColor(entity: Entity): Int {
		return entity.teamColorValue
	}

	fun setGlowingColor(entity: Entity, color: Color) {
		val special = entity as EntityMixinInterface
		val intColor = color.rgb
		if (entity.teamColorValue == intColor) return;
		special.usbplus_setGlowingColor(intColor)
	}

	fun resetGlowingColor(entity: Entity) {
		val special = entity as EntityMixinInterface
		special.usbplus_resetColor()
	}

	fun setGlowing(entity: Entity, value: Boolean) {
		val special = entity as EntityMixinInterface
		special.usbplus_setForceGlowing(if (value) 2 else 0)
	}

	fun isGlowingForced(entity: Entity): Boolean {
		val special = entity as EntityMixinInterface
		if (special.usbplus_getForceGlowing() == 1) return false
		return true
	}

	fun resetGlowing(entity: Entity) {
		val special = entity as EntityMixinInterface
		special.usbplus_setForceGlowing(1)
	}
}