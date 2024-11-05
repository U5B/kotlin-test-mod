package net.usbwire.usbplus.features.health;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint;
import gg.essential.elementa.constraints.ChildBasedSizeConstraint;
import gg.essential.elementa.constraints.ConstantColorConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.elementa.state.BasicState;
import gg.essential.elementa.state.State;
import gg.essential.elementa.components.Window;
import gg.essential.universal.UGraphics;
import gg.essential.universal.UMatrixStack;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.hud.CustomCenterConstraint;
import net.usbwire.usbplus.features.health.Base.PlayerHP;
import net.usbwire.usbplus.features.health.Base.PlayerHPDraw;
import net.usbwire.usbplus.features.health.Base.PlayerHPStates;
import net.usbwire.usbplus.features.health.Base.HealthData;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;
public class HUD {
	private static final DecimalFormat DEC = new DecimalFormat("0.0");
	public static final State<Float> xPos = new BasicState<>(Config.healthDrawX);
	public static final State<Float> yPos = new BasicState<>(Config.healthDrawY);
	public static final State<Float> alignPos = new BasicState<>(Config.healthDrawAlign);
	public static final State<Number> textSize = new BasicState<>(Config.healthDrawScale);
	public static final State<Boolean> alignRightExtra =
			new BasicState<>(Config.healthDrawAlignExtraRight);
	public static final Window window = new Window(ElementaVersion.V7, 60);
	public static final UIContainer container = new UIContainer();
	static {
		container.setX(new CustomCenterConstraint(xPos));
		container.setY(new CustomCenterConstraint(yPos));
		container.setWidth(new ChildBasedMaxSizeConstraint());
		container.setHeight(new ChildBasedSizeConstraint());
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
		State<Color> absorptionColorS = new BasicState<>(Color.ORANGE);
		// root container (contains everything)
		UIContainer rootC = new UIContainer();
		rootC.setX(new CustomCenterConstraint(alignPos));
		rootC.setY(new SiblingConstraint(2f + textSize.get().floatValue()));
		rootC.setWidth(new ChildBasedSizeConstraint());
		rootC.setHeight(new ChildBasedMaxSizeConstraint());
		rootC.setComponentName(name);
		UIText nameC = new UIText();
		nameC.setX(new SiblingConstraint(UGraphics.getCharWidth('l') * textSize.get().floatValue(), false));
		nameC.setColor(new ConstantColorConstraint(hpColorS));
		nameC.setTextScale(new PixelConstraint(textSize.get().floatValue(), false, false));
		nameC.bindText(nameS);
		nameC.setComponentName("name");
		UIText healthC = new UIText();
		healthC.setX(new SiblingConstraint(UGraphics.getCharWidth('l') * textSize.get().floatValue(), false));
		healthC.setColor(new ConstantColorConstraint(hpColorS));
		healthC.setTextScale(new PixelConstraint(textSize.get().floatValue(), false, false));
		healthC.bindText(healthS);
		healthC.setComponentName("health");
		UIText absorptionC = new UIText();
		absorptionC.setX(new SiblingConstraint(UGraphics.getCharWidth('l') * textSize.get().floatValue(), false));
		absorptionC.setColor(new ConstantColorConstraint(absorptionColorS));
		absorptionC.setTextScale(new PixelConstraint(textSize.get().floatValue(), false, false));
		absorptionC.bindText(absorptionS);
		absorptionC.setComponentName("absorption");
		UIText damageC = new UIText();
		damageC.setX(new SiblingConstraint(UGraphics.getCharWidth('l') * textSize.get().floatValue(), false));
		damageC.setColor(new ConstantColorConstraint(damageColorS));
		damageC.setTextScale(new PixelConstraint(textSize.get().floatValue(), false, false));
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
