// https://github.com/Sk1erLLC/Patcher/blob/e70db7cdc9d0dbc116f276783b2b68937a0ce883/src/main/kotlin/club/sk1er/patcher/commands/PatcherPlayerArgumentParser.kt#L9
package net.usbwire.usbplus.commands.parsers;

import gg.essential.api.commands.ArgumentParser;
import gg.essential.api.commands.ArgumentQueue;
import net.usbwire.usbplus.features.Poi;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class PoiParser {
	public static class PoiCommandParser implements ArgumentParser<PoiName> {
		@Override
		public PoiName parse(ArgumentQueue arguments, Parameter param) {
			StringBuilder name = new StringBuilder();
			while (!arguments.isEmpty()) {
				name.append(arguments.poll());
				if (arguments.peek() != null)
					name.append(" ");
			}
			return new PoiName(name.toString());
		}

		@Override
		public List<String> complete(ArgumentQueue arguments, Parameter param) {
			StringBuilder name = new StringBuilder();
			while (!arguments.isEmpty()) {
				name.append(arguments.poll());
				if (arguments.peek() != null)
					name.append(" ");
			}
			List<String> suggestions = getPoiSuggestions();
			return suggestions.stream()
					.filter(s -> s.toLowerCase().startsWith(name.toString().toLowerCase()))
					.collect(Collectors.toList());
		}

		private List<String> getPoiSuggestions() {
			return Poi.getCommandSuggestions();
		}
	}

	public static class PoiName {
		private final String name;

		public PoiName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
