package net.usbwire.usbplus.features.health;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint;
import gg.essential.elementa.constraints.ChildBasedSizeConstraint;
import gg.essential.elementa.constraints.ColorConstraint;
import gg.essential.elementa.constraints.HeightConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.elementa.dsl.ComponentsKt;
import gg.essential.elementa.dsl.ConstraintsKt;
import gg.essential.elementa.dsl.UtilitiesKt;
import gg.essential.elementa.state.BasicState;
import gg.essential.elementa.state.State;
import gg.essential.elementa.utils.TextKt;
import gg.essential.elementa.components.Window;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.wrappers.message.UMessage;
import gg.essential.universal.wrappers.message.UTextComponent;
import kotlin.Unit;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.hud.CustomCenterConstraint;
import net.usbwire.usbplus.features.health.Base.PlayerHP;
import net.usbwire.usbplus.features.health.Base.PlayerHPDraw;
import net.usbwire.usbplus.features.health.Base.PlayerHPStates;
import net.usbwire.usbplus.features.health.Base.HealthData;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class HUD {
	private static final DecimalFormat DEC = new DecimalFormat("0.0");
	public static final State<Float> xPos = new BasicState<>(Config.healthDrawX);
	public static final State<Float> yPos = new BasicState<>(Config.healthDrawY);
	public static final State<Float> alignPos = new BasicState<>(Config.healthDrawAlign);
	public static final State<Number> textSize = new BasicState<>(Config.healthDrawScale);
	public static final State<Boolean> alignRightExtra =
			new BasicState<>(Config.healthDrawAlignExtraRight);
	public static final Window window = new Window(ElementaVersion.V2, 60);
	public static final UIContainer container = ComponentsKt.constrain(new UIContainer(), c -> {
		c.setX(new CustomCenterConstraint(xPos));
		c.setY(new CustomCenterConstraint(yPos));
		c.setWidth(new ChildBasedMaxSizeConstraint());
		c.setHeight(new ChildBasedSizeConstraint());
		return Unit.INSTANCE;
	});
	static {
		window.addChild(container);
	}

	public static PlayerHPDraw createPlayer(String name) {
		// create states
		State<String> nameS = new BasicState<>(name + ":");
		State<String> healthS = new BasicState<>("0/0 ❤");
		State<String> absorptionS = new BasicState<>("");
		State<String> damageS = new BasicState<>("");
		State<Color> hpColorS = new BasicState<>(Color.WHITE);
		State<Color> damageColorS = new BasicState<>(Color.WHITE);
		// root container (contains everything)
		UIContainer rootC = ComponentsKt.constrain(new UIContainer(), c -> {
			c.setX(new CustomCenterConstraint(alignPos));
			c.setY(new SiblingConstraint(2f + textSize.get().floatValue()));
			c.setWidth(new ChildBasedSizeConstraint());
			c.setHeight(new ChildBasedMaxSizeConstraint());
			return Unit.INSTANCE;
		});
		rootC.setComponentName(name);
		UIText nameC = ComponentsKt.constrain(new UIText(), c -> {
			c.setX(new SiblingConstraint(UtilitiesKt.width('l', textSize.get().floatValue()), false));
			c.setColor(UtilitiesKt.toConstraint(hpColorS.get()));
			c.setTextScale(UtilitiesKt.pixels(textSize.get()));
			return Unit.INSTANCE;
		});
		nameC.bindText(nameS);
		nameC.setComponentName("name");
		UIText healthC = ComponentsKt.constrain(new UIText(), c -> {
			c.setX(new SiblingConstraint(UtilitiesKt.width('l', textSize.get().floatValue()), false));
			c.setColor(UtilitiesKt.toConstraint(hpColorS.get()));
			c.setTextScale(UtilitiesKt.pixels(textSize.get()));
			return Unit.INSTANCE;
		});
		healthC.bindText(healthS);
		healthC.setComponentName("health");
		UIText absorptionC = ComponentsKt.constrain(new UIText(), c -> {
			c.setX(new SiblingConstraint(UtilitiesKt.width('l', textSize.get().floatValue()), false));
			c.setColor(UtilitiesKt.toConstraint(Color.ORANGE));
			c.setTextScale(UtilitiesKt.pixels(textSize.get()));
			return Unit.INSTANCE;
		});
		absorptionC.bindText(absorptionS);
		absorptionC.setComponentName("absorption");
		UIText damageC = ComponentsKt.constrain(new UIText(), c -> {
			c.setX(new SiblingConstraint(UtilitiesKt.width('l', textSize.get().floatValue()), false));
			c.setColor(UtilitiesKt.toConstraint(damageColorS.get()));
			c.setTextScale(UtilitiesKt.pixels(textSize.get()));
			return Unit.INSTANCE;
		});
		damageC.bindText(damageS);
		damageC.setComponentName("damage");
		if (alignRightExtra.get()) { // reverse order
			rootC.addChild(damageC);
			rootC.addChild(nameC);
			rootC.addChild(healthC);
			rootC.addChild(absorptionC);
		} else {
			rootC.addChild(nameC);
			rootC.addChild(healthC);
			rootC.addChild(absorptionC);
			rootC.addChild(damageC);
		}
		PlayerHPStates states =
				new PlayerHPStates(nameS, healthS, absorptionS, damageS, hpColorS, damageColorS);
		return new PlayerHPDraw(rootC, states);
	}

	public static void updatePlayer(PlayerHP player, HealthData hp) {
		// set color
		player.draw.states.healthColor.set(hp.color);
		// health
		String maxHp = DEC.format(hp.max);
		String currentHp = DEC.format(hp.current);
		player.draw.states.health.set(currentHp + "/" + maxHp + " ❤");
		// absorption
		if (hp.absorption > 0) {
			String abHp = DEC.format(hp.absorption);
			player.draw.states.absorption.set("+" + abHp);
		} else {
			player.draw.states.absorption.set("");
		}
		// damage/heal change
		if (Config.healthDrawDamageEnabled) {
			if (player.health.current != hp.current) {
				float changeHp =
						(hp.current + hp.absorption) - (player.health.current + player.health.absorption);
				String damageHp = DEC.format(changeHp);
				if (changeHp > 0.1) {
					player.draw.states.damage.set("+" + damageHp);
					player.draw.states.damageColor.set(Config.healthGoodColor);
					player.draw.damageTick = -1;
				} else if (changeHp < -0.1) {
					player.draw.states.damage.set(damageHp); // no negative sign needed
					player.draw.states.damageColor.set(Config.healthCriticalColor);
					player.draw.damageTick = -1;
				}
			} else if (player.draw.damageTick > Config.healthDrawDamageDelay) {
				player.draw.states.damage.set("");
			} else {
				player.draw.damageTick++;
			}
		} else {
			player.draw.states.damage.set("");
		}
	}

	public static void add(PlayerHPDraw player) {
		if (!player.root.getHasParent()) {
			container.addChild(player.root);
		}
	}

	public static void remove(PlayerHPDraw player) {
		container.removeChild(player.root);
	}

	public static void sort(Map<String, PlayerHP> playerMap) {
		// sort container by max health and percent
		if (Config.healthDrawSort == 0) { // name
			container.getChildren().sort((a, b) -> a.getComponentName().compareTo(b.getComponentName()));
		} else if (Config.healthDrawSort == 1) { // health (low to high)
			container.getChildren()
					.sort((a, b) -> Float.compare(playerMap.get(a.getComponentName()).health.percent,
							playerMap.get(b.getComponentName()).health.percent));
		} else if (Config.healthDrawSort == 2) { // health (high to low)
			container.getChildren()
					.sort((a, b) -> Float.compare(playerMap.get(b.getComponentName()).health.percent,
							playerMap.get(a.getComponentName()).health.percent));
		}
	}

	public static void clear() {
		if (!container.getChildren().isEmpty())
			container.clearChildren();
	}

	public static void draw(UMatrixStack matrix) {
		if (!Config.healthDrawEnabled)
			return;
		if (!container.getChildren().isEmpty())
			window.draw(matrix);
	}
}
