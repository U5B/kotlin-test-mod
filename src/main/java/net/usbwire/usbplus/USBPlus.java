package net.usbwire.usbplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.commands.CommandUtil;
import net.usbwire.usbplus.util.MixinHelper;
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

	public static final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		logger.info("Starting usbplus!");
		MixinHelper.init();
		CommandUtil.init();
		logger.info("Started usbplus!");
	}
}
