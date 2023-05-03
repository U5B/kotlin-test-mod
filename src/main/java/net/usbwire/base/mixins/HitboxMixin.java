// https://github.com/Splzh/ClearHitboxes
// https://github.com/W-OVERFLOW/Wyvtils/blob/1.17.1/src/main/java/xyz/qalcyo/rysm/mixins/EntityRenderDispatcherMixin.java
package net.usbwire.base.mixins;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.usbwire.base.util.MixinHelper;

@Mixin(EntityRenderDispatcher.class)
public abstract class HitboxMixin {
  @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
  private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta,
      CallbackInfo ci) {
    Boolean cancel = MixinHelper.INSTANCE.renderHitbox(matrices, vertices, entity);
    if (cancel) ci.cancel();
  }
}
