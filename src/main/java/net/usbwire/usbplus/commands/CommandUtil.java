package net.usbwire.usbplus.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.fabric.FabricClientCommandManager;
import org.incendo.cloud.parser.standard.BooleanParser;
import org.incendo.cloud.parser.standard.ByteParser;
import org.incendo.cloud.parser.standard.DoubleParser;
import org.incendo.cloud.parser.standard.FloatParser;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.incendo.cloud.parser.standard.StringParser;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;

public final class CommandUtil {

	private CommandUtil() {
	}
	public static void init() {
		// commands
		final CommandManager<FabricClientCommandSource> commandManager = FabricClientCommandManager.createNative(ExecutionCoordinator.simpleCoordinator());
		final AnnotationParser<FabricClientCommandSource> annotationParser = new AnnotationParser<FabricClientCommandSource>(commandManager, FabricClientCommandSource.class);
		try {
			annotationParser.parseContainers();
			parseConfigCommands(commandManager);
	} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void parseConfigCommands(final CommandManager<FabricClientCommandSource> commandManager) {
		// hacky way to parse list of config values into a massive command
		try {
			final Command.Builder<FabricClientCommandSource> base = commandManager.commandBuilder("usbplus").literal("config");
			commandManager.command(base.handler(ctx -> {
				Config.openConfig();
			}));
			for (Field field : USBPlus.config.getClass().getDeclaredFields()) {
				if (!Modifier.isPublic(field.getModifiers())) {
					continue;
				}
				if (!Config.isOption(field)) {
					continue;
				}
				// if (!Config.debugEnabled && Config.isOptionHidden(field)) {
				// 	continue;
				// }
				String optionString = Config.getOptionName(field).toLowerCase(Locale.US).replaceAll(" ", "_");
				Command.Builder<FabricClientCommandSource> result = null;
				switch (field.getGenericType().getTypeName()) {
					case "boolean": {
						result = base.literal(optionString).required(field.getName(), BooleanParser.booleanParser()).handler(context -> {
							tryCatchWrapper(context, field);
						});
						break;
					}
					case "byte": {
						result = base.literal(optionString).required(field.getName(), ByteParser.byteParser()).handler(context -> {
							tryCatchWrapper(context, field);
						});
						break;
					}
					case "int": {
						result = base.literal(optionString).required(field.getName(), IntegerParser.integerParser()).handler(context -> {
							tryCatchWrapper(context, field);
						});
						break;
					}
					case "double": {
						result = base.literal(optionString).required(field.getName(), DoubleParser.doubleParser()).handler(context -> {
							tryCatchWrapper(context, field);
						});
						break;
					}
					case "float": {
						result = base.literal(optionString).required(field.getName(), FloatParser.floatParser()).handler(context -> {
							tryCatchWrapper(context, field);
						});
						break;
					}
					case "java.lang.String": {
						result = base.literal(optionString).required(field.getName(), StringParser.greedyStringParser()).handler(context -> {
							tryCatchWrapper(context, field);
						});
						break;
					}
					default: {
						USBPlus.logger.error(() -> "Unsupported type for config field: " + field.getName() + " " + field.getGenericType().getTypeName());
						break;
					}
				}
				if (result != null) {
					commandManager.command(result);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void tryCatchWrapper(CommandContext<FabricClientCommandSource> context, Field field) {
		try {
			field.set(USBPlus.config, context.getOrDefault(field.getName(), field.get(USBPlus.config)));
		} catch (Exception e) {
			USBPlus.logger.error(() -> "Failed to set config value for: " + field.getName(), e);
		}
	}
}
