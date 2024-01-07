package net.usbwire.usbplus.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.client.gui.screen.Screen;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.features.Compass;
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
			category = "Health General", triggerActionOnInitialization = false, hidden = false)
	public static boolean healthWhitelistEnabled = false;
	@Property(type = PropertyType.PARAGRAPH, name = "Player names separated by spaces to allow",
			description = "Player names separated by spaces to allow", category = "Health General",
			hidden = false)
	public static String healthWhitelist = "";
	@Property(type = PropertyType.SLIDER, name = "Update Rate In Ticks",
			description = "Update Rate In Ticks", category = "Health General", min = 1, max = 20,
			hidden = false)
	public static int healthUpdateTicks = 1;
	// *Health Color Toggles & Percentages*
	@Property(type = PropertyType.SWITCH, name = "Hurt Color Toggle",
			description = "Hurt Color Toggle", category = "Health Colors",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthHurtEnabled = false;
	@Property(type = PropertyType.SWITCH, name = "Fire Color Toggle",
			description = "Fire Color Toggle", category = "Health Colors",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthEffectEnabled = false;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Good HP percent", description = "100% HP",
			category = "Health Colors", hidden = false)
	public static float healthGoodPercent = 9.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Low HP percent", description = "70% HP",
			category = "Health Colors", hidden = false)
	public static float healthLowPercent = 0.7f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Critical HP percent",
			description = "40% HP", category = "Health Colors", hidden = false)
	public static float healthCriticalPercent = 0.4f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Alpha Percentage of Inside Box",
			description = "Set to 0 to disable.", category = "Health General", hidden = false)
	public static float healthFillPercent = 0.0f;
	// *Health Colors*
	@Property(type = PropertyType.COLOR, name = "Base HP color",
			description = "Alpha under 10 doesn't show", category = "Health Colors", hidden = false)
	public static Color healthBaseColor = Color.LIGHT_GRAY;
	@Property(type = PropertyType.COLOR, name = "Good HP color", category = "Health Colors",
			hidden = false)
	public static Color healthGoodColor = Color.GREEN;
	@Property(type = PropertyType.COLOR, name = "Low HP color", category = "Health Colors",
			hidden = false)
	public static Color healthLowColor = Color.YELLOW;
	@Property(type = PropertyType.COLOR, name = "Critical HP color", category = "Health Colors",
			hidden = false)
	public static Color healthCriticalColor = Color.RED;
	@Property(type = PropertyType.COLOR, name = "Hurt color", category = "Health Colors",
			hidden = false)
	public static Color healthHurtColor = Color.ORANGE;
	@Property(type = PropertyType.COLOR, name = "Fire Color", category = "Health Colors",
			hidden = false)
	public static Color healthEffectColor = Color.GRAY;
	// *BoxHealth*
	@Property(type = PropertyType.SWITCH, name = "Toggle BoxHealth", description = "Toggle BoxHealth",
			category = "Health General", triggerActionOnInitialization = false, hidden = false)
	public static boolean healthEnabled = true;
	@Property(type = PropertyType.SWITCH, name = "Color Glowing Players!",
			description = "Color Glowing Players!", category = "Health General",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthGlowingEnabled = true;
	// *DrawHealth*
	@Property(type = PropertyType.SWITCH, name = "Toggle DrawHealth",
			description = "Toggle DrawHealth", category = "Health Draw",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthDrawEnabled = true;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "X Position Percent",
			description = "X Position Percent", category = "Health Draw", hidden = false)
	public static float healthDrawX = 0.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Y Position Percent",
			description = "Y Position Percent", category = "Health Draw", hidden = false)
	public static float healthDrawY = 0.0f;
	@Property(type = PropertyType.PERCENT_SLIDER, name = "Text Alignment Percent",
			description = "Text Alignment Percent", category = "Health Draw", hidden = false)
	public static float healthDrawAlign = 0.0f;
	@Property(type = PropertyType.SWITCH, name = "Recent Damage Alignment",
			description = "Recent Damage Alignment", category = "Health Draw",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthDrawAlignExtraRight = false;
	@Property(type = PropertyType.DECIMAL_SLIDER, name = "Text Scale", description = "Text Scale",
			category = "Health Draw", minF = 0.5f, maxF = 4.0f, increment = 1, hidden = false)
	public static float healthDrawScale = 1.0f;
	@Property(type = PropertyType.SWITCH, name = "Display Recent Damage",
			description = "Display Recent Damage", category = "Health Draw",
			triggerActionOnInitialization = false, hidden = false)
	public static boolean healthDrawDamageEnabled = false;
	@Property(type = PropertyType.SLIDER, name = "Damage Hide Delay in Ticks",
			description = "Damage Hide Delay in Ticks", category = "Health Draw", min = 1, max = 60,
			hidden = false)
	public static int healthDrawDamageDelay = 10;
	@Property(type = PropertyType.SELECTOR, name = "Sort player list by",
			description = "Sort player list by", category = "Health Draw",
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

	public Config() {
		super(new File(configFile));
		Util.createDirectory(Path.of(configFile));

		initialize();
		try {
			Consumer<Object> poiConfigChanged = any -> Poi.configChanged();
			registerListener("Toggle POI", poiConfigChanged);
			Consumer<Object> fetchPoiDataConsumer = any -> Poi.fetchPoiData();
			registerListener("Refresh POIs", fetchPoiDataConsumer);
			Consumer<Object> compassConfigChanged = any -> Compass.configChanged();
			registerListener("Toggle Compass", compassConfigChanged);
			Consumer<Float> healthDrawXChanged = value -> {
				HUD.xPos.set(value);
				Base.configDirty = true;
			};
			registerListener("X Position Percent", healthDrawXChanged);
			Consumer<Float> healthDrawYChanged = value -> {
				HUD.yPos.set(value);
				Base.configDirty = true;
			};
			registerListener("Y Position Percent", healthDrawYChanged);
			Consumer<Float> healthDrawAlignChanged = value -> {
				HUD.alignPos.set(value);
				Base.configDirty = true;
			};
			registerListener("Text Alignment Percent", healthDrawAlignChanged);
			Consumer<Float> healthDrawScaleChanged = value -> {
				HUD.textSize.set(value);
				Base.configDirty = true;
			};
			registerListener("Text Scale", healthDrawScaleChanged);
			Consumer<Boolean> damageAlignmentConfigChanged = value -> {
				HUD.alignRightExtra.set(value);
				Base.configDirty = true;
			};
			registerListener("Recent Damage Alignment", damageAlignmentConfigChanged);
		} catch (Exception e) {
			USBPlus.logger.error("Failed to register config listeners: ", e);
		}
		/*
		category("POI", (config) -> {
		    switchField(() -> poiEnabled, "Toggle POI", "Type /poi to get started!", false, Poi::configChanged);
		    textField(() -> poiUrl, "Internal POI URL", "Should not be changed unless you know what you are doing!");
		    button("Refresh POIs", "Fetches from " + poiUrl + " for the latest data", false, Poi::fetchPoiData);
		});

		category("Compass", () -> {
		    switchField(() -> compassEnabled, "Toggle Compass", "Trigger by left clicking with a compass.", false, Compass::configChanged);
		});

		category("Health Colors", () -> {
		    subcategory("Color", () -> {
		        colorField(() -> healthBaseColor, "Base HP color", "Alpha under 10 doesn't show", true);
		        colorField(() -> healthGoodColor, "Good HP color", true);
		        percentSlider(() -> healthGoodPercent, "Good HP percent", "100% HP");
		        colorField(() -> healthLowColor, "Low HP color", true);
		        percentSlider(() -> healthLowPercent, "Low HP percent", "70% HP");
		        colorField(() -> healthCriticalColor, "Critical HP color", true);
		        percentSlider(() -> healthCriticalPercent, "Critical HP percent", "40% HP");
		        colorField(() -> healthHurtColor, "Hurt color", true);
		        switchField(() -> healthHurtEnabled, "Hurt Color Toggle");
		        colorField(() -> healthEffectColor, "Fire Color", true);
		        switchField(() -> healthEffectEnabled, "Fire Color Toggle");
		    });
		});

		category("Health Draw", () -> {
		    subcategory("Draw", () -> {
		        switchField(() -> healthDrawEnabled, "Toggle DrawHealth");
		        percentSlider(() -> healthDrawX, "X Position Percent", false, value -> {
		            HUD.xPos.set(value);
		            Base.configDirty = true;
		        });
		        percentSlider(() -> healthDrawY, "Y Position Percent", false, value -> {
		            HUD.yPos.set(value);
		            Base.configDirty = true;
		        });
		        percentSlider(() -> healthDrawAlign, "Text Alignment Percent", false, value -> {
		            HUD.alignPos.set(value);
		            Base.configDirty = true;
		        });
		        decimalSlider(() -> healthDrawScale, "Text Scale", 0.5f, 4.0f, 1, false, value -> {
		            HUD.textSize.set(value);
		            Base.configDirty = true;
		        });
		        switchField(() -> healthDrawDamageEnabled, "Display Recent Damage");
		        slider(() -> healthDrawDamageDelay, "Damage Hide Delay in Ticks", 1, 60);
		        switchField(() -> healthDrawAlignExtraRight, "Recent Damage Alignment", false, value -> {
		            HUD.alignRightExtra.set(value);
		            Base.configDirty = true;
		        });
		        selector(() -> healthDrawSort, "Sort player list by", List.of("alphabetical", "health", "time"));
		    });
		});

		category("Health General", () -> {
		    subcategory("Hitbox", () -> {
		        switchField(() -> healthEnabled, "Toggle BoxHealth");
		        switchField(() -> healthGlowingEnabled, "Color Glowing Players!");
		        percentSlider(() -> healthFillPercent, "Alpha Percentage of Inside Box", "Set to 0 to disable.");
		    });
		    subcategory("General", () -> {
		        slider(() -> healthUpdateTicks, "Update Rate In Ticks", 1, 20);
		        switchField(() -> healthWhitelistEnabled, "Toggle Whitelist");
		        paragraph(() -> healthWhitelist, "Player names separated by spaces to allow");
		    });
		});

		category("Misc", () -> {
		    switchField(() -> pickupEnabled, "Sneak to toggle between pickup states");
		    slider(() -> pickupDelay, "Sneaking delay in ticks", 1, 40);
		    selector(() -> pickupMode, "Pickup Mode", List.of("interesting", "lore", "tiered"));
		});

		category("_Debug", () -> {
		    switchField(() -> debugEnabled, "Toggle Debug");
		    switchField(() -> debugContainer, "Print contents of opened chests");
		});
		initialize(); // this needs to be called for whatever reason so that configs actually save
		*/
	}
}
