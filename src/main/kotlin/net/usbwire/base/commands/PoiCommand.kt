package net.usbwire.base.commands

import gg.essential.api.commands.Command
import gg.essential.api.commands.Options
import gg.essential.api.commands.Greedy
import gg.essential.api.commands.DefaultHandler

import net.usbwire.base.BaseMod
import net.usbwire.base.util.Util
import net.usbwire.base.features.Poi
import net.usbwire.base.commands.parsers.PoiName
import net.usbwire.base.config.VigilanceConfig

object PoiCommand : Command("poi", false) {
  @DefaultHandler
  fun handle(@Greedy poi_name: PoiName?) {
    if (VigilanceConfig.poiEnabled == false) return
    if (poi_name == null) return
    val poiString = poi_name.name
    var string = Util.trimString(poiString)
    if (string.length <= 3) {
      Util.chat("'$poiString': Too short!")
      return
    }
    val validPoi = BaseMod.poi.searchPoi(poiString)
    if (validPoi == null) {
      Util.chat("'$poiString': No POI found!")
    } else {
      for (poi in validPoi) {
        BaseMod.poi.responsePoi(poiString, poi)
      }
    }
  }
}