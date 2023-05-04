package net.usbwire.usbplus.commands

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.Greedy
import net.usbwire.usbplus.USBPlus
import net.usbwire.usbplus.features.Poi
import net.usbwire.usbplus.commands.parsers.PoiName
import net.usbwire.usbplus.config.Config
import net.usbwire.usbplus.util.Util

object PoiCommand : Command("poi", false) {
  @DefaultHandler
  fun handle(@Greedy poi_name: PoiName?) {
    if (Config.poiEnabled == false) return
    if (poi_name == null) return
    val poiString = poi_name.name
    var string = Util.trimString(poiString)
    if (string.length <= 3) {
      Util.chat("'$poiString': Too short!")
      return
    }
    val validPoi = Poi.searchPoi(poiString)
    if (validPoi == null) {
      Util.chat("'$poiString': No POI found!")
      return
    }
    for (poi in validPoi) {
      Poi.responsePoi(poiString, poi)
    }
  }
}
