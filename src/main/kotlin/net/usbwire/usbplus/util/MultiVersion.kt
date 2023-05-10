//#if MC>=11902
//$$ import net.minecraft.registry.Registries
//#else
import net.minecraft.util.registry.Registry
//#endif
import net.minecraft.item.Item

object ItemHelper {

  // returns item identifier (minecraft:item)
  fun getItemId (item: Item?): String {
    //#if MC>=11902
    //$$ return Registries.ITEM.getId(item).toString()
    //#else
    return Registry.ITEM.getId(item).toString()
    //#endif
  }
}