package net.usbwire.usbplus.interfaces;
/**
 * @author JsMacros
 * https://github.com/JsMacros/JsMacros/blob/backport-1.18.2/common/src/main/java/xyz/wagyourtail/jsmacros/client/access/IMixinEntity.java
 */
public interface EntityMixinInterface {

	public void usbplus_setGlowingColor(int glowingColor);

	public void usbplus_resetColor();

	/**
	 * @param glowing 1 for enabled, 2 for forced
	 */
	public void usbplus_setForceGlowing(int glowing);
}