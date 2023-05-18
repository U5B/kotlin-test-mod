//#if MC>=11903
//$$ import net.minecraft.registry.Registries
//#else
//#endif
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

/**
 * For when there are differences between versions
 */
object ItemHelper {

	/**
	 * returns item identifier (minecraft:item)
	 */
	fun getItemId(item: Item?): String {
		//#if MC>=11903
		//$$ return Registries.ITEM.getId(item).toString()
		//#else
		return Registry.ITEM.getId(item).toString()
		//#endif
	}
}