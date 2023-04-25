// https://github.com/Splzh/ClearHitboxes
package net.usbwire.base.mixins;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.gui.DrawableHelper;
import java.awt.*;
import net.usbwire.base.util.Hitbox;

@Mixin(EntityRenderDispatcher.class)
public class HitboxMixin {
  @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
  private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
    Boolean cancel = Hitbox.INSTANCE.renderHitbox(matrices, vertices, entity, tickDelta);
    if (cancel == true) ci.cancel();
  }
}
