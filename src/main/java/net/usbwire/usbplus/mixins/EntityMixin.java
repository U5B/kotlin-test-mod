package net.usbwire.usbplus.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.usbwire.usbplus.interfaces.EntityMixinInterface;

/**
 * @author JsMacros
 * https://github.com/JsMacros/JsMacros/blob/backport-1.18.2/common/src/main/java/xyz/wagyourtail/jsmacros/client/mixins/access/MixinEntity.java
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements EntityMixinInterface {
	@Unique
	private int glowingColor = -1;
	@Unique
	private int forceGlowing = 1;

	@Override
	public void usbplus_setGlowingColor(int glowingColor) {
		this.glowingColor = glowingColor & 0xFFFFFF;
	}

	@Override
	public void usbplus_resetColor() {
		glowingColor = -1;
	}

	@Inject(method = "getTeamColorValue()I", cancellable = true, at = @At("HEAD"))
	public void getTeamColorValue(CallbackInfoReturnable<Integer> ci) {
		if (glowingColor != -1) {
			ci.setReturnValue(glowingColor);
			ci.cancel();
		}
	}

	@Override
	public void usbplus_setForceGlowing(int glowing) {
		forceGlowing = glowing;
	}

	@Override
	public int usbplus_getForceGlowing() {
		return forceGlowing;
	}

	@Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
	public void isGlowing(CallbackInfoReturnable<Boolean> cir) {
		if (forceGlowing == 0) {
			cir.setReturnValue(false);
		} else if (forceGlowing == 2) {
			cir.setReturnValue(true);
		}
	}
}
