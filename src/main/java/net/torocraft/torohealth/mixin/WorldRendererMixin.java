package net.torocraft.torohealth.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.HealthBarRenderer;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	private EntityRenderDispatcher entityRenderDispatcher;

	@Inject(method = "renderEntity", at = @At(value = "RETURN"))
	private void renderEntity(Entity entity, double x, double y, double z, float g,
			MatrixStack matrix, VertexConsumerProvider v, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			if (entity instanceof LivingEntity) {
				HealthBarRenderer.prepareRenderInWorld((LivingEntity) entity);
			}
		}
	}

	@Inject(method = "render", at = @At(value = "RETURN"))
	private void render(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
			GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix,
			Matrix4f projectionMatrix, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			HealthBarRenderer.renderInWorld(positionMatrix, camera);
		}
	}

}