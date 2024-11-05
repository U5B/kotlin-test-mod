package net.usbwire.usbplus.features;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gg.essential.universal.wrappers.message.UMessage;
import net.usbwire.usbplus.USBPlus;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.util.Util;
import net.usbwire.usbplus.util.chat.Coordinates;

public class Poi {
	public static class JsonPoi {
		public String name;
		public String shard;
		public String region;
		public String subregion;
		public Coordinates.Coordinate coordinates;

		public JsonPoi(String name, String shard, String region, String subregion,
				Coordinates.Coordinate coordinates) {
			this.name = name;
			this.shard = shard;
			this.region = region;
			this.subregion = subregion;
			this.coordinates = coordinates;
		}
	}

	private static final Path poiPath = Path.of(USBPlus.configPath + "/pois.json");
	private static Map<String, JsonPoi> poiMap = new HashMap<>();
	private static List<String> poiSuggestions = new ArrayList<>();
	private static final Map<String, String> mapShardToDimension = Map.of("King's Valley",
			"monumenta:valley", "Celsian Isles", "monumenta:isles", "Architect's Ring", "monumenta:ring");

	public static void fetchPoiData() {
		try {
			URL url = new URL(Config.poiUrl);
			try (var reader = new InputStreamReader(url.openStream())) {
				Map<String, JsonPoi> project =
						new Gson().fromJson(reader, new TypeToken<Map<String, JsonPoi>>() {}.getType());
				updatePoiData(project);
				savePoiData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			USBPlus.logger.error("Invalid URL: " + Config.poiUrl);
			USBPlus.logger.error("Resetting URL to default!");
			Config.poiUrl = "https://raw.githubusercontent.com/U5B/Monumenta/main/out/pois.json";
		}
	}

	public static void loadPoiData() {
		if (Files.notExists(poiPath)) {
			fetchPoiData();
			return;
		}
		try (var reader = Files.newBufferedReader(poiPath)) {
			Map<String, JsonPoi> project =
					new Gson().fromJson(reader, new TypeToken<Map<String, JsonPoi>>() {}.getType());
			updatePoiData(project);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void updatePoiData(Map<String, JsonPoi> project) {
		poiMap = project;
		makeCommandSuggestions();
	}

	private static void savePoiData() {
		if (poiMap.isEmpty())
			return;
		Util.createPath(poiPath);
		try (var writer = Files.newBufferedWriter(poiPath)) {
			new Gson().toJson(poiMap, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String storedDimension = "";

	public static List<String> makeCommandSuggestions() {
		storedDimension = Util.getDimension();
		List<String> suggestions = new ArrayList<>();
		boolean isInShard = mapShardToDimension.values().contains(Util.getDimension());
		for (JsonPoi poi : poiMap.values()) {
			String dimension = mapShardToDimension.getOrDefault(poi.shard, Util.getDimension());
			if (isInShard && !dimension.equals(Util.getDimension()))
				continue;
			suggestions.add(poi.name);
		}
		poiSuggestions = new ArrayList<>(suggestions);
		return poiSuggestions;
	}

	public static List<String> getCommandSuggestions() {
		if (poiSuggestions.isEmpty() || !storedDimension.equals(Util.getDimension())) {
			return makeCommandSuggestions();
		}
		return poiSuggestions;
	}

	public static List<JsonPoi> searchPoi(String input) {
		List<JsonPoi> response = new ArrayList<>();
		for (Map.Entry<String, JsonPoi> entry : poiMap.entrySet()) {
			if (input.equals(entry.getValue().name)) {
				response.add(entry.getValue());
				return response;
			}
		}
		for (Map.Entry<String, JsonPoi> entry : poiMap.entrySet()) {
			List<String> tags = Arrays.asList(Util.trimString(entry.getValue().name).split(" "));
			if (tags.contains(Util.trimString(input))) {
				response.add(entry.getValue());
			}
		}
		if (response.isEmpty()) {
			for (Map.Entry<String, JsonPoi> entry : poiMap.entrySet()) {
				if (Util.cleanString(entry.getValue().name).contains(Util.cleanString(input))) {
					response.add(entry.getValue());
				}
			}
		}
		return response.isEmpty() ? null : response;
	}

	public static void responsePoi(JsonPoi poi) {
		if (poi.coordinates == null) {
			Util.chat("'" + poi.name + "': No coordinates for POI found.");
			return;
		}
		String dimension = mapShardToDimension.getOrDefault(poi.shard, Util.getDimension());
		String name = poi.name;
		int x = poi.coordinates.getX();
		int y = poi.coordinates.getY();
		int z = poi.coordinates.getZ();
		UMessage message = Coordinates.coordinateBuilder(name, x, y, z, dimension);
		Util.chat(message);
	}

	private static boolean firstRun = true;

	public static void configChanged() {
		if (Config.poiEnabled && firstRun) {
			loadPoiData();
			firstRun = false;
		}
	}

	private static final Pattern bountyRegex =
			Pattern.compile("^§r§fYour bounty for today is §r§b(.*)§r§f!$");

	public static boolean onChat(UMessage message) {
		if (!Config.poiEnabled || !bountyRegex.matcher(message.getFormattedText()).matches())
			return false;
		var matcher = bountyRegex.matcher(message.getFormattedText());
		if (!matcher.find())
			return false;
		String poiString = matcher.group(1);
		List<JsonPoi> searchPoi = searchPoi(poiString);
		if (searchPoi == null) {
			Util.chat("'" + poiString + "': No POI found!");
			return false;
		}
		for (JsonPoi poi : searchPoi) {
			responsePoi(poi);
		}
		return true;
	}
}
