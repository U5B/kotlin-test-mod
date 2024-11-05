package net.usbwire.usbplus.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.client.gui.screen.Screen;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.features.Compass;
import net.usbwire.usbplus.features.Maow;
import net.usbwire.usbplus.features.Poi;
import net.usbwire.usbplus.features.health.Base;
import net.usbwire.usbplus.features.health.HUD;
import net.usbwire.usbplus.util.Util;
import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class Config extends Vigilant {
	// *POI*
	@Property(type = PropertyType.SWITCH, name = "Toggle POI",
			description = "Type /poi to get started!", category = "POI",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean poiEnabled = true;
	@Property(type = PropertyType.TEXT, name = "Internal POI URL",
			description = "Should not be changed unless you know what you are doing!", category = "POI",
			hidden = false)
	public static String poiUrl =
			"https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json"; // github url
	@Property(type = PropertyType.BUTTON, name = "Refresh POIs",
			description = "Fetches from 'Internal POI URL' for the latest data", category = "POI",
			hidden = false)
	public static void fetchPoiDataSetting() {
		Poi.fetchPoiData();
	}

	// *Compass*
	@Property(type = PropertyType.SWITCH, name = "Toggle Compass",
			description = "Trigger by left clicking with a compass.", category = "Compass",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean compassEnabled = true;
	// *Common Health*
	// Health Whitelist
	@Property(type = PropertyType.SWITCH, name = "Toggle Whitelist", description = "Toggle Whitelist",
			category = "Health General", subcategory = "General", triggerActionOnInitialization = false, hidden = false)
	public static boolean healthWhitelistEnabled = false;
	@Property(type = PropertyType.PARAGRAPH, name = "Player names separated by spaces to allow",
			description = "Player names separated by spaces to allow", category = "Health General", subcategory = "General",
			hidden = false)
	public static String healthWhitelist = "";
	@Property(type = PropertyType.SLIDER, name = "Update Rate In Ticks",
			description = "Update Rate In Ticks", category = "Health General", subcategory = "General", min = 1, max = 20,
			hidden = false)
	public static int healthUpdateTicks = 1;

	// *Health Color Toggles & Percentages*
	@Property(type = PropertyType.SWITCH, name = "Hurt Color Toggle",
			description = "Hurt Color Toggle", category = "Health Colors", subcategory = "Color",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthHurtEnabled = false;
	@Property(type = PropertyType.SWITCH, name = "Fire Color Toggle",
			description = "Fire Color Toggle", category = "Health Colors", subcategory = "Color",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthEffectEnabled = false;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Good HP percent", description = "100% HP",
			category = "Health Colors", subcategory = "Color", hidden = false)
	public static float healthGoodPercent = 9.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Low HP percent", description = "70% HP",
			category = "Health Colors", subcategory = "Color", hidden = false)
	public static float healthLowPercent = 0.7f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Critical HP percent",
			description = "40% HP", category = "Health Colors", subcategory = "Color", hidden = false)
	public static float healthCriticalPercent = 0.4f;

	// *Health Colors*
	@Property(type = PropertyType.COLOR, name = "Base HP color",
			description = "Alpha under 10 doesn't show", category = "Health Colors", subcategory = "Color", hidden = false)
	public static Color healthBaseColor = Color.LIGHT_GRAY;
	@Property(type = PropertyType.COLOR, name = "Good HP color", category = "Health Colors", subcategory = "Color",
			hidden = false)
	public static Color healthGoodColor = Color.GREEN;
	@Property(type = PropertyType.COLOR, name = "Low HP color", category = "Health Colors", subcategory = "Color",
			hidden = false)
	public static Color healthLowColor = Color.YELLOW;
	@Property(type = PropertyType.COLOR, name = "Critical HP color", category = "Health Colors", subcategory = "Color",
			hidden = false)
	public static Color healthCriticalColor = Color.RED;
	@Property(type = PropertyType.COLOR, name = "Hurt color", category = "Health Colors", subcategory = "Color",
			hidden = false)
	public static Color healthHurtColor = Color.ORANGE;
	@Property(type = PropertyType.COLOR, name = "Fire Color", category = "Health Colors", subcategory = "Color",
			hidden = false)
	public static Color healthEffectColor = Color.GRAY;

	// *BoxHealth*
	@Property(type = PropertyType.SWITCH, name = "Toggle BoxHealth", description = "Toggle BoxHealth",
			category = "Health General", subcategory = "HitBox", triggerActionOnInitialization = false, hidden = false)
	public static boolean healthEnabled = true;
	@Property(type = PropertyType.SWITCH, name = "Color Glowing Players!",
			description = "Color Glowing Players!", category = "Health General", subcategory = "HitBox",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthGlowingEnabled = true;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Alpha Percentage of Inside Box",
	description = "Set to 0 to disable.", category = "Health General", subcategory = "HitBox", hidden = false)
	public static float healthFillPercent = 0.0f;

	// *DrawHealth*
	@Property(type = PropertyType.SWITCH, name = "Toggle DrawHealth",
			description = "Toggle DrawHealth", category = "Health Draw", subcategory = "Draw",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthDrawEnabled = true;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "X Position Percent",
			description = "X Position Percent", category = "Health Draw", subcategory = "Draw", hidden = false)
	public static float healthDrawX = 0.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Y Position Percent",
			description = "Y Position Percent", category = "Health Draw", subcategory = "Draw", hidden = false)
	public static float healthDrawY = 0.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Text Alignment Percent",
			description = "Text Alignment Percent", category = "Health Draw", subcategory = "Draw", hidden = false)
	public static float healthDrawAlign = 0.0f;
	@Property(type = PropertyType.DECIMAL_SLIDER, name = "Text Scale", description = "Text Scale",
			category = "Health Draw", subcategory = "Draw", minF = 0.5f, maxF = 4.0f, increment = 1, hidden = false)
	public static float healthDrawScale = 1.0f;
	@Property(type = PropertyType.SWITCH, name = "Display Recent Damage",
			description = "Display Recent Damage", category = "Health Draw", subcategory = "Draw",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthDrawDamageEnabled = false;
	@Property(type = PropertyType.SLIDER, name = "Damage Hide Delay in Ticks",
			description = "Damage Hide Delay in Ticks", category = "Health Draw", subcategory = "Draw", min = 1, max = 60,
			hidden = false)
	public static int healthDrawDamageDelay = 10;
	@Property(type = PropertyType.SWITCH, name = "Recent Damage Alignment",
	description = "Recent Damage Alignment", category = "Health Draw", subcategory = "Draw",
	triggerActionOnInitialization = false, hidden = false)
public static boolean healthDrawAlignExtraRight = false;
	@Property(type = PropertyType.SELECTOR, name = "Sort player list by",
			description = "Sort player list by", category = "Health Draw", subcategory = "Draw",
			options = {"alphabetical", "health (low to high)", "health (high to low)", "time"}, hidden = false)
	public static int healthDrawSort = 0;

	// *Pickup*
	@Property(type = PropertyType.SWITCH, name = "Sneak to toggle between pickup states",
			description = "Sneak to toggle between pickup states", category = "Misc",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean pickupEnabled = false;
	@Property(type = PropertyType.SLIDER, name = "Sneaking delay in ticks",
			description = "Sneaking delay in ticks", category = "Misc", min = 1, max = 40, hidden = false)
	public static int pickupDelay = 20;
	@Property(type = PropertyType.SELECTOR, name = "Pickup Mode", description = "Pickup Mode",
			category = "Misc", options = {"interesting", "lore", "tiered"}, hidden = false)
	public static int pickupMode = 0;

	// *Debug*
	@Property(type = PropertyType.SWITCH, name = "Toggle Debug", description = "Toggle Debug",
			category = "_Debug", triggerActionOnInitialization = false, hidden = false)
	public static boolean debugEnabled = false;
	@Property(type = PropertyType.SWITCH, name = "Print contents of opened chests",
			description = "Print contents of opened chests", category = "_Debug",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean debugContainer = false;
	private static final String configFile = USBPlus.configPath + "/config.toml";

	// *Maow*
	@Property(type = PropertyType.SWITCH, name = "Toggle Maow", description = "Toggle Maow",
			category = "Maow", triggerActionOnInitialization = false, hidden = false)
	public static boolean maowEnabled = false;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "X Position Percent",
			description = "X Position Percent", category = "Maow", hidden = false)
	public static float maowX = 0.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Y Position Percent",
	description = "Y Position Percent", category = "Maow", hidden = false)
	public static float maowY = 0.0f;
	@Property(type = PropertyType.SLIDER, name = "Maow Image Height", description = "Maow Image Height",
			category = "Maow", min = 0, max = 2000, hidden = false)
	public static int maowHeight = 150;
	@Property(type = PropertyType.SLIDER, name = "Maow Image Width", description = "Maow Image Width",
			category = "Maow", min = 0, max = 2000, hidden = false)
	public static int maowWidth = 150;
	@Property(type = PropertyType.SLIDER, name = "Maow Image Alpha", description = "Maow Image Alpha",
			category = "Maow", min = 0, max = 255, hidden = false)
	public static int maowAlpha = 255;
	@Property(type = PropertyType.SLIDER, name = "Maow Refresh Delay in Seconds", description = "Maow Refresh Rate in Seconds",
			category = "Maow", min = 0, max = 60, hidden = false)
	public static int maowRefreshRate = 0;
	@Property(type = PropertyType.TEXT, name = "Maow Image URL", description = "Maow Image URL",
			category = "Maow", hidden = false)
	public static String maowUrl = "https://catgirl.usbwire.net";

	public Config() {
		super(new File(configFile));
		Util.createDirectory(Path.of(configFile));

		initialize();
		try {
			// ! - Make sure to update variable names and method calls down here as well
			Consumer<Object> poiEnabledChanged = any -> Poi.configChanged();
			registerListener("poiEnabled", poiEnabledChanged);
			Consumer<Object> compassEnabledChanged = any -> Compass.configChanged();
			registerListener("compassEnabled", compassEnabledChanged);

			// draw health
			Consumer<Float> healthDrawXChanged = value -> {
				HUD.xPos.set(value);
				Base.configDirty = true;
			};
			registerListener("healthDrawX", healthDrawXChanged);
			Consumer<Float> healthDrawYChanged = value -> {
				HUD.yPos.set(value);
				Base.configDirty = true;
			};
			registerListener("healthDrawY", healthDrawYChanged);
			Consumer<Float> healthDrawAlignChanged = value -> {
				HUD.alignPos.set(value);
				Base.configDirty = true;
			};
			registerListener("healthDrawAlign", healthDrawAlignChanged);
			Consumer<Float> healthDrawScaleChanged = value -> {
				HUD.textSize.set(value);
				Base.configDirty = true;
			};
			registerListener("healthDrawScale", healthDrawScaleChanged);
			Consumer<Boolean> healthDrawAlignExtraRightChanged = value -> {
				HUD.alignRightExtra.set(value);
				Base.configDirty = true;
			};
			registerListener("healthDrawAlignExtraRight", healthDrawAlignExtraRightChanged);

			// maow
			Consumer<Float> maowXChanged = value -> {
				Maow.xPos.set(value);
			};
			registerListener("maowX", maowXChanged);
			Consumer<Float> maowYChanged = value -> {
				Maow.yPos.set(value);
			};
			registerListener("maowY", maowYChanged);
			Consumer<Integer> maowHeightChanged = value -> {
				Maow.height.set((float) value);
				Maow.dirty = true;
			};
			registerListener("maowHeight", maowHeightChanged);
			Consumer<Integer> maowWidthChanged = value -> {
				Maow.width.set((float) value);
				Maow.dirty = true;
			};
			registerListener("maowWidth", maowWidthChanged);
			Consumer<Integer> maowAlphaChanged = value -> {
				Maow.color.set(new Color(255, 255, 255, value));
			};
			registerListener("maowAlpha", maowAlphaChanged);
		} catch (Exception e) {
			USBPlus.logger.error("Failed to register config listeners: ", e);
		}
	}
}
