package net.usbwire.usbplus.features.health;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.state.State;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.usbwire.usbplus.config.Config;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.usbwire.usbplus.util.RenderUtil;
import net.usbwire.usbplus.util.MultiVersion.EntityHelper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Base {
	public static class HealthData {
		public float current;
		public float max;
		public float absorption;
		public float percent;
		public Color color;

		public HealthData(float current, float max, float absorption, float percent, Color color) {
			this.current = current;
			this.max = max;
			this.absorption = absorption;
			this.percent = percent;
			this.color = color;
		}
	}

	public static class PlayerHP {
		public String name;
		public HealthData health;
		public int tick = -1;
		public PlayerHPDraw draw = null;
		public boolean glow = false;

		public PlayerHP(String name, HealthData health) {
			this.name = name;
			this.health = health;
		}
	}

	public static class PlayerHPDraw {
		public UIComponent root;
		public PlayerHPStates states;
		public int damageTick = -1;

		public PlayerHPDraw(UIComponent root, PlayerHPStates states) {
			this.root = root;
			this.states = states;
		}
	}

	public static class PlayerHPStates {
		public State<String> name;
		public State<String> health;
		public State<String> absorption;
		public State<String> damage;
		public State<Color> healthColor;
		public State<Color> damageColor;

		public PlayerHPStates(State<String> name, State<String> health, State<String> absorption,
				State<String> damage, State<Color> healthColor, State<Color> damageColor) {
			this.name = name;
			this.health = health;
			this.absorption = absorption;
			this.damage = damage;
			this.healthColor = healthColor;
			this.damageColor = damageColor;
		}
	}

	public static class PlayerHPTicks {
		public int main = -1;
		public int damage = -1;
	}

	public static Map<String, PlayerHP> playerMap = new HashMap<>();
	public static List<String> currentPlayers = new ArrayList<>();

	private Base() {}

	public static HealthData getHealthProperties(PlayerEntity entity) {
		float current = entity.getHealth();
		float max = entity.getMaxHealth();
		float absorption = entity.getAbsorptionAmount();
		float percent = current / max;
		Color color;
		if (Config.healthHurtEnabled && entity.hurtTime != 0) {
			color = hasBadEffect(entity) != null ? hasBadEffect(entity) : Config.healthHurtColor;
		} else {
			color = getHealthColor(percent);
		}
		return new HealthData(current, max, absorption, percent, color);
	}

	public static Color getHealthColor(float percent) {
		if (percent >= Config.healthGoodPercent) {
			return Config.healthBaseColor;
		} else if (percent >= Config.healthLowPercent && percent <= Config.healthGoodPercent) {
			return Config.healthGoodColor;
		} else if (percent >= Config.healthCriticalPercent && percent <= Config.healthLowPercent) {
			return Config.healthLowColor;
		} else if (percent <= Config.healthCriticalPercent) {
			return Config.healthCriticalColor;
		}
		return Config.healthBaseColor;
	}

	public static Color hasBadEffect(PlayerEntity entity) {
		if (!Config.healthEffectEnabled)
			return null;
		if (entity.isOnFire() && !entity.isFireImmune())
			return Config.healthEffectColor;
		return null;
	}

	public static void updatePlayers(ClientWorld world) {
		List<AbstractClientPlayerEntity> worldPlayers = world.getPlayers();
		if (worldPlayers.isEmpty())
			return;
		Map<String, PlayerHP> previousPlayerMap = new HashMap<>(playerMap);
		currentPlayers.clear();
		for (PlayerEntity player : worldPlayers) {
			String name = new UMessage(new UTextComponent(player.getName())).getUnformattedText();
			if (Config.healthWhitelistEnabled
					&& !Config.healthWhitelist.toLowerCase().contains(name.toLowerCase()))
				continue;
			HealthData hp = getHealthProperties(player);
			if (!playerMap.containsKey(name))
				playerMap.put(name, new PlayerHP(name, hp));
			if (Config.healthDrawEnabled) {
				if (playerMap.get(name).draw == null)
					playerMap.get(name).draw = HUD.createPlayer(name);
				HUD.updatePlayer(playerMap.get(name), hp);
			}
			playerMap.get(name).tick++;
			playerMap.get(name).health = hp;
			currentPlayers.add(name);
		}
		for (Map.Entry<String, PlayerHP> player : previousPlayerMap.entrySet()) {
			if (currentPlayers.contains(player.getKey()))
				continue;
			playerMap.remove(player.getKey());
			PlayerHPDraw draw = player.getValue().draw;
			if (draw != null)
				HUD.remove(draw);
		}
		if (Config.healthDrawEnabled) {
			for (Map.Entry<String, PlayerHP> player : playerMap.entrySet()) {
				PlayerHPDraw draw = player.getValue().draw;
				if (draw != null)
					HUD.add(draw);
			}
			HUD.sort(playerMap);
		}
	}

	public static void resetPlayer(PlayerEntity player) {
		EntityHelper.resetGlowingColor(player);
	}

	public static boolean configDirty = true;

	public static void onWorldTick(ClientWorld world) {
		if (!Config.healthDrawEnabled && !Config.healthGlowingEnabled && !Config.healthDrawEnabled) {
			configDirty = true;
			return;
		}
		if (configDirty) {
			playerMap.clear();
			if (Config.healthDrawEnabled)
				HUD.clear();
			configDirty = false;
		}
		if (world.getTime() % Config.healthUpdateTicks == 0) {
			updatePlayers(world);
		}
	}
}
