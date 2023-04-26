// https://github.com/Splzh/ClearHitboxes
// https://github.com/W-OVERFLOW/Wyvtils/blob/1.17.1/src/main/java/xyz/qalcyo/rysm/mixins/EntityRenderDispatcherMixin.java
package net.usbwire.base.mixins;

import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gg.essential.vigilance.Vigilance;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawableHelper;
import java.awt.*;
import net.usbwire.base.util.Hitbox;
import net.usbwire.base.config.VigilanceConfig;

@Mixin(EntityRenderDispatcher.class)
public abstract class HitboxMixin {
  @Shadow
  private boolean renderHitboxes;

  @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
  private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
    Boolean cancel = Hitbox.INSTANCE.renderHitbox(matrices, vertices, entity);
    if (cancel == true) ci.cancel();
  }

  @Shadow
  public abstract void setRenderHitboxes(boolean value);

  @Inject(method = "render", at = @At("HEAD"))
  private void forceHitboxes(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
      if (!renderHitboxes && VigilanceConfig.INSTANCE.healthForcedHitbox()) {
          setRenderHitboxes(true);
      }
  }
}
