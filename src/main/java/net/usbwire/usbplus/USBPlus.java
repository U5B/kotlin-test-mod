package net.usbwire.usbplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.usbwire.usbplus.commands.ConfigCommand;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.commands.CompassCommand;
import net.usbwire.usbplus.util.MixinHelper;
import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.poi.*;
import org.apache.logging.log4j.*;

public class USBPlus implements ClientModInitializer {
	// get this from gradle.properties
	public static final FabricLoader fabricLoader = FabricLoader.getInstance();
	public static final String modid =
			fabricLoader.getModContainer("usbplus").get().getMetadata().getId();
	public static final String name =
			fabricLoader.getModContainer("usbplus").get().getMetadata().getName();
	public static final Logger logger = LogManager.getLogger(modid);
	public static final String configPath = "./config/" + modid;
	public static final Config config = new Config();

	// https://github.com/Skytils/SkytilsMod/blob/268e8e473a00e55cddc89c47653c3a00db263aac/src/main/kotlin/gg/skytils/skytilsmod/Skytils.kt#L122
	public static final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		logger.info("Starting usbplus!");
		new ConfigCommand().register();
		MixinHelper.init();
		logger.info("Started usbplus!");
	}
}
