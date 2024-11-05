package net.usbwire.usbplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
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
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.fabric.FabricClientCommandManager;

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

	public static final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		logger.info("Starting usbplus!");
		MixinHelper.init();
		// commands
		final CommandManager<FabricClientCommandSource> commandManager = FabricClientCommandManager.createNative(ExecutionCoordinator.simpleCoordinator());
		final AnnotationParser<FabricClientCommandSource> annotationParser = new AnnotationParser<FabricClientCommandSource>(commandManager, FabricClientCommandSource.class);
		try {
			annotationParser.parseContainers();
	} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.info("Started usbplus!");
	}
}
